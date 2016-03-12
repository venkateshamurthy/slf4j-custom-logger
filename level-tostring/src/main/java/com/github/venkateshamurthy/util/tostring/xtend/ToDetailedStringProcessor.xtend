package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.logging.LevelOfDetail
import com.github.venkateshamurthy.util.logging.LevelledToString
import com.google.common.base.Preconditions
import java.lang.annotation.ElementType
import java.lang.annotation.Target
import java.util.Collection
import java.util.List
import javax.annotation.PostConstruct
import org.eclipse.xtend.lib.annotations.Data
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.Visibility
import java.util.concurrent.atomic.AtomicBoolean

/**
 * This annotation allows user to specify attribute set of a target object to be printed for 
 * different levels.
 */
@Target(ElementType.TYPE)
@Active(ToLevelledStringCompilationParticipant)
annotation ToLevelledStringAnnotation {
    /** BRIEF/INFO level attributes. */
    String[] brief = #[]

    /** MEDIUM/DEBUG level attributes. */
    String[] medium = #[]
}

/**
 * This Class processor is responsible for adding level based toString methods to desired target 
 * classes
 */
class ToLevelledStringCompilationParticipant extends AbstractClassProcessor {

    /**Do transform. */
    override doTransform(MutableClassDeclaration clazz, extension TransformationContext context) {
        val annotation = clazz.getAnnotation(ToLevelledStringAnnotation)
        if (annotation != null) {
            // Get fields using a transform helper
            val briefFields = new TransformHelper(context, clazz, LevelOfDetail.BRIEF).postConstruct
            val mediumFields = new TransformHelper(context, clazz, LevelOfDetail.MEDIUM).postConstruct

            // Add toString(Level) method
            clazz.addMethod("toString") [
                returnType = context.newTypeReference("java.lang.String")
                visibility = Visibility.PUBLIC
                addParameter("level", context.newTypeReference(typeof(LevelOfDetail).name))
                body = [
                    '''
                        if (level==null ){
                           throw new IllegalArgumentException("level parameter cannot be null");
                        }
                        switch(level) {
                           case NONE   : return "";
                           case BRIEF  : return String.format("«briefFields.pattern»",  «briefFields.fieldsCsv»);
                           case MEDIUM : return String.format("«mediumFields.pattern»", «mediumFields.fieldsCsv»);
                           default     : return toString();
                        }
                    '''
                ]
                docComment = "{@inheritDoc}"
            ]
            // Add interface ToDetailedStringMarker
            clazz.implementedInterfaces = clazz.implementedInterfaces +
                #[context.newTypeReference(typeof(LevelledToString).name)]
        }
    }

    /** A static local extension  */
    def static getAnnotation(ClassDeclaration annotatedClass, Class<?> a) {
        System.out.println("Annotated class name:" + annotatedClass.simpleName + " Annotation name:" + a.name)
        return annotatedClass.annotations.findLast [
            it.annotationTypeDeclaration.qualifiedName == a.name
        ]
    }

    /** A helper class to hold transform related objects. */
    @Data
    private static class TransformHelper {
        TransformationContext context

        /** The {@link ToDetailedStringAnnotation} annotatedClass  */
        MutableClassDeclaration annotatedClass

        /** level of detail */
        LevelOfDetail level

        /** Field List */
        List<String> fields = newArrayList()

        AtomicBoolean hasSuperClass = new AtomicBoolean(false)

        @PostConstruct
        def postConstruct() {
            if (!annotatedClass.extendedClass.name.equals(Object.name)) {
                val MutableClassDeclaration superClazz = context.findClass(annotatedClass.extendedClass.name)
                if (superClazz != null) {
                    val isLevelledToString = superClazz.implementedInterfaces.exists [
                        it.name.equals(LevelledToString.name)
                    ]
                    hasSuperClass.set(isLevelledToString);
                    System.out.format("%s does have a superclass %s which further implement LevelledToString?%s %s\n",
                        annotatedClass.simpleName, annotatedClass.extendedClass.name, hasSuper, isLevelledToString)
                }
            }
            fields.removeAll()
            val List<String> fieldsAtLevel = getFieldsAtLevel();
            // System.out.println(fieldsAtLevel.get(0))
            fields.addAll(fieldsAtLevel)
            return this
        }

        def private boolean hasSuper() {
            System.out.format("%s has super class?%s \n", annotatedClass.simpleName, hasSuperClass.get)
            return hasSuperClass.get
        }

        /**
         * Get the printing format for as many fields indicated by the level during construction
         * @return a string containing format markers
         */
        def getPattern() {
            val StringBuilder sb = if(hasSuper) new StringBuilder("[%s] ") else new StringBuilder()
            for (String field : fields)
                sb.append("%s ")
            return sb.toString()
        }

        /**
         * Get field names or &lt;field&gt;.toString(level) as comma separated list
         * @return a string containing comma separated values of field names or &lt;field&gt;.toString(level)
         */
        def getFieldsCsv() {
            val StringBuilder sb = if(hasSuper) new StringBuilder("super.toString(level), ") else new StringBuilder()
            fields.forEach [
                // Get the class type of the field
                val field = annotatedClass.findDeclaredField(it)
                if (field == null) {
                    throw new Exception(
                        String::format("the field '%s' is absent in the class '%s'", it, annotatedClass.simpleName))
                }
                val fieldType = field.type
                // check if the class of this field has ToDetailedString implemented
                val componentType = if(fieldType.array) fieldType.arrayComponentType else fieldType
                val toLevelledStringInterfaceExists = componentType.declaredSuperTypes.exists [
                    it.name.equals(LevelledToString.name)
                ]
                // If toDetailedString Interface is implemented then check if it array
                if (toLevelledStringInterfaceExists) {
                    if (fieldType.array) {
                        val utilName = typeof(ToLevelledStringUtil).name
                        sb.append('''«utilName».toString(«it», level), ''')
                    } else {
                        sb.append(it).append(".toString(level), ");
                    }
                } else {
                    sb.append(it).append(", ")
                }
            ]
            // Remove the last trailing comma
            if (sb.lastIndexOf(",") != -1) {
                sb.replace(sb.lastIndexOf(","), sb.length, "")
            }
            return sb.toString()
        }

        /**
         * Get field names at different {@link LevelOfDetail level} as specified
         */
        def private getFieldsAtLevel() {
            val ref = annotatedClass.getAnnotation(ToLevelledStringAnnotation)
            val List<String> fieldList = newArrayList(ref.getStringArrayValue(level.name().toLowerCase()))
            if (fieldList.isEmpty()) {
                annotatedClass.declaredFields.forEach [
                    fieldList.add(it.simpleName)
                ]
            }
            return fieldList;
        }

    }
}

/** A simple utility to deal with group of {@link LevelledToString} objects. */
class ToLevelledStringUtil {

    /** 
     * Get level based printing on array of {@link LevelledToString} objects.
     * @param objects are collection/array of {@link LevelledToString}
     * @param level is {@link LevelOfDetail} indicating attributes of each object in set to be chosen
     * @return String form of level based attributes
     */
    def static String toString(LevelledToString[] objects, LevelOfDetail level) {
        val nonNullObjects = objects != null
        val nonNullLevel = level != null
        Preconditions.checkArgument(nonNullObjects && nonNullLevel,
            "The ToDetailedString[] objects or the level cannot be null/empty")
        val StringBuilder pattern = new StringBuilder()
        val objectsLength = objects.length
        val String[] fieldArray = newArrayOfSize(objectsLength)
        for (var int i = 0; i < objectsLength; i++) {
            pattern.append("%s ")
            val object = objects.get(i)
            val nonNullObject = object != null
            Preconditions.checkArgument(nonNullObject, "The elements in  object array cannot be null")
            val objectStringValue = object.toString(level)
            fieldArray.set(i, objectStringValue)
        }
        return String::format(pattern.toString(), fieldArray)
    }

    /** 
     * Get level based printing on array of {@link LevelledToString} objects.
     * @param objects are collection/array of {@link LevelledToString}
     * @param level is {@link LevelOfDetail} indicating attributes of each object in set to be chosen
     * @return String form of level based attributes
     */
    def static String toString(Collection<LevelledToString> objects, LevelOfDetail level) {
        val nonNullObjects = objects != null
        val nonNullLevel = level != null
        Preconditions.checkArgument(nonNullObjects && nonNullLevel,
            "The Collection<LevelledToString> objects or level cannot be null/empty")
        val StringBuilder pattern = new StringBuilder()
        val objectsLength = objects.length
        val String[] fieldArray = newArrayOfSize(objectsLength)
        for (var int i = 0; i < objectsLength; i++) {
            pattern.append("%s ")
            val object = objects.get(i)
            val nonNullObject = object != null
            Preconditions.checkArgument(nonNullObject, "The elements in object collection cannot be null")
            val objectStringValue = object.toString(level)
            fieldArray.set(i, objectStringValue)
        }
        return String::format(pattern.toString(), fieldArray)
    }
}
