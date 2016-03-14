package com.github.venkateshamurthy.util.tostring.xtend;

import com.github.venkateshamurthy.util.logging.LevelOfDetail;
import com.github.venkateshamurthy.util.logging.LevelledToString;
import com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringAnnotation;
import com.github.venkateshamurthy.util.tostring.xtend.ToLevelledStringUtil;
import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.AnnotationReference;
import org.eclipse.xtend.lib.macro.declaration.AnnotationTypeDeclaration;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.CompilationStrategy;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.TypeReference;
import org.eclipse.xtend.lib.macro.declaration.Visibility;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtend2.lib.StringConcatenationClient;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class processor is responsible for adding level based toString methods to desired target
 * classes
 */
@SuppressWarnings("all")
public class ToLevelledStringCompilationParticipant extends AbstractClassProcessor {
  /**
   * A helper class to hold transform related objects.
   */
  @Data
  private static class TransformHelper {
    private final TransformationContext context;
    
    /**
     * The {@link ToDetailedStringAnnotation} annotatedClass
     */
    private final MutableClassDeclaration annotatedClass;
    
    /**
     * level of detail
     */
    private final LevelOfDetail level;
    
    /**
     * Field List
     */
    private final List<String> fields = CollectionLiterals.<String>newArrayList();
    
    private final AtomicBoolean hasSuperClass = new AtomicBoolean(false);
    
    @PostConstruct
    public ToLevelledStringCompilationParticipant.TransformHelper postConstruct() {
      TypeReference _extendedClass = this.annotatedClass.getExtendedClass();
      String _name = _extendedClass.getName();
      String _name_1 = Object.class.getName();
      boolean _equals = _name.equals(_name_1);
      boolean _not = (!_equals);
      if (_not) {
        TypeReference _extendedClass_1 = this.annotatedClass.getExtendedClass();
        String _name_2 = _extendedClass_1.getName();
        final MutableClassDeclaration superClazz = this.context.findClass(_name_2);
        boolean _notEquals = (!Objects.equal(superClazz, null));
        if (_notEquals) {
          Iterable<? extends TypeReference> _implementedInterfaces = superClazz.getImplementedInterfaces();
          final Function1<TypeReference, Boolean> _function = new Function1<TypeReference, Boolean>() {
            @Override
            public Boolean apply(final TypeReference it) {
              String _name = it.getName();
              String _name_1 = LevelledToString.class.getName();
              return Boolean.valueOf(_name.equals(_name_1));
            }
          };
          final boolean isLevelledToString = IterableExtensions.exists(_implementedInterfaces, _function);
          this.hasSuperClass.set(isLevelledToString);
          String _simpleName = this.annotatedClass.getSimpleName();
          TypeReference _extendedClass_2 = this.annotatedClass.getExtendedClass();
          String _name_3 = _extendedClass_2.getName();
          boolean _hasSuper = this.hasSuper();
          ToLevelledStringCompilationParticipant.log.debug("{} does have a superclass {} which further implement LevelledToString?{} {}\n", _simpleName, _name_3, Boolean.valueOf(_hasSuper), Boolean.valueOf(isLevelledToString));
        }
      }
      CollectionExtensions.<String>removeAll(this.fields);
      final List<String> fieldsAtLevel = this.getFieldsAtLevel();
      this.fields.addAll(fieldsAtLevel);
      return this;
    }
    
    private boolean hasSuper() {
      String _simpleName = this.annotatedClass.getSimpleName();
      boolean _get = this.hasSuperClass.get();
      ToLevelledStringCompilationParticipant.log.trace("{} has super class?{} \n", _simpleName, Boolean.valueOf(_get));
      return this.hasSuperClass.get();
    }
    
    /**
     * Get the printing format for as many fields indicated by the level during construction
     * @return a string containing format markers
     */
    public String getPattern() {
      StringBuilder _xifexpression = null;
      boolean _hasSuper = this.hasSuper();
      if (_hasSuper) {
        _xifexpression = new StringBuilder("[%s] ");
      } else {
        _xifexpression = new StringBuilder();
      }
      final StringBuilder sb = _xifexpression;
      for (final String field : this.fields) {
        sb.append("%s ");
      }
      return sb.toString();
    }
    
    private static TypeReference getCoreFieldType(final MutableFieldDeclaration field) {
      final TypeReference fieldType = field.getType();
      TypeReference componentType = null;
      boolean _isArray = fieldType.isArray();
      if (_isArray) {
        TypeReference _arrayComponentType = fieldType.getArrayComponentType();
        componentType = _arrayComponentType;
        String _simpleName = field.getSimpleName();
        ToLevelledStringCompilationParticipant.log.trace("The field {} is an array of type: {}\n", _simpleName, componentType);
      } else {
        boolean _and = false;
        List<TypeReference> _actualTypeArguments = fieldType.getActualTypeArguments();
        boolean _notEquals = (!Objects.equal(_actualTypeArguments, null));
        if (!_notEquals) {
          _and = false;
        } else {
          List<TypeReference> _actualTypeArguments_1 = fieldType.getActualTypeArguments();
          boolean _isEmpty = _actualTypeArguments_1.isEmpty();
          boolean _not = (!_isEmpty);
          _and = _not;
        }
        if (_and) {
          List<TypeReference> _actualTypeArguments_2 = fieldType.getActualTypeArguments();
          TypeReference _head = IterableExtensions.<TypeReference>head(_actualTypeArguments_2);
          componentType = _head;
          String _simpleName_1 = field.getSimpleName();
          ToLevelledStringCompilationParticipant.log.trace("The field {} is mostly a collection and is of type: {}\n", _simpleName_1, componentType);
        } else {
          componentType = fieldType;
          String _simpleName_2 = field.getSimpleName();
          ToLevelledStringCompilationParticipant.log.trace("The field {} is not an array/collection and is of type: {}\n", _simpleName_2, componentType);
        }
      }
      String _simpleName_3 = field.getSimpleName();
      TypeReference _arrayComponentType_1 = fieldType.getArrayComponentType();
      List<TypeReference> _actualTypeArguments_3 = fieldType.getActualTypeArguments();
      String _string = Arrays.toString(((Object[])Conversions.unwrapArray(_actualTypeArguments_3, Object.class)));
      ToLevelledStringCompilationParticipant.log.debug(
        "field:{}  type:{} and arrayComponentType:{} list of types:{} final core type:{}\n", _simpleName_3, fieldType, _arrayComponentType_1, _string, componentType);
      return componentType;
    }
    
    private static boolean isCollectionOrArray(final MutableFieldDeclaration field) {
      boolean _or = false;
      TypeReference _type = field.getType();
      boolean _isArray = _type.isArray();
      if (_isArray) {
        _or = true;
      } else {
        TypeReference _type_1 = field.getType();
        List<TypeReference> _actualTypeArguments = _type_1.getActualTypeArguments();
        boolean _isEmpty = _actualTypeArguments.isEmpty();
        boolean _not = (!_isEmpty);
        _or = _not;
      }
      return _or;
    }
    
    /**
     * Get field names or &lt;field&gt;.toString(level) as comma separated list
     * @return a string containing comma separated values of field names or &lt;field&gt;.toString(level)
     */
    public String getFieldsCsv() {
      StringBuilder _xifexpression = null;
      boolean _hasSuper = this.hasSuper();
      if (_hasSuper) {
        _xifexpression = new StringBuilder("super.toString(level), ");
      } else {
        _xifexpression = new StringBuilder();
      }
      final StringBuilder sb = _xifexpression;
      final Procedure1<String> _function = new Procedure1<String>() {
        @Override
        public void apply(final String it) {
          try {
            final MutableFieldDeclaration field = TransformHelper.this.annotatedClass.findDeclaredField(it);
            boolean _equals = Objects.equal(field, null);
            if (_equals) {
              String _simpleName = TransformHelper.this.annotatedClass.getSimpleName();
              String _format = String.format("the field \'%s\' is absent in the class \'%s\'.\r\n                        please specify the attributes present only in this class", it, _simpleName);
              throw new Exception(_format);
            }
            final TypeReference componentType = ToLevelledStringCompilationParticipant.TransformHelper.getCoreFieldType(field);
            boolean _equals_1 = Objects.equal(componentType, null);
            if (_equals_1) {
              String _simpleName_1 = TransformHelper.this.annotatedClass.getSimpleName();
              String _format_1 = String.format("the field \'%s\' in the class \'%s\' doesn\'t have core component type \'%s\' which is strange!!", it, _simpleName_1, componentType);
              throw new Exception(_format_1);
            }
            boolean _and = false;
            Iterable<? extends TypeReference> _declaredSuperTypes = componentType.getDeclaredSuperTypes();
            boolean _notEquals = (!Objects.equal(_declaredSuperTypes, null));
            if (!_notEquals) {
              _and = false;
            } else {
              Iterable<? extends TypeReference> _declaredSuperTypes_1 = componentType.getDeclaredSuperTypes();
              final Function1<TypeReference, Boolean> _function = new Function1<TypeReference, Boolean>() {
                @Override
                public Boolean apply(final TypeReference it) {
                  String _name = LevelledToString.class.getName();
                  String _name_1 = it.getName();
                  return Boolean.valueOf(_name.equals(_name_1));
                }
              };
              boolean _exists = IterableExtensions.exists(_declaredSuperTypes_1, _function);
              _and = _exists;
            }
            final boolean toLevelledStringInterfaceExists = _and;
            if (toLevelledStringInterfaceExists) {
              boolean _isCollectionOrArray = ToLevelledStringCompilationParticipant.TransformHelper.isCollectionOrArray(field);
              if (_isCollectionOrArray) {
                final String utilName = ToLevelledStringUtil.class.getName();
                StringConcatenation _builder = new StringConcatenation();
                _builder.append(it, "");
                _builder.append("==null?\"null\":");
                _builder.append(utilName, "");
                _builder.append(".toString(");
                _builder.append(it, "");
                _builder.append(", level), ");
                sb.append(_builder);
              } else {
                StringConcatenation _builder_1 = new StringConcatenation();
                _builder_1.append(it, "");
                _builder_1.append("==null?\"null\":");
                _builder_1.append(it, "");
                _builder_1.append(".toString(level), ");
                sb.append(_builder_1);
              }
            } else {
              StringBuilder _append = sb.append(it);
              _append.append(", ");
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      IterableExtensions.<String>forEach(this.fields, _function);
      int _lastIndexOf = sb.lastIndexOf(",");
      boolean _notEquals = (_lastIndexOf != (-1));
      if (_notEquals) {
        int _lastIndexOf_1 = sb.lastIndexOf(",");
        int _length = sb.length();
        sb.replace(_lastIndexOf_1, _length, "");
      }
      return sb.toString();
    }
    
    /**
     * Get field names at different {@link LevelOfDetail level} as specified
     */
    private List<String> getFieldsAtLevel() {
      final AnnotationReference ref = ToLevelledStringCompilationParticipant.getAnnotation(this.annotatedClass, ToLevelledStringAnnotation.class);
      String _name = this.level.name();
      String _lowerCase = _name.toLowerCase();
      String[] _stringArrayValue = ref.getStringArrayValue(_lowerCase);
      final List<String> fieldList = CollectionLiterals.<String>newArrayList(_stringArrayValue);
      boolean _isEmpty = fieldList.isEmpty();
      if (_isEmpty) {
        Iterable<? extends MutableFieldDeclaration> _declaredFields = this.annotatedClass.getDeclaredFields();
        final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
          @Override
          public void apply(final MutableFieldDeclaration it) {
            String _simpleName = it.getSimpleName();
            fieldList.add(_simpleName);
          }
        };
        IterableExtensions.forEach(_declaredFields, _function);
      }
      return fieldList;
    }
    
    public TransformHelper(final TransformationContext context, final MutableClassDeclaration annotatedClass, final LevelOfDetail level) {
      super();
      this.context = context;
      this.annotatedClass = annotatedClass;
      this.level = level;
    }
    
    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.context== null) ? 0 : this.context.hashCode());
      result = prime * result + ((this.annotatedClass== null) ? 0 : this.annotatedClass.hashCode());
      result = prime * result + ((this.level== null) ? 0 : this.level.hashCode());
      result = prime * result + ((this.fields== null) ? 0 : this.fields.hashCode());
      result = prime * result + ((this.hasSuperClass== null) ? 0 : this.hasSuperClass.hashCode());
      return result;
    }
    
    @Override
    @Pure
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ToLevelledStringCompilationParticipant.TransformHelper other = (ToLevelledStringCompilationParticipant.TransformHelper) obj;
      if (this.context == null) {
        if (other.context != null)
          return false;
      } else if (!this.context.equals(other.context))
        return false;
      if (this.annotatedClass == null) {
        if (other.annotatedClass != null)
          return false;
      } else if (!this.annotatedClass.equals(other.annotatedClass))
        return false;
      if (this.level == null) {
        if (other.level != null)
          return false;
      } else if (!this.level.equals(other.level))
        return false;
      if (this.fields == null) {
        if (other.fields != null)
          return false;
      } else if (!this.fields.equals(other.fields))
        return false;
      if (this.hasSuperClass == null) {
        if (other.hasSuperClass != null)
          return false;
      } else if (!this.hasSuperClass.equals(other.hasSuperClass))
        return false;
      return true;
    }
    
    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("context", this.context);
      b.add("annotatedClass", this.annotatedClass);
      b.add("level", this.level);
      b.add("fields", this.fields);
      b.add("hasSuperClass", this.hasSuperClass);
      return b.toString();
    }
    
    @Pure
    public TransformationContext getContext() {
      return this.context;
    }
    
    @Pure
    public MutableClassDeclaration getAnnotatedClass() {
      return this.annotatedClass;
    }
    
    @Pure
    public LevelOfDetail getLevel() {
      return this.level;
    }
    
    @Pure
    public List<String> getFields() {
      return this.fields;
    }
    
    @Pure
    public AtomicBoolean getHasSuperClass() {
      return this.hasSuperClass;
    }
  }
  
  private final static Logger log = LoggerFactory.getLogger(ToLevelledStringCompilationParticipant.class.getName());
  
  /**
   * Do transform.
   */
  @Override
  public void doTransform(final MutableClassDeclaration clazz, @Extension final TransformationContext context) {
    final AnnotationReference annotation = ToLevelledStringCompilationParticipant.getAnnotation(clazz, ToLevelledStringAnnotation.class);
    boolean _notEquals = (!Objects.equal(annotation, null));
    if (_notEquals) {
      ToLevelledStringCompilationParticipant.TransformHelper _transformHelper = new ToLevelledStringCompilationParticipant.TransformHelper(context, clazz, LevelOfDetail.BRIEF);
      final ToLevelledStringCompilationParticipant.TransformHelper briefFields = _transformHelper.postConstruct();
      ToLevelledStringCompilationParticipant.TransformHelper _transformHelper_1 = new ToLevelledStringCompilationParticipant.TransformHelper(context, clazz, LevelOfDetail.MEDIUM);
      final ToLevelledStringCompilationParticipant.TransformHelper mediumFields = _transformHelper_1.postConstruct();
      MutableFieldDeclaration _findDeclaredField = clazz.findDeclaredField("log");
      boolean _equals = Objects.equal(_findDeclaredField, null);
      if (_equals) {
        final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
          @Override
          public void apply(final MutableFieldDeclaration it) {
            String _name = Logger.class.getName();
            TypeReference _newTypeReference = context.newTypeReference(_name);
            it.setType(_newTypeReference);
            it.setVisibility(Visibility.PRIVATE);
            it.setStatic(true);
            StringConcatenationClient _client = new StringConcatenationClient() {
              @Override
              protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
                _builder.append("org.slf4j.LoggerFactory.getLogger(");
                _builder.append(clazz, "");
                _builder.append(".class);");
              }
            };
            it.setInitializer(_client);
          }
        };
        clazz.addField("log", _function);
      }
      String _name = LevelOfDetail.class.getName();
      final TypeReference levelOfDetailRef = context.newTypeReference(_name);
      MutableMethodDeclaration _findDeclaredMethod = clazz.findDeclaredMethod("toString", levelOfDetailRef);
      boolean _equals_1 = Objects.equal(_findDeclaredMethod, null);
      if (_equals_1) {
        final Procedure1<MutableMethodDeclaration> _function_1 = new Procedure1<MutableMethodDeclaration>() {
          @Override
          public void apply(final MutableMethodDeclaration it) {
            TypeReference _newTypeReference = context.newTypeReference("java.lang.String");
            it.setReturnType(_newTypeReference);
            it.setVisibility(Visibility.PUBLIC);
            it.addParameter("level", levelOfDetailRef);
            final CompilationStrategy _function = new CompilationStrategy() {
              @Override
              public CharSequence compile(final CompilationStrategy.CompilationContext it) {
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("log.trace(\"The special {}.toString({}) is invoked\",\"");
                String _simpleName = clazz.getSimpleName();
                _builder.append(_simpleName, "");
                _builder.append("\",level.name());");
                _builder.newLineIfNotEmpty();
                _builder.append("if (level==null ){");
                _builder.newLine();
                _builder.append("   ");
                _builder.append("throw new IllegalArgumentException(\"level parameter cannot be null\");");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
                _builder.append("switch(level) {");
                _builder.newLine();
                _builder.append("   ");
                _builder.append("case NONE   : return \"\";");
                _builder.newLine();
                _builder.append("   ");
                _builder.append("case BRIEF  : return String.format(\"");
                String _pattern = briefFields.getPattern();
                _builder.append(_pattern, "   ");
                _builder.append("\",  ");
                String _fieldsCsv = briefFields.getFieldsCsv();
                _builder.append(_fieldsCsv, "   ");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("   ");
                _builder.append("case MEDIUM : return String.format(\"");
                String _pattern_1 = mediumFields.getPattern();
                _builder.append(_pattern_1, "   ");
                _builder.append("\", ");
                String _fieldsCsv_1 = mediumFields.getFieldsCsv();
                _builder.append(_fieldsCsv_1, "   ");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("   ");
                _builder.append("default     : return toString();");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
                return _builder;
              }
            };
            it.setBody(_function);
            it.setDocComment("{@inheritDoc}");
          }
        };
        clazz.addMethod("toString", _function_1);
      }
      Iterable<? extends TypeReference> _implementedInterfaces = clazz.getImplementedInterfaces();
      String _name_1 = LevelledToString.class.getName();
      TypeReference _newTypeReference = context.newTypeReference(_name_1);
      Iterable<TypeReference> _plus = Iterables.<TypeReference>concat(_implementedInterfaces, 
        Collections.<TypeReference>unmodifiableList(CollectionLiterals.<TypeReference>newArrayList(_newTypeReference)));
      clazz.setImplementedInterfaces(_plus);
    }
  }
  
  /**
   * A static local extension
   */
  public static AnnotationReference getAnnotation(final ClassDeclaration annotatedClass, final Class<?> a) {
    String _simpleName = annotatedClass.getSimpleName();
    String _plus = ("Annotated class name:" + _simpleName);
    String _plus_1 = (_plus + " Annotation name:");
    String _name = a.getName();
    String _plus_2 = (_plus_1 + _name);
    ToLevelledStringCompilationParticipant.log.debug(_plus_2);
    Iterable<? extends AnnotationReference> _annotations = annotatedClass.getAnnotations();
    final Function1<AnnotationReference, Boolean> _function = new Function1<AnnotationReference, Boolean>() {
      @Override
      public Boolean apply(final AnnotationReference it) {
        AnnotationTypeDeclaration _annotationTypeDeclaration = it.getAnnotationTypeDeclaration();
        String _qualifiedName = _annotationTypeDeclaration.getQualifiedName();
        String _name = a.getName();
        return Boolean.valueOf(Objects.equal(_qualifiedName, _name));
      }
    };
    return IterableExtensions.findLast(_annotations, _function);
  }
}