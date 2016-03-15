package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.logging.LevelOfDetail
import com.github.venkateshamurthy.util.logging.LevelledToString
import com.google.common.base.Preconditions
import java.lang.annotation.ElementType
import java.lang.annotation.Target
import java.util.Arrays
import java.util.Collection
import java.util.List
import java.util.Set
import java.util.concurrent.atomic.AtomicBoolean
import javax.annotation.PostConstruct
import org.apache.commons.lang3.StringUtils
import org.eclipse.xtend.lib.annotations.Data
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration
import org.eclipse.xtend.lib.macro.declaration.TypeReference
import org.eclipse.xtend.lib.macro.declaration.Visibility
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
    val static Logger log = LoggerFactory.getLogger(typeof(ToLevelledStringCompilationParticipant).name)

    /**Do transform. */
    override doTransform(MutableClassDeclaration clazz, extension TransformationContext context) {
        val annotation = clazz.getAnnotation(ToLevelledStringAnnotation)
        if (annotation != null) {
            // Get fields using a transform helper
            val briefFields = new TransformHelper(context, clazz, LevelOfDetail.BRIEF).postConstruct
            val mediumFields = new TransformHelper(context, clazz, LevelOfDetail.MEDIUM).postConstruct
            if (clazz.findDeclaredField("log") == null) {
                clazz.addField("log") [
                    type = context.newTypeReference(typeof(Logger).name)
                    visibility = Visibility.PRIVATE
                    static = true
                    initializer = '''org.slf4j.LoggerFactory.getLogger(«clazz».class);'''
                ]
            }
            val TypeReference levelOfDetailRef = context.newTypeReference(typeof(LevelOfDetail).name)
            // Add toString(Level) method
            if (clazz.findDeclaredMethod("toString", levelOfDetailRef) == null) {
                clazz.addMethod("toString") [
                    returnType = context.newTypeReference("java.lang.String")
                    visibility = Visibility.PUBLIC
                    addParameter("level", levelOfDetailRef)
                    body = [
                        '''
                            log.trace("The special {}.toString({}) is invoked","«clazz.simpleName»",level.name());
                            if (level==null ){
                               throw new IllegalArgumentException("level parameter cannot be null");
                            }
                            switch(level) {
                               case NONE     : return "";
«««                            //case BRIEF  : return String.format("«briefFields.pattern»",  «briefFields.fieldsCsv»);
«««                            //case MEDIUM : return String.format("«mediumFields.pattern»", «mediumFields.fieldsCsv»);
                               case BRIEF    : return «briefFields.getStyledString()»;
                               case MEDIUM   : return «mediumFields.getStyledString()»;
                               default     : return toString();
                            }
                        '''
                    ]
                    docComment = "{@inheritDoc}"
                ]
            }
            // Add interface ToDetailedStringMarker
            clazz.implementedInterfaces = clazz.implementedInterfaces +
                #[context.newTypeReference(typeof(LevelledToString).name)]
        }
    }

    /** A static local extension  */
    def static getAnnotation(ClassDeclaration annotatedClass, Class<?> a) {
        log.debug("Annotated class name:" + annotatedClass.simpleName + " Annotation name:" + a.name)
        return annotatedClass.annotations.findLast [
            it.annotationTypeDeclaration.qualifiedName == a.name
        ]
    }

    /** A helper class to hold transform related objects. */
    @Data
    private static class TransformHelper {
        val TransformationContext context

        /** The {@link ToDetailedStringAnnotation} annotatedClass  */
        val MutableClassDeclaration annotatedClass

        /** level of detail */
        val LevelOfDetail level

        /** Field List */
        val List<String> fields = newArrayList()

        val AtomicBoolean hasSuperClassLevelledToString = new AtomicBoolean(false)
        val AtomicBoolean hasSuperClass = new AtomicBoolean(false)

        @PostConstruct
        def postConstruct() {
            if (!annotatedClass.extendedClass.name.equals(Object.name)) {
                val MutableClassDeclaration superClazz = context.findClass(annotatedClass.extendedClass.name)
                if (superClazz != null) {
                    hasSuperClass.set(true)
                    val isLevelledToString = superClazz.implementedInterfaces.exists [
                        it.name.equals(LevelledToString.name)
                    ]
                    hasSuperClassLevelledToString.set(isLevelledToString);
                    log.debug("{} does have a superclass {} which further implement LevelledToString?{} {}\n",
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
            log.trace("{} has super class?{} \n", annotatedClass.simpleName, hasSuperClassLevelledToString.get)
            return hasSuperClassLevelledToString.get
        }

        def getStyledString() {
            val StringBuilder sb = new StringBuilder('''new org.apache.commons.lang3.builder.ToStringBuilder(this,
                com.github.venkateshamurthy.util.logging.ToLevelledStringStyle.mapLevelToStringStyle.get(LevelOfDetail.«level»))''')
            if (hasSuperClass.get()) {
                sb.append(StringUtils.LF)
                if (hasSuperClassLevelledToString.get()) {
                    sb.append('''.append(super.toString(LevelOfDetail.«level»))''')
                } else {
                    sb.append('''append(super.toString())''')
                }
            }
            for (String field : fields) {
                sb.append(StringUtils.LF).append('''.append("«field»",«field»)''')
            }
            sb.append(StringUtils.LF).append('''.toString()''')
            sb.toString()
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
        

//        /**
//         * Get the printing format for as many fields indicated by the level during construction
//         * @return a string containing format markers
//         */
//        def getPattern() {
//            val StringBuilder sb = if(hasSuper) new StringBuilder("[%s] ") else new StringBuilder()
//            for (String field : fields)
//                sb.append("%s ")
//            return sb.toString()
//        }
//
//        def private static getCoreFieldType(MutableFieldDeclaration field) {
//            val fieldType = field.type
//            var TypeReference componentType = null
//            if (fieldType.array) {
//                componentType = fieldType.arrayComponentType
//                log.trace("The field {} is an array of type: {}\n", field.simpleName, componentType)
//            } else if (fieldType.actualTypeArguments != null && !fieldType.actualTypeArguments.isEmpty) {
//                componentType = fieldType.actualTypeArguments.head
//                log.trace("The field {} is mostly a collection and is of type: {}\n", field.simpleName, componentType)
//            } else {
//                componentType = fieldType
//                log.trace("The field {} is not an array/collection and is of type: {}\n", field.simpleName,
//                    componentType)
//            }
//            // check if the class of this field has ToDetailedString implemented
//            log.debug(
//                "field:{}  type:{} and arrayComponentType:{} list of types:{} final core type:{}\n",
//                field.simpleName,
//                fieldType,
//                fieldType.arrayComponentType,
//                Arrays.toString(fieldType.actualTypeArguments),
//                componentType
//            )
//            return componentType
//        }
//
//        def private static isCollectionOrArray(MutableFieldDeclaration field) {
//            return field.type.array || !field.type.actualTypeArguments.isEmpty
//        }
//
//        /**
//         * Get field names or &lt;field&gt;.toString(level) as comma separated list
//         * @return a string containing comma separated values of field names or &lt;field&gt;.toString(level)
//         */
//        def getFieldsCsv() {
//            val StringBuilder sb = if(hasSuper) new StringBuilder("super.toString(level), ") else new StringBuilder()
//            fields.forEach [
//                // Get the class type of the field
//                val field = annotatedClass.findDeclaredField(it)
//                if (field == null) {
//                    throw new Exception(String::format("the field '%s' is absent in the class '%s'.
//                        please specify the attributes present only in this class", it, annotatedClass.simpleName))
//                }
//
//                val componentType = getCoreFieldType(field)
//                if (componentType == null) {
//                    throw new Exception(String::format(
//                        "the field '%s' in the class '%s' doesn't have core component type '%s' which is strange!!",
//                        it,
//                        annotatedClass.simpleName,
//                        componentType
//                    ))
//                }
//                val toLevelledStringInterfaceExists = componentType.declaredSuperTypes != null &&
//                    componentType.declaredSuperTypes.exists [
//                        LevelledToString.name.equals(it.name)
//                    ]
//                // If toDetailedString Interface is implemented then check if it array
//                if (toLevelledStringInterfaceExists) {
//                    if (isCollectionOrArray(field)) {
//                        val utilName = typeof(ToLevelledStringUtil).name
//                        sb.append('''«it»==null?"null":«utilName».toString(«it», level), ''')
//                    } else {
//                        sb.append('''«it»==null?"null":«it».toString(level), ''');
//                    }
//                } else {
//                    sb.append(it).append(", ")
//                }
//            ]
//            // Remove the last trailing comma
//            if (sb.lastIndexOf(",") != -1) {
//                sb.replace(sb.lastIndexOf(","), sb.length, "")
//            }
//            return sb.toString()
//        }
    }
}
//
///** A simple utility to deal with group of {@link LevelledToString} objects. */
//class ToLevelledStringUtil {
//
//    /** 
//     * Get level based printing on array of {@link LevelledToString} objects.
//     * @param objects are collection/array of {@link LevelledToString}
//     * @param level is {@link LevelOfDetail} indicating attributes of each object in set to be chosen
//     * @return String form of level based attributes
//     */
//    def static String toString(LevelledToString[] objects, LevelOfDetail level) {
//        val Set<Object> seenSet = <Object>newHashSet()
//        val nonNullObjects = objects != null
//        val nonNullLevel = level != null
//        Preconditions.checkArgument(nonNullObjects && nonNullLevel,
//            "The ToDetailedString[] objects or the level cannot be null/empty")
//        val StringBuilder pattern = new StringBuilder()
//        val objectsLength = objects.length
//        val String[] fieldArray = newArrayOfSize(objectsLength)
//        for (var int i = 0; i < objectsLength; i++) {
//            val object = objects.get(i)
//            if (!seenSet.contains(object)) {
//                seenSet.add(object)
//                pattern.append("%s ")
//                val nonNullObject = object != null
//                Preconditions.checkArgument(nonNullObject, "The elements in  object array cannot be null")
//                val objectStringValue = object.toString(level)
//                fieldArray.set(i, objectStringValue)
//            } else {
//                fieldArray.set(i, "...")
//            }
//            seenSet.remove(object)
//        }
//        return String::format(pattern.toString(), fieldArray)
//    }
//
//    /** 
//     * Get level based printing on array of {@link LevelledToString} objects.
//     * @param objects are collection/array of {@link LevelledToString}
//     * @param level is {@link LevelOfDetail} indicating attributes of each object in set to be chosen
//     * @return String form of level based attributes
//     */
//    def static <T extends Collection<? extends LevelledToString>> String toString(T objects, LevelOfDetail level) {
//        val nonNullObjects = objects != null
//        val nonNullLevel = level != null
//        Preconditions.checkArgument(nonNullObjects && nonNullLevel,
//            "The Collection<LevelledToString> objects or level cannot be null/empty")
//        val Set<Object> seenSet = <Object>newHashSet()
//        val StringBuilder pattern = new StringBuilder()
//        val objectsLength = objects.size
//        val String[] fieldArray = newArrayOfSize(objectsLength)
//        var int i = 0;
//
//        for (LevelledToString lToS : objects) {
//            val object = lToS
//            if (!seenSet.contains(object)) {
//                seenSet.add(lToS)
//                pattern.append("%s ")
//
//                val nonNullObject = object != null
//                Preconditions.checkArgument(nonNullObject, "The elements in object collection cannot be null")
//                val objectStringValue = object.toString(level)
//                fieldArray.set(i++, objectStringValue)
//            } else {
//                fieldArray.set(i++, "...")
//            }
//            seenSet.remove(object)
//        }
//        return String::format(pattern.toString(), fieldArray)
//    }
//}
