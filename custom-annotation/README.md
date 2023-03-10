## 注解的定义

对于很多初次接触的开发者来说应该都有这个疑问。
Annontation 是 Java 5 开始引入的新特征，中文名称叫注解。它提供了一种安全的类似注释的机制，用来将任何的信息或元数据（metadata）与程序元素（类、方法、成员变量等）进行关联。为程序的元素（类、方法、成员变量）加上更直观更明了的说明，这些说明信息是与程序的业务逻辑无关，并且供指定的工具或框架使用。
Annontation 像一种修饰符一样，应用于包、类型、构造方法、方法、成员变量、参数及本地变量的声明语句中。
Java 注解是附加在代码中的一些元信息，用于一些工具在编译、运行时进行解析和使用，起到说明、配置的功能。注解不会也不能影响代码的实际逻辑，仅仅起到辅助性的作用，包含在 java.lang.annotation 包中。

## 注解的用处

1. 生成文档。这是最常见的，也是 Java 最早提供的注解。常用的有`@param`、`@return` 等。
2. 跟踪代码依赖性，实现替代配置文件功能。比如 Dagger 2 依赖注入，现在 Java 开发大量注解配置，具有很大用处。
3. 在编译时进行格式检查。如 `@override` 放在方法前，如果你这个方法并不是覆盖了超类方法，则编译时就能检查出。

## 注解的原理

注解本质是一个继承了 Annotation 的特殊接口，其具体实现类是 Java 运行时生成的动态代理类。而我们通过反射获取注解时，返回的是 Java 运行时生成的动态代理对象 `$Proxy1`。通过代理对象调用自定义注解（接口）的方法，会最终调用 `AnnotationInvocationHandler` 的 `invoke` 方法。该方法会从 `memberValues` 这个Map 中索引出对应的值，而 `memberValues` 的来源是 Java 常量池。

## 元注解

java.lang.annotation 提供了四种元注解，专门注解其他的注解（在自定义注解的时候，需要使用到元注解）：

- @Documented – 注解是否将包含在JavaDoc中
- @Retention – 什么时候使用该注解
- @Target – 注解用于什么地方
- @Inherited – 是否允许子类继承该注解

#### 1.@Retention – 定义该注解的生命周期

- RetentionPolicy.SOURCE：在编译阶段丢弃。这些注解在编译结束之后就不再有任何意义，所以它们不会写入字节码。@Override, @SuppressWarnings都属于这类注解。
- RetentionPolicy.CLASS：在类加载的时候丢弃。在字节码文件的处理中有用。注解默认使用这种方式
- RetentionPolicy.RUNTIME：始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息。我们自定义的注解通常使用这种方式。

#### 2.Target – 表示该注解用于什么地方。默认值为任何元素，表示该注解用于什么地方。可用的ElementType 参数包括

- ElementType.CONSTRUCTOR: 用于描述构造器
- ElementType.FIELD: 成员变量、对象、属性（包括enum实例）
- ElementType.LOCAL_VARIABLE: 用于描述局部变量
- ElementType.METHOD: 用于描述方法
- ElementType.PACKAGE: 用于描述包
- ElementType.PARAMETER: 用于描述参数
- ElementType.TYPE: 用于描述类、接口(包括注解类型) 或enum声明

#### 3.@Documented – 一个简单的Annotations 标记注解，表示是否将注解信息添加在java 文档中。

#### 4.@Inherited – 定义该注释和子类的关系

@Inherited 元注解是一个标记注解，@Inherited 阐述了某个被标注的类型是被继承的。如果一个使用了@Inherited 修饰的 annotation 类型被用于一个 class，则这个 annotation 将被用于该 class 的子类。

## 常见标准的Annotation

#### 1.Override

java.lang.Override 是一个标记类型注解，它被用作标注方法。它说明了被标注的方法重载了父类的方法，起到了断言的作用。如果我们使用了这种注解在一个没有覆盖父类方法的方法时，编译器将以一个编译错误来警示。

#### 2.Deprecated

Deprecated 也是一种标记类型注解。当一个类型或者类型成员使用 @Deprecated 修饰的话，编译器将不鼓励使用这个被标注的程序元素。所以使用这种修饰具有一定的“延续性”：如果我们在代码中通过继承或者覆盖的方式使用了这个过时的类型或者成员，虽然继承或者覆盖后的类型或者成员并不是被声明为 @Deprecated，但编译器仍然要报警。

#### 3.SuppressWarnings

SuppressWarning 不是一个标记类型注解。它有一个类型为 String[] 的成员，这个成员的值为被禁止的警告名。对于 javac 编译器来讲，被-Xlint 选项有效的警告名也同样对@SuppressWarings 有效，同时编译器忽略掉无法识别的警告名。

```text
@SuppressWarnings("unchecked")
```

## 自定义注解

#### 自定义注解类编写的一些规则:

1. Annotation 型定义为 @interface，所有的 Annotation 会自动继承 java.lang.Annotation 这一接口，并且不能再去继承别的类或是接口。
2. 参数成员只能用 public 或默认(default) 这两个访问权修饰。
3. 参数成员只能用基本类型 byte、short、char、int、long、float、double、boolean 八种基本数据类型和 String、Enum、Class、annotations 等数据类型，以及这一些类型的数组。
4. 要获取类方法和字段的注解信息，必须通过 Java 的反射技术来获取  Annotation 对象，除此之外没有别的获取注解对象的方法。
5. 注解也可以没有定义成员，不过这样单纯的注解不起作用。自定义注解需要使用到元注解。

## 自定义注解实例

FruitName：

```java
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FruitName {

    /**
     * 值，名称
     *
     * @return {@link String}
     */
    String value() default "";

}
```

FruitClolor：

```java
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FruitColor {

    /**
     * 颜色属性
     */
    Color fruitColor() default Color.GREEN;

    /**
     * 颜色枚举
     */
    public enum Color {
        /**
         * 蓝色
         */
        BLUE,
        /**
         * 红色
         */
        RED,
        /**
         * 绿色
         */
        GREEN,
    }

}
```

FruitSupplier：

```java
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FruitSupplier {

    /**
     * 供应商编号
     */
    public int id() default -1;

    /**
     * 供应商名称
     */
    public String name() default "";

    /**
     * 供应商地址
     */
    public String address() default "";

}
```

定义工具类，处理注解：

```java
public class FruitUtil {

    public static void getFruitInfo(Class<?> clazz) {

        String strFruitName = " 水果名称：";
        String strFruitColor = " 水果颜色：";
        String strFruitSupplier = "供应商信息：";

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(FruitName.class)) {
                FruitName fruitName = field.getAnnotation(FruitName.class);
                strFruitName = strFruitName + fruitName.value();
                System.out.println(strFruitName);
            } else if (field.isAnnotationPresent(FruitColor.class)) {
                FruitColor fruitColor = field.getAnnotation(FruitColor.class);
                strFruitColor = strFruitColor + fruitColor.fruitColor().toString();
                System.out.println(strFruitColor);
            } else if (field.isAnnotationPresent(FruitSupplier.class)) {
                FruitSupplier fruitSupplier = field.getAnnotation(FruitSupplier.class);
                strFruitSupplier = " 供应商编号：" + fruitSupplier.id() + " 供应商名称：" + fruitSupplier.name() + " 供应商地址：" + fruitSupplier.address();
                System.out.println(strFruitSupplier);
            }
        }
    }

}
```

定义实体类，使用注解：

```java
public class Apple {

    @FruitName("Apple")
    private String appleName;

    @FruitColor(fruitColor = FruitColor.Color.RED)
    private String appleColor;

    @FruitSupplier(id = 1, name = "陕西红富士集团", address = "陕西省西安市延安路89号红富士大厦")
    private String appleProvider;

    public void setAppleColor(String appleColor) {
        this.appleColor = appleColor;
    }

    public String getAppleColor() {
        return appleColor;
    }

    public void setAppleName(String appleName) {
        this.appleName = appleName;
    }

    public String getAppleName() {
        return appleName;
    }

    public void setAppleProvider(String appleProvider) {
        this.appleProvider = appleProvider;
    }

    public String getAppleProvider() {
        return appleProvider;
    }

    public void displayName() {
        System.out.println("水果的名字是：苹果");
    }

}
```

定义测试类，使用注解：

```java
public class Test {

    public static void main(String[] args) {
        FruitUtil.getFruitInfo(Apple.class);
    }

}
```

运行之后，输出结果为：

>  水果名称：Apple
>  水果颜色：RED
>  供应商编号：1 供应商名称：陕西红富士集团 供应商地址：陕西省西安市延安路89号红富士大厦

## 参考链接

> [https://blog.csdn.net/qq_14958051/article/details/117971003](https://blog.csdn.net/qq_14958051/article/details/117971003)

