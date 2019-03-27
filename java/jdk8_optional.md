Optional
-

Optional是JDK1.8新增的，用于避免NullPointerException的工具类。

Optional提供以下几类方法，通过下面的方法，可以链式的处理对象，无需判断是否存在

- of 使用目标对象得到Optional对象
- present 判断对象是否为空
- filter 对对象进行筛选
- map 将对象映射得到其他Optional
- orElse、get 得到对象

### of

- public static <T> Optional<T> of(T value)

传入一个不为空的对象得到Optional，传入null会抛出异常

- public static <T> Optional<T> ofNullable(T value)

通过一个可为空的对象得到Optional

```java
public class Example {
    public static void main(String[] args){
        UserService userService = new UserServiceImpl();
    	Optional<User> o1 = Optional.of(userService.findById(1L));
    	Optional<User> o2 = Optional.ofNullable(userService.findById(0L));
    }
}
```

### present

- public boolean isPresent()

返回对象是否存在

- public void ifPresent(Consumer<? super T> consumer)

当对象存在时，执行操作

```java
public class Example {
    public static void main(String[] args){
        UserService userService = new UserServiceImpl();
        Optional<User> user = Optional.ofNullable(userService.findById(1L));
        if (user.isPresent()){
            // 判断存在后，进行操作。一般不这样做
        }
        user.isPresent(u->{
            // 当存在时执行的逻辑
        });
    }
}
```

### filter

- public Optional<T> filter(Predicate<? super T> predicate)

当对象存在时，对其进行筛选处理

```java
public class Example {
    public static void main(String[] args){
        UserService userService = new UserServiceImpl();
        Optional<User> user = Optional.ofNullable(userService.findById(1L));
        // 筛选用户姓名不为空
		user.filter(u->u.getName()!=null);
    }
}
```

### map

- public<U> Optional<U> map(Function<? super T, ? extends U> mapper)

将对象映射得到其他Optional对象

- public<U> Optional<U> flatMap(Function<? super T, Optional<U>> mapper)

将对象映射得到其他Optional对象，与map的区别是，这个方法需要自己包装Optional

```java
public class Example {
    public static void main(String[] args){
        UserService userService = new UserServiceImpl();
        Optional<User> user = Optional.ofNullable(userService.findById(1L));
        // 映射得到用户名的Optional对象
        user.map(u->user.getName());
		user.flatMap(u->Optional.ofNullable(u.getName()));
    }
}
```

### orElse get

- public T get()

得到对象，对象不存在会抛出异常，不常用

- public T orElse(T other)

若对象不存在则得到传入值

- public T orElseGet(Supplier<? extends T> other)

若对象不存在则执行传入表达式，并返回结果

- public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier)

若对象不存在则得到传入异常

```java
public class Example {
    public static void main(String[] args){
        UserService userService = new UserServiceImpl();
        Optional<User> user = Optional.ofNullable(userService.findById(1L));
        user.get();
        user.orElse(User.defaultUser());
        user.orElseGet(()->User.defaultUser());
        user.orElseThrow(()->new RuntimeException("user is null"));
    }
}
```