package org.isite.commons.lang;

import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.isite.commons.lang.json.Comment;
import org.isite.commons.lang.json.JsonField;
import org.isite.commons.lang.json.JsonType;
import org.isite.commons.lang.utils.TypeUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
/**
 * @Description 反射工具类
 * @Author <font color='blue'>zhangcm</font>
 */
public class Reflection {
    /**
     * Getter方法
     */
    private static final Pattern GET_PATTERN = Pattern.compile("^get[A-Z].*");
    /**
     * is方法
     */
    private static final Pattern IS_PATTERN = Pattern.compile("^is[A-Z].*");

    private Reflection() {
    }

    /**
     * 获取当前类或父类或父接口的所有字段，包括 public/protected/private 修饰的字段
     */
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> results = new ArrayList<>();
        // getFields 获取当前类或父类或父接口的 public 修饰的字段
        // getDeclaredFields 获取当前类的所有字段，包括 public/protected/private 修饰的字段
        Field[] fields = clazz.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            results.addAll(Arrays.asList(fields));
        }
        Class<?> superclass = clazz.getSuperclass();
        if (null != superclass) {
            List<Field> list = getFields(superclass);
            if (CollectionUtils.isNotEmpty(list)) {
                results.addAll(list);
            }
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        if (ArrayUtils.isNotEmpty(interfaces)) {
            for (Class<?> item : interfaces) {
                List<Field> list = getFields(item);
                if (CollectionUtils.isNotEmpty(list)) {
                    results.addAll(list);
                }
            }
        }
        return results;
    }

    /**
     * 获取当前类或父类或父接口的字段值
     */
    @SneakyThrows
    public static Object getValue(Object object, String attribute) {
        Field field = getField(object.getClass(), attribute);
        if (null != field) {
            return getValue(object, field);
        }
        return null;
    }

    /**
     * 获取当前类或父类或父接口的字段值
     */
    @SneakyThrows
    public static Object getValue(Object object, Field field) {
        //屏蔽Java语言的访问检查(public、private字段，默认都是开启访问检查)，使得对象的私有属性也可以被查询和设置
        field.setAccessible(Boolean.TRUE);
        Object value = field.get(object);
        field.setAccessible(Boolean.FALSE);
        return value;
    }

    /**
     * 给对象的属性赋值
     */
    @SneakyThrows
    public static void setValue(Object object, String attribute, Object value) {
        Field field = getField(object.getClass(), attribute);
        if (null != field) {
            //屏蔽Java语言的访问检查(public、private字段，默认都是开启访问检查)，使得对象的私有属性也可以被查询和设置
            field.setAccessible(Boolean.TRUE);
            field.set(object, value);
            field.setAccessible(Boolean.FALSE);
        }
    }

    /**
     * 获取当前类或父类或父接口的字段
     */
    public static Field getField(Class<?> clazz, String name) {
        Field[] fields = clazz.getDeclaredFields();
        if (ArrayUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                if (field.getName().equals(name)) {
                    return field;
                }
            }
        }
        Class<?> superclass = clazz.getSuperclass();
        if (null != superclass) {
            Field field = getField(superclass, name);
            if (null != field) {
                return field;
            }
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        if (ArrayUtils.isNotEmpty(interfaces)) {
            for (Class<?> item : interfaces) {
                Field field = getField(item, name);
                if (null != field) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * 获取泛型参数
     * @param type 泛型类
     */
    public static Class<?> getGenericParameter(Type type) {
        return getGenericParameter(type, Constants.ZERO);
    }

    /**
     * @Description 获取泛型参数
     * @param type 泛型类
     * @param index 泛型参数数组下标，从0开始
     */
    private static Class<?> getGenericParameter(Type type, int index) {
        //该类是否支持泛型
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getActualTypeArguments()[index];
            if (type instanceof ParameterizedType) {
                return TypeUtils.cast(((ParameterizedType) type).getRawType());
            }
            return (Class<?>) type;
        }
        return null;
    }

    /**
     * @Description 获取泛型参数
     * @param objectClass 对象类
     * @param genericClass 对象类继承的泛型接口或泛型父类
     */
    public static Class<?> getGenericParameter(Class<?> objectClass, Class<?> genericClass) {
        return getGenericParameter(objectClass, genericClass, Constants.ZERO);
    }

    /**
     * @Description 获取泛型参数
     * @param objectClass 对象类
     * @param genericClass 对象类继承的泛型接口或泛型父类
     * @param index 泛型参数数组下标，从0开始
     */
    public static Class<?> getGenericParameter(Class<?> objectClass, Class<?> genericClass, int index) {
        //判断是否为接口
        if (genericClass.isInterface()) {
            //返回object类直接实现的接口（包含泛型参数）返回值null说明没有直接实现的接口
            return getGenericParameter(getGenericInterface(objectClass.getGenericInterfaces(), genericClass), index);
        } else {
            //返回object类直接继承的父类（包含泛型参数）,返回值null说明没有直接的父类
            return getGenericParameter(getGenericSuperclass(objectClass.getGenericSuperclass(), genericClass), index);
        }
    }

    /**
     * @Description 获取泛型参数
     * Java的泛型是伪泛型，这是因为Java在编译期间，所有的泛型信息都会被擦掉。
     * Java的泛型基本上都是在编译器这个层次上实现的，在生成的字节码中是不包含泛型中的类型信息的，
     * 由于泛型类型在运行时被消除，泛型不能用于显性地引用运行时类型的操作之中，在运行时需要获取泛型类型信息的操作都无法进行工作。
     * 因此，对于如何使用泛型类型是有一些限制的:
     * 1）不能使用new E[]
     * 2）在静态方法，数据域中或初始化语句中，为类引用泛型类型的参数是非法的；
     * 3）异常类不能是泛型的
     *
     * @param objectClass 对象类
     * @param genericClass 对象类继承的泛型接口或泛型父类
     */
    public static Class<?>[] getGenericParameters(Class<?> objectClass, Class<?> genericClass) {
        Type type;
        //判断是否为接口
        if (genericClass.isInterface()) {
            //返回object类直接实现的接口（包含泛型参数）返回值null说明没有直接实现的接口
            type = getGenericInterface(objectClass.getGenericInterfaces(), genericClass);
        } else {
            //返回object类直接继承的父类（包含泛型参数）,返回值null说明没有直接的父类
            type = getGenericSuperclass(objectClass.getGenericSuperclass(), genericClass);
        }
        //该类是否支持泛型
        if (type instanceof ParameterizedType) {
            return getGenericParameters((ParameterizedType) type);
        }
        return new Class[Constants.ZERO];
    }

    /**
     * 获取泛型参数
     */
    private static Class<?>[] getGenericParameters(ParameterizedType type) {
        Type[] types = type.getActualTypeArguments();
        Class<?>[] arguments = new Class[types.length];
        for (int i = Constants.ZERO; i < types.length; i++) {
            if (types[i] instanceof ParameterizedType) {
                arguments[i] = TypeUtils.cast(((ParameterizedType) types[i]).getRawType());
            } else {
                arguments[i] = TypeUtils.cast(types[i]);
            }
        }
        return arguments;
    }

    /**
     * 返回type的泛型父类genericClass，包含泛型参数
     */
    private static Type getGenericSuperclass(Type type, Class<?> genericClass) {
        if (null == type) {
            return null;
        }
        Class<?> tClass;
        if (type instanceof ParameterizedType) {
            tClass = TypeUtils.cast(((ParameterizedType) type).getRawType());
        } else {
            tClass = TypeUtils.cast(type);
        }
        if (genericClass.equals(tClass)) {
            return type;
        }
        return getGenericSuperclass(tClass.getGenericSuperclass(), genericClass);
    }

    /**
     * 返回types的泛型接口genericInterface，包含泛型参数
     */
    private static Type getGenericInterface(Type[] types, Class<?> genericInterface) {
        if (ArrayUtils.isEmpty(types)) {
            return null;
        }
        Class<?> tClass;
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                tClass = TypeUtils.cast(((ParameterizedType) type).getRawType());
            } else {
                tClass = TypeUtils.cast(type);
            }
            if (genericInterface.equals(tClass)) {
                return type;
            }
            type = getGenericInterface(tClass.getGenericInterfaces(), genericInterface);
            if (null != type) {
                return type;
            }
        }
        return null;
    }

    /**
     * 转换当前对象类或父类或父接口的所有字段为JsonField（必须有非 static 的 getter/is 方法）
     */
    public static List<JsonField> toJsonFields(Object object) {
        if (null == object) {
            return Collections.emptyList();
        }
        List<JsonField> jsonFields = new ArrayList<>();
        toJsonFields(object.getClass(), object, jsonFields);
        return jsonFields;
    }

    /**
     * 转换当前类或父类或父接口的所有字段为JsonField（必须有非 static 的 getter/is 方法）
     */
    private static void toJsonFields(Class<?> clazz, Object object, List<JsonField> jsonFields) {
        if (Object.class.equals(clazz) || Class.class.equals(clazz) || Enum.class.equals(clazz)) {
            return;
        }

        // getDeclaredMethods 获取的是类自身声明的所有方法，包含public、protected和private方法
        // getMethods 获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法（包括Object的方法）
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterCount() > Constants.ZERO || Void.TYPE.equals(method.getReturnType()) ||
                    Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            toJsonField(method, object, jsonFields);
        }

        Class<?> superclass = clazz.getSuperclass();
        if (null != superclass) {
            toJsonFields(superclass, object, jsonFields);
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        if (ArrayUtils.isNotEmpty(interfaces)) {
            for (Class<?> item : interfaces) {
                toJsonFields(item, object, jsonFields);
            }
        }
    }

    /**
     * 转换当前类或父类或父接口的所有字段（必须有Getter方法）为JsonField
     */
    public static List<JsonField> toJsonFields(Class<?> clazz) {
        if (null == clazz) {
            return Collections.emptyList();
        }
        List<JsonField> jsonFields = new ArrayList<>();
        toJsonFields(clazz, null, jsonFields);
        return jsonFields;
    }

    /**
     * 转换当前Getter为JsonField
     */
    @SneakyThrows
    private static void toJsonField(Method method, Object object, List<JsonField> jsonFields) {
        String fieldName = toFieldName(method.getName());
        if (null == fieldName || contains(jsonFields, fieldName)) {
            return;
        }
        JsonField jsonField = new JsonField();
        jsonField.setName(fieldName);
        if (null != object) {
            object = method.invoke(object);
        }
        jsonField.setType(JsonType.getType(null == object ? method.getReturnType() : object.getClass()));
        jsonField.setComment(getFieldComment(method, fieldName));
        jsonField.setData(getJsonFieldData(method, object, jsonField));
        jsonFields.add(jsonField);
    }

    /**
     * 获取JSON字段数据
     */
    private static Object getJsonFieldData(Method method, Object returnValue, JsonField jsonField) {
        Class<?> returnType = null == returnValue ? method.getReturnType() : returnValue.getClass();
        if (JsonType.OBJECT.equals(jsonField.getType())) {
            List<JsonField> dataFields = new ArrayList<>();
            if (Class.class.equals(returnType)) {
                toJsonFields(null != returnValue ? TypeUtils.cast(returnValue) : returnType, null, dataFields);
            } else {
                toJsonFields(returnType, returnValue, dataFields);
            }
            return dataFields;
        } else if (returnType.isArray()) {
            //处理数组类型,getComponentType()返回数组元素的类型
            return toJsonFields(returnType.getComponentType(), TypeUtils.castArray(returnValue, Object.class));
        } else if (JsonType.ARRAY.equals(jsonField.getType())) {
            //处理集合类型
            Collection<?> collections = TypeUtils.cast(returnValue);
            //method.getGenericReturnType：返回带有泛型参数的返回类
            return toJsonFields(getGenericParameter(method.getGenericReturnType()), collections);
        }
        return returnValue;
    }

    /**
     * 获取JSON字段注释
     */
    private static String getFieldComment(Method method, String fieldName) {
        //getAnnotation可以获取到父类中可以继承来的注解
        Comment comment = method.getDeclaredAnnotation(Comment.class);
        if (null == comment) {
            // method.getDeclaringClass() 获取当前方法所在的类的Class
            Field field = getField(method.getDeclaringClass(), fieldName);
            if (null != field) {
                comment = field.getAnnotation(Comment.class);
            }
        }
        return null == comment ? null : comment.value();
    }

    /**
     * 判断集合中是否已包含指定JSON字段
     */
    private static boolean contains(List<JsonField> jsonFields, String fieldName) {
        for (JsonField jsonField : jsonFields) {
            if (jsonField.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换当前数组元素类的所有字段（必须有Getter方法）为JsonField
     */
    private static List<List<JsonField>> toJsonFields(Class<?> clazz, Object[] objects) {
        List<List<JsonField>> arrayFields = new ArrayList<>();
        if (ArrayUtils.isEmpty(objects)) {
            arrayFields.add(toJsonFields(clazz));
        } else {
            for (Object object : objects) {
                List<JsonField> jsonFields = new ArrayList<>();
                toJsonFields(clazz, object, jsonFields);
                arrayFields.add(jsonFields);
            }
        }
        return arrayFields;
    }

    /**
     * 转换当前集合元素类的所有字段（必须有Getter方法）为JsonField
     */
    private static List<List<JsonField>> toJsonFields(Class<?> clazz, Collection<?> objects) {
        List<List<JsonField>> collectionFields = new ArrayList<>();
        if (CollectionUtils.isEmpty(objects)) {
            collectionFields.add(toJsonFields(clazz));
        } else {
            objects.forEach(object -> {
                List<JsonField> jsonFields = new ArrayList<>();
                toJsonFields(object.getClass(), object, jsonFields);
                collectionFields.add(jsonFields);
            });
        }
        return collectionFields;
    }

    /**
     * Getter方法名转字段名
     */
    public static String toFieldName(String getter) {
        if (GET_PATTERN.matcher(getter).matches()) {
            getter = getter.substring(Constants.THREE);
            return getter.substring(Constants.ZERO, Constants.ONE).toLowerCase() + getter.substring(Constants.ONE);
        } else if(IS_PATTERN.matcher(getter).matches()) {
            getter = getter.substring(Constants.TWO);
            return getter.substring(Constants.ZERO, Constants.ONE).toLowerCase() + getter.substring(Constants.ONE);
        }
        return null;
    }

    /**
     * @Description Functions方法名转字段名
     * 虚拟机会自动给实现Serializable接口的lambda表达式生成 writeReplace()方法,
     * 我们可以通过反射的方式来手动调用writeReplace()方法，返回 SerializedLambda对象，
     * 然后再调用serializedLambda.getImplMethodName()得到表达式中的方法名
     */
    @SneakyThrows
    public static String toFieldName(Functions<?, ?> getter) {
        Method method = getter.getClass().getDeclaredMethod("writeReplace");
        //屏蔽Java语言的访问检查(public、private方法，默认都是开启访问检查)，使得对象的私有属性也可以被查询和设置
        //writeReplace是私有方法，需要去掉私有属性
        method.setAccessible(Boolean.TRUE);
        //手动调用writeReplace()方法，返回一个SerializedLambda对象
        SerializedLambda lambda = (SerializedLambda) method.invoke(getter);
        method.setAccessible(Boolean.FALSE);
        //得到lambda表达式中调用的方法名
        return toFieldName(lambda.getImplMethodName());
    }
}
