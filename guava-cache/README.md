# 工程简介

Guava Cache 是 Google Guava 库中的一个组件，它是一个高性能的内存缓存。

使用时，需要先构建一个 Cache 对象，然后调用它的 get 方法来获取缓存中的值。

如果在调用 get 方法时遇到空指针错误，可能是因为在缓存中没有为指定的键存储任何值。

在这种情况下，可以使用 Cache.getIfPresent 方法来避免空指针错误。

此方法会返回 null，而不会抛出异常，因此可以检查返回值是否为 null，然后决定是否执行后续操作。

另外，也可以在构建 Cache 对象时指定 CacheLoader，它会在缓存中没有对应键的值时自动加载所需的值。

这样，就可以确保在调用 get 方法时总能获取到非空的值。

在使用 Guava Cache 时，可以通过返回 Optional 对象来避免空指针问题。

例如，假设有以下代码：

```java
Copy code Cache<String, Object> cache = CacheBuilder.newBuilder().build();

Object getValue(String key) { 
    return cache.getIfPresent(key); 
} 
```

这段代码中，如果 getValue 方法在缓存中找不到与指定 key 对应的值，它将会返回
null。如果调用该方法的代码没有进行空指针检查，那么就会导致空指针异常。

为了避免这种情况，可以将 getValue 方法的返回值改为 Optional 对象，如下所示：

```java
Cache<String, Object> cache = CacheBuilder.newBuilder().build();

Optional<Object> getValue(String key) { 
    return Optional.ofNullable(cache.getIfPresent(key)); 
}
```

这样，如果调用 getValue 方法时在缓存中找不到与指定 key 对应的值，那么 getValue 方法将会返回一个空的 Optional 对象，而不是 null。调用该方法的代码可以通过调用 Optional 对象的 isPresent 方法来检查返回的
Optional 对象是否为空，如下所示：

```java
Optional<Object> value=getValue("someKey");
if (value.isPresent()) {
    // Do something with the value
    Object obj=value.get();
    // ...
}else{
    // Handle the case where the value is not present
    // ...
}
```

通过这种方式，可以避免空指针问题，并且能够更好地处理缓存中找不到指定 key 对应的值。
