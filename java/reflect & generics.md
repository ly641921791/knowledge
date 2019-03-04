## 反射 & 泛型

当反射遇到泛型，应该如何解析，例如下面的类，想要获得父类泛型的String

```java
public class StringList extends ArrayList<String> {
}
```

JDK1.5新增泛型特性的同时，Class类也新增了下面两个方法用于处理泛型

- public Type getGenericSuperclass() 获得父类类型
- public Type[] getGenericInterfaces() 获得接口类型数组

当父类或接口没有声明泛型，Type的真实类型是Class，否则Type的真实类型是ParameterizedType，结构如下

```java
public interface ParameterizedType extends Type {
    // 获得泛型类型，以StringList为例，返回[String.class]
	Type[] getActualTypeArguments();
	// 获得原始类型，以StringList为例，返回ArrayList.class
	Type getRawType();
	Type getOwnerType();
}
```

下面提供相关工具类，用于获得父类或接口的泛型类型，使用时需要判断`type instanceof Class`。

```java
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author ly
 */
public class ClassUtils {
    /**
     * 获得父类的泛型参数
     *
     * @param source 目标类
     * @return 父类泛型参数
     */
    public static Type[] getSuperclassGenericType(Class source) {
        Type type = source.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        return new Type[]{};
    }

    /**
     * 获得指定接口的泛型类型
     *
     * @param source        目标类
     * @param interfaceType 接口类型
     * @return 指定接口的泛型参数
     */
    public static Type[] getInterfacesGenericType(Class source, Class interfaceType) {
        Type[] types = source.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if (Objects.equals(interfaceType, parameterizedType.getRawType())) {
                    return parameterizedType.getActualTypeArguments();
                }
            }
        }
        return new Type[]{};
    }
}
```


