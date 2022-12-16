# 工程简介

1、Update操作的幂等性
1）根据唯一业务号去更新数据

通过版本号的方式，来控制update的操作的幂等性，用户查询出要修改的数据，系统将数据返回给页面，将数据版本号放入隐藏域，用户修改数据，点击提交，将版本号一同提交给后台，后台使用版本号作为更新条件

```sql
update set version = version +1 ,xxx=${xxx} where id =xxx and version = ${version};
```

2、使用Token机制，保证update、insert操作的幂等性
1）没有唯一业务号的update与insert操作

进入到注册页时，后台统一生成Token， 返回前台隐藏域中，
用户在页面点击提交时，将Token一同传入后台，使用Token获取分布式锁，完成Insert操作，执行成功后，不释放锁，等待过期自动释放。

