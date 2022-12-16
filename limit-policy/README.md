## 基于Nginx的限流

### **1.IP限流**

1.编写Controller

```java
@RestController  
@Slf4j  
public class Controller{  
    //nginx测试使用  
    @GetMapping("/nginx")  
    public String nginx(){  
        log.info("Nginx success");  
    }  
}  
```

2.修改host文件，添加一个网址域名

```
127.0.0.1   www.test.com  
```

3.修改 nginx，将步骤 2 中的域名，添加到路由规则当中

打开 nginx 的配置文件

```
vim /usr/local/nginx/conf/nginx.conf  
```

添加一个服务

```shell
#根据IP地址限制速度  
#1）$binary_remote_addr   binary_目的是缩写内存占用，remote_addr表示通过IP地址来限流  
#2）zone=iplimit:20m   iplimit是一块内存区域（记录访问频率信息），20m是指这块内存区域的大小  
#3）rate=1r/s  每秒放行1个请求  
limit_req_zone $binary_remote_addr zone=iplimit:20m rate=1r/s;  
  
server{  
    server_name www.test.com;  
    location /access-limit/ {  
        proxy_pass http://127.0.0.1:8080/;  
          
        #基于ip地址的限制  
        #1）zone=iplimit 引用limit_rep_zone中的zone变量  
        #2）burst=2  设置一个大小为2的缓冲区域，当大量请求到来，请求数量超过限流频率时，将其放入缓冲区域  
        #3）nodelay   缓冲区满了以后，直接返回503异常  
        limit_req zone=iplimit burst=2 nodelay;  
    }  
}  
```

4.访问地址，测试是否限流

> www.test.com/access-limit/nginx

### **2.多维度限流**

1.修改nginx配置

```shell
#根据IP地址限制速度  
limit_req_zone $binary_remote_addr zone=iplimit:20m rate=10r/s;  
#根据服务器级别做限流  
limit_req_zone $server_name zone=serverlimit:10m rate=1r/s;  
#根据ip地址的链接数量做限流  
limit_conn_zone $binary_remote_addr zone=perip:20m;  
#根据服务器的连接数做限流  
limit_conn_zone $server_name zone=perserver:20m;  
  
  
server{  
    server_name www.test.com;  
    location /access-limit/ {  
        proxy_pass http://127.0.0.1:8080/;  
          
        #基于ip地址的限制  
        limit_req zone=iplimit burst=2 nodelay;  
        #基于服务器级别做限流  
        limit_req zone=serverlimit burst=2 nodelay;  
        #基于ip地址的链接数量做限流  最多保持100个链接  
        limit_conn zone=perip 100;  
        #基于服务器的连接数做限流 最多保持100个链接  
        limit_conn zone=perserver 1;  
        #配置request的异常返回504（默认为503）  
        limit_req_status 504;  
        limit_conn_status 504;  
    }  
      
     location /download/ {  
        #前100m不限制速度  
        limit_rate_affer 100m;  
        #限制速度为256k  
        limit_rate 256k;  
     }  
}  
```

## 基于Redis+Lua的分布式限流

### **1.Lua脚本**

Lua是一个很小巧精致的语言，它的诞生（1993年）甚至比JDK 1.0还要早。Lua是由标准的C语言编写的，它的源码部分不过2万多行C代码，甚至一个完整的Lua解释器也就200k的大小。

Lua往大了说是一个新的编程语言，往小了说就是一个脚本语言。对于有编程经验的同学，拿到一个Lua脚本大体上就能把业务逻辑猜的八九不离十了。

Redis内置了Lua解释器，执行过程保证原子性

### **2.Lua安装**

安装Lua：

1.参考`http://www.lua.org/ftp/`教程，下载5.3.5_1版本，本地安装

如果你使用的是Mac，那建议用brew工具直接执行brew install lua就可以顺利安装，有关brew工具的安装可以参考`https://brew.sh/`网站，使用brew安装后的目录在`/usr/local/Cellar/lua/5.3.5_1`

2.安装IDEA插件，在IDEA->Preferences面板，Plugins，里面Browse repositories，在里面搜索lua，然后就选择同名插件lua。安装好后重启IDEA

3.配置Lua SDK的位置：`IDEA->File->Project Structure`,选择添加Lua，路径指向Lua SDK的bin文件夹，关于nginx面试资料，公众 号Java精选，回复java面试，领取面试资料。

4.都配置好之后，在项目中右键创建Module，左侧栏选择lua，点下一步，选择lua的sdk，下一步，输入lua项目名，完成

### **3.编写hello lua**

```lua
print 'Hello Lua'  
```

### **4.编写模拟限流**

```lua
-- 模拟限流  
  
-- 用作限流的key  
local key = 'my key'  
  
-- 限流的最大阈值  
local limit = 2  
  
-- 当前限流大小  
local currentLimit = 2  
  
-- 是否超过限流标准  
if currentLimit + 1 > limit then  
    print 'reject'  
    return false  
else  
    print 'accept'  
    return true  
end  
```

### **5.限流组件封装**

1.添加maven

```xml
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-data-redis</artifactId>  
</dependency>  
<dependency>  
    <groupId>org.springframework.boot</groupId>  
    <artifactId>spring-boot-starter-aop</artifactId>  
</dependency>  
<dependency>  
    <groupId>com.google.guava</groupId>  
    <artifactId>guava</artifactId>  
    <version>18.0</version>  
</dependency>  
```

2.添加Spring配置

不是重要内容就随便写点，主要就是把reids配置一下

```properties
server.port=8080  
  
spring.redis.database=0  
spring.redis.host=localhost  
spring.redis.port=6376  
```

3.编写限流脚本

lua脚本放在resource目录下就可以了

```lua
-- 获取方法签名特征  
local methodKey = KEYS[1]  
redis.log(redis.LOG_DEBUG,'key is',methodKey)  
  
-- 调用脚本传入的限流大小  
local limit = tonumber(ARGV[1])  
  
-- 获取当前流量大小  
local count = tonumber(redis.call('get',methodKey) or "0")  
  
--是否超出限流值  
if count + 1 >limit then  
    -- 拒绝访问  
    return false  
else  
    -- 没有超过阈值  
    -- 设置当前访问数量+1  
    redis.call('INCRBY',methodKey,1)  
    -- 设置过期时间  
    redis.call('EXPIRE',methodKey,1)  
    -- 放行  
    return true  
end  
```

4.使用`spring-data-redis`组件集成Lua和Redis

创建限流类

```java
@Service  
@Slf4j  
public class AccessLimiter{  
    @Autowired  
    private StringRedisTemplate stringRedisTemplate;  
    @Autowired  
    private RedisScript<Boolean> rateLimitLua;  
  
    public void limitAccess(String key,Integer limit){  
        boolean acquired = stringRedisTemplate.execute(  
            rateLimitLua,//lua脚本的真身  
            Lists.newArrayList(key),//lua脚本中的key列表  
            limit.toString()//lua脚本的value列表  
        );  
  
        if(!acquired){  
            log.error("Your access is blocked,key={}",key);  
            throw new RuntimeException("Your access is blocked");  
        }  
    }  
}  
```

创建配置类

```java
@Configuration  
public class RedisConfiguration{  
    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory factory){  
        return new StringRedisTemplate(factory);  
    }  
      
    public DefaultRedisScript loadRedisScript(){  
        DefaultRedisScript redisScript = new DefaultRedisScript();  
        redisScript.setLocation(new ClassPathResource("rateLimiter.lua"));  
        redisScript.setResultType(java.lang.Boolean.class);  
        return redisScript;  
    }  
}  
```

5.在Controller中添加测试方法验证限流效果

```java
@RestController  
@Slf4j  
public class Controller{  
    @Autowired  
    private AccessLimiter accessLimiter;  
      
    @GetMapping("test")  
    public String test(){
        accessLimiter.limitAccess("ratelimiter-test",1);  
        return "success";  
    }  
}   
```

### **6.编写限流注解**

1.新增注解

```java
@Target({ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface AccessLimiterAop{  
    int limit();  
      
    String methodKey() default "";  
}  
```

2.新增切面

```java
@Slf4j  
@Aspect  
@Component  
public class AccessLimiterAspect{  
    @Autowired  
    private AccessLimiter  accessLimiter;  
  
    //根据注解的位置，自己修改  
    @Pointcut("@annotation(com.gyx.demo.annotation.AccessLimiter)")  
    public void cut(){  
        log.info("cut");  
    }  
      
    @Before("cut()")  
    public void before(JoinPoint joinPoint){  
        //获取方法签名，作为methodkey  
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();  
        Method method = signature.getMethod();  
        AccessLimiterAop annotation = method.getAnnotation(AccessLimiterAop.class);  
          
        if(annotation == null){  
            return;  
        }  
        String key = annotation.methodKey();  
        Integer limit = annotation.limit();  
        //如果没有设置methodKey，就自动添加一个  
        if(StringUtils.isEmpty(key)){  
            Class[] type = method.getParameterType();  
            key = method.getName();  
            if (type != null){  
                String paramTypes=Arrays.stream(type)  
                    .map(Class::getName)  
                    .collect(Collectors.joining(","));  
                    key += "#"+paramTypes;  
            }  
        }  
          
        //调用redis  
        return accessLimiter.limitAccess(key,limit);  
    }  
}  
```

3.在Controller中添加测试方法验证限流效果

```java
@RestController  
@Slf4j  
public class Controller{  
    @Autowired  
    private AccessLimiter accessLimiter;  
      
    @GetMapping("test")  
    @AccessLImiterAop(limit =1)  
    public String test(){  
        return "success";  
    }  
} 
```

