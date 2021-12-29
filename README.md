---
title: B2C电商实践
date: 10/15/2021
tags: SpringCloudAlibaba
categories: SpringCloudAlibaba
keywords: 
description: SpringCloudAlibaba
top_img: https://cdn.jsdelivr.net/gh/Raptor1998/imghouse/untidy/20210401142650.jpg
comments:  是否顯示評論（除非設置false,可以不寫）
cover: https://cdn.jsdelivr.net/gh/Raptor1998/imghouse/untidy/20210401142650.jpg
toc:  是否顯示toc （除非特定文章設置，可以不寫）
toc_number: 是否顯示toc數字 （除非特定文章設置，可以不寫）
copyright: 是否顯示版權 （除非特定文章設置，可以不寫）
---

# B2C

尚硅谷谷粒商城的demo及部分解决方法笔记，**不是完整的代码**。

[夏沫止水/gulimall](https://gitee.com/agoni_no/gulimall?_from=gitee_search)

# Nacos

## Nacos集群启动问题

### 数据库配置(bin/)

`将nacos-mysql.sql执行`

### 配置文件(conf/)

```yml
###################最后追加###################
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://127.0.0.1:3306/cloud?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=Asia/Shanghai
db.user=root
db.password=****
```

### 配置cluster.conf

**这个IP不能写127.0.0.1,必须是Linux命令hostname -i能够识别的IP**

```shell
cp cluster.conf.example cluster.conf

# 配置集群信息
vim cluster.conf

182.154.146.46:3333
182.154.146.46:4444
182.154.146.46:5555

```

### 启动脚本(bin/)

![](https://cdn.jsdelivr.net/gh/Raptor1998/imghouse/untidy/20211017175846.png)

![](https://cdn.jsdelivr.net/gh/Raptor1998/imghouse/untidy/20211017175847.png)

### 启动方式

**./startup.sh -p 3333**

### 内存不足启动解决

```shell
standalone：表示单机模式运行，非集群模式
-Xms: 设定程序启动时占用内存大小
-Xmx: 设定程序运行期间最大可占用的内存大小
-Xmn：新生代大小
```

**JAVA_OPT="${JAVA_OPT} -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"**

更改为，比如，可根据机器调整

**JAVA_OPT="${JAVA_OPT} -server -Xms300m -Xmx300m -Xmn100m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=160m"**

### nginx负载均衡

```shell
upstream cluster{                                                        
    server 127.0.0.1:3333;
    server 127.0.0.1:4444;
    server 127.0.0.1:5555;
}


server{                       
    listen 1111;
    server_name localhost;
    location /{
         proxy_pass http://cluster;                                               
    }

```

# renren-fast oss 报错问题

**依赖改为继承自root项目**

# idea热部署不生效

+ **File-Settings-Compiler-Build project automatically**

+ **ctrl + shift + alt + /,选择Registry,勾上 Compiler autoMake allow when app running**

# JSR303

## 分组校验

```java
1. @NotBlank(message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
   给校验直接添加分组
2. @Validated({UpdateGroup.class}) 
   标注校验的分组
   默认没有指定的分组的注解，分组校验的情况下不会生效
```

## 自定义校验

```java
1. 编写自定义校验注解
2. 编写自定义校验器
@Documented
@Constraint(validatedBy = {ListValueConstrainValidation.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListValue {
    String message() default "{com.raptor.common.valid.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] vals() default {};
}

public class ListValueConstrainValidation implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();

    @Override
    public void initialize(ListValue constraintAnnotation) {

        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            set.add(val);
        }

    }
    //判断是否校验成功
    @Override
    public boolean isValid(Integer s, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(s);
    }
}
3. 编写自定义的返回信息 ValidationMessages.properties
com.raptor.common.valid.ListValue.message=必须是指定的值
4. 关联自定义的校验器和自定义的注解
@Constraint(validatedBy = {ListValueConstrainValidation.class})
```

# elasticsearch

## docker安装elasticsearch

```shell
1. 下载镜像
docker pull elasticsearch:7.6.2
2. 创建一个挂载目录
mkdir -p /mydata/elasticsearch/config
mkdir -p /mydata/elasticsearch/data
echo "http.host: 0.0.0.0" >> /mydata/elasticsearch/config/elasticsearch.yml
3. 启动
docker run --name elasticsearch -p 9200:9200 -p 9300:9300  -e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms64m -Xmx128m" -v /mydata/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /mydata/elasticsearch/data:/usr/share/elasticsearch/data -v /mydata/elasticsearch/plugins:/usr/share/elasticsearch/plugins -d elasticsearch:7.6.2

其中elasticsearch.yml是挂载的配置文件，data是挂载的数据，plugins是es的插件

4. 权限问题
docker logs elasticsearch 查看日志 发现报无权限错误
"Caused by: java.nio.file.AccessDeniedException: /usr/share/elasticsearch/data/nodes"
添加权限
chmod -R 777 /mydata/elasticsearch/

-e "discovery.type=single-node" 设置为单节点
-e ES_JAVA_OPTS="-Xms256m -Xmx256m" \ 测试环境下，设置ES的初始内存和最大内存，否则导致过大启动不了ES


5. 访问http://your ipadress:9200/   说明启动成功
{
  "name" : "26e50cad52d5",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "RrKY3rRZRW2nh7Ap-rIjvQ",
  "version" : {
    "number" : "7.6.2",
    "build_flavor" : "default",
    "build_type" : "docker",
    "build_hash" : "ef48eb35cf30adf4db14086e8aabd07ef6fb113f",
    "build_date" : "2020-03-26T06:34:37.794943Z",
    "build_snapshot" : false,
    "lucene_version" : "8.4.0",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```

## docker安装Kibana

```shell
docker pull kibana:7.6.2

docker run --name kibana -e ELASTICSEARCH_HOSTS=http://your ipaddress:9200 -p 5601:5601 -d kibana:7.6.2
```

# nginx转发到网关时请求的host丢失

xxx

# 缓存

## 缓存溢出问题

```java
//TODO 性能优化  改为一次查询数据库
// 堆外内存溢出  springboot 2.0默认使用lettuce操作redis客户端  使用netty进行网络通信
// lettuce的bug导致netty堆外内存溢出   -Xmx300m  netty如果乜有指定堆外内存 默认使用 -Xmx300m 不能使用 可以通过 -Dio.netty.maxDirectMeory去调到堆外内存
// 解决方案：升级lettuce客户端    切换使用jedis
```

## 缓存穿透

只查询一个一定不存在的数据，由于缓存是不命中，将去查询数据库，数据库无此记录，没有将null写到缓存，导致每次请求都要取存储层查询。

可以利用不存在的数据进行攻击，数据库压力增大，最终导致崩溃

解决方案：null结果缓存，并加入短暂过期时间

## 缓存雪崩

值设置缓存是key采用了相同的过期时间，导致缓存在某一时刻同时失效，请求全部转发到db，cb压力过大雪崩

原有的时效基础上增加一个随机值，比如1-5分钟随机，这样每个缓存的过期时间重复率就会降低，很难引发集体失效时间。

## 缓存击穿

对于一些设置了过期时间的key，如果这些key可能会在某些时间点被炒高并发访问，是一种热点数据，如果这个key在大量请求同时进来前刚好失效，所有对这个key 的查询都落到db

解决：加锁，大量并发只让一个人去查，其他人等待，查到以后释放锁，其他人获取到锁，先查缓存，就会有数据。

## redis的分布式锁

```java
//占分布式锁
String uuid = UUID.randomUUID().toString();
Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid,300,TimeUnit.SECONDS);
if (lock) {
    System.out.println("获取分布式锁成功");
    Map<String, List<Catelog2Vo>> dataFromDb = null;
    try {
        //加锁成功
        dataFromDb = getDataFromDb();
    } finally {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        //删锁
        stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);

    }
    //只删除自己的锁
    //获取值对比，对比成功删除=原子性 lua脚本解锁
    //可能会出现删除其他线程锁的情况   在业务执行尾，获取到自己的数据后，传输过程中锁过期，执行删除锁命令，将其他线程锁删除
    // String lockValue = stringRedisTemplate.opsForValue().get("lock");
    // if (uuid.equals(lockValue)) {
    //     //只能删除我自己的锁
    //     stringRedisTemplate.delete("lock");
    // }

    return dataFromDb;
} else {
    System.out.println("获取分布式锁失败");
    //加锁失败
    try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
    //自旋
    return getCatalogJsonFromDbWithRedisLock();     
}
```

## 缓存一致性问题

### 双写模式（更新完数据更新缓存）

![双写模式](https://cdn.jsdelivr.net/gh/Raptor1998/imghouse/untidy/20211104171156.png)

### 失效模式（更新完数据缓存失效）

![失效模式](https://cdn.jsdelivr.net/gh/Raptor1998/imghouse/untidy/20211104171157.png)

### 解决

![缓存一致性解决方案](https://cdn.jsdelivr.net/gh/Raptor1998/imghouse/untidy/20211104171350.png)

# SpringCache

xxx

# Session共享问题

![image-20211119191231370](E:\material\md\image-20211119191231370.png)

![image-20211119191430478](E:\material\md\image-20211119191430478.png)

![image-20211119191131013](E:\material\md\image-20211119191131013.png)

![image-20211119191741406](E:\material\md\image-20211119191741406.png)

# 异步&线程池

```java
/**
 * 集成Thread
 * 实现Runnable接口
 * 实现Callable接口+futureTask
 * 线程池
 */
//Thread01 thread01 = new Thread01();
//thread01.start();

//Runnable01 runnable01 = new Runnable01();
//new Thread(runnable01).start();


//FutureTask<Integer> futureTask = new FutureTask<Integer>(new Callable01());
//new Thread(futureTask).start();
////阻塞等待
//Integer integer = futureTask.get();
//System.out.println(integer);

//应该将所有的异步任务都交给线程池执行

/**
 * int corePoolSize,  核心线程数，线程池创建号以后就准备就绪的线程数量，就等待接受异步任务去执行
 * 只要线程池不销毁，就一直存在   除非奢姿了这个allowCoreThreadTimeOut
 * int maximumPoolSize,  最大的线程数量  控制资源并发
 * long keepAliveTime,  存活时间，当正在运行线程数量大于核心数量，回释放空闲的线程，
 * 只要线程空闲大于执行的存活时间，释放的线程等于maximumPoolSize-corePoolSize
 * TimeUnit unit, 时间单位
 * BlockingQueue<Runnable> workQueue, 阻塞队列 如果任务很多，将多余的任务放在队列里，有现成空闲就回去队列里取新的任务
 * ThreadFactory threadFactory,  线程创建工厂，
 * RejectedExecutionHandler handler   如果队列满了，按照指定的拒绝策略，拒绝执行任务
 *
 *
 *
 * 工作顺序：
 * 1. 线程池创建号，准备好core的核心线程数量，准备接受任务
 * 2. 新的任务进来，用core准备好的空闲线程去执行
 *  （1）core满了，将再进来的任务放到阻塞队列中，空闲的core就会自己去阻塞队列获取任务
 *  （2）阻塞队列满了，就直接开新的线程执行，最大只能开到max指定的数量
 *  （3）max都执行好了，Max-core数量空闲的线程会在keepAliveTime指定的时间后自动销毁，最终保持到core大小
 *  （4）如果线程开到了max的数量，还有新任务进来，就会使用reject指定的拒绝策略进行处理
 * 3.所有的线程创建都是由指定的factory创建的
 *
 *  一个线程池 core 7，max 20 ，queue 50   100并发进来怎么分配
 *  先执行7个，后续50个请求进队列，然后创建13个新线程执行，
 *  现在执行了 7 + 13 个  50个在队列中   剩下30执行拒绝策略
 *
 *
 *  new LinkedBlockingDeque<>()  默认integer的最大值
 */
ThreadPoolExecutor executor = new ThreadPoolExecutor(7,
                20,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(50),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        for (int i = 0; i < 100; i++) {
            executor.execute(new Thread(new Runnable() {
                @Override
                public void run() {

                    System.out.println("当前执行的线程是：" + Thread.currentThread().getName());
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, String.valueOf(i)));
        }

        System.out.println("main....end");
        executor.shutdown();
```

# 接口的幂等性

接口幂等性就是用户对于同一操作发起的一次请求或者多次请求的结果是一致的。

+ token机制

  1. 服务端提供了发送token接口。那些业务存在幂等问题，就必须在这之前执行，先去获取token，服务器把token保存到redis
  2. 然后调用业务接口请求时，把token携带过去，一般放在请求头部
  3. 服务器判断是否存在redis中，存在表示第一次请求，然后删除token，继续执行业务
  4. 如果判断token不存在redis中，则表示是重复操作，直接返回重读标记给client

  危险性：

  + 先删除还是后删除token

    + 先删除可能导致，业务确实没有执行，重试还带上之前的token，由于防虫设计导致，接口还是不能执行

      即分布式系统中两次请求同时从redis中取得token，服务端均校验通过，同时执行了业务

    + 后删可能导致，业务处理成功，但是服务闪断，出现超时，没有删除token，继续重试，导致业务被执行两次+

    + 最好设计为先删除token，如果业务嗲用失败，就重新获取token在此请求

  + token获取、比较和删除必须是原子性

    + redis.get(token)、token.equals、redis.del(token)如果这两个操作不是院子，可能导致，高并发下，都得到同样的token，判断都成功，继续业务并发执行

    + 可以再redis使用lua脚本完成这个操作

      if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end

+ 锁机制

  + 数据库悲观锁

    悲观锁使用时一般伴随事务一起使用,数据锁定时间可能会很长，需要根据实际情况选用。另外要注意的是，id字段一定是主键或者唯一索引不然可能造成锁表的结息，处理起来会非常麻烦。

  + 数据库乐观锁

    这种方法适合在更新的场景中,

    updatet_goods set count = count -1 , version = version + 1 where good_id=2 and version= 1根据version 版本，也就是在操作库存前先获取当前商品的version版本号，然后操作的时候带上此version号。我们梳理下，我们第一次操作库存时，得到version为1，调用库存服务version 变成了2;但返回给订单服务出现了问题，订单服务又一次发起调用库存服务，当订单服务传如的version还是1，再执行上面的sql语句时，就不会执行:因为version已经变为2了，where条件就不成立。这样就保证了不管调用几次，只会真正的处理一次。

    乐观锁主要使用于处理读多写少的问题

  + 业务层分布式锁

    如果多个机器可能在同一时间同时处理相同的数据,比如多台机器定时任务都拿到了相同数据处理，我们就可以加分布式锁，锁定此数据，处理完成后释放锁。获取到锁的必须先判断这个数据是否被处理过。

+ 各种唯一约束

  + 数据库唯一约束

    插入数据,应该按照唯一索引进行插入，比如订单号,相同的订单就不可能有两条记录插入。我们在数据库层面防止重复。
    这个机制是利用了数据库的主键唯一约束的特性，解决了在insert场景时幂等问题。但主键的要求不是自增的主键,这样就需要业务生成全局唯一的主键。
    如果是分库分表场景下，路由规则要保证相同请求下,落地在同一个数据库和同一表中，要不然数据库主键约束就不起效果了，因为是不同的数据库和表主键不相关。

  + redis set 防重

    很多数据需要处理，只能被处理一次,比如我们可以计算数据的 MD5将其放入redis 的 set,每次处理数据,先看这个MD5是否已经存在,存在就不处理。

+ 防重表
    使用订单号 orderNo做为去重表的唯一索引,把唯一索引插入去重表,再进行业务操作，且他们在同一个事务中。这个保证了重复请求时，因为去重表有唯一约束，导致请求失败,避免了幂等问题。这里要注意的是，去重表和业务表应该在同一库中，这样就保证了在同一个事务，即使业务操作失败了，也会把去重表的数据回滚。这个很好的保证了数据一致性。

+ 全局请求唯一id
  调用接口时,生成一个唯一id,redis 将数据保存到集合中（去重)，存在即处理过。可以使用 nginx设置每一个请求的唯一 id;

  proxy_set_header X-Request-ld Srequest_id;

# 本地事务与分布式事务

事务保证：

1. 订单服务异常，库存锁定不运行，全部回滚，撤销操作

2. 库存服务事务自制，锁定失败全部回滚，订单感受到继续回滚

3. 库存服务锁定成功，但是网络原因返回数据途中问题

   远程服务调用失败，远程服务成功，却由于网络故障等原因，没有返回，导致订单回滚，库存却扣减

4. 库存服务锁定成功，库存服务下面的逻辑发生故障，订单回滚

   远程服务执行完成，当前订单服务中调用远程服务的其他地方却发生故障，已执行的远程服务无法回滚

利用消息队列实现最终一致，库存服务锁定成功后给消息队列消息，过段时间自动解锁，甲所示先查询订单的支付状态，解锁成功修改库存工作单详情状态为已解锁

## 事务的基本特质

数据库事务的ACID特性：原子性（Atomicity）、已执行（Consistency）、隔离性或独立性（Isolation）、持久性（Durability）

+ 原子性：一系列的操作整体不可拆分，要么同时成功，要么同时失败

+ 一致性：数据在事务的前后，业务整体一致

  A（1000）给B（1000）转账200，事务成功则A（800）、B(1200)

+ 隔离性：事务之间相互隔离

+ 持久性：一旦事务成功，数据一定会落在数据库

在单体应用中，多个业务操作使用同一条连接操作不同的数据表，一旦出现异常，很同意进行整体回滚。一个事务开始：代表一笑的所有操作都在一个连接里面。

## 事务的隔离级别

1、脏读：事务A读取了事务B更新的数据，然后B回滚操作，那么A读取到的数据是脏数据

2、不可重复读：事务 A 多次读取同一数据，事务 B 在事务A多次读取的过程中，对数据作了更新并提交，导致事务A多次读取同一数据时，结果 不一致。

3、幻读：系统管理员A将数据库中所有学生的成绩从具体分数改为ABCDE等级，但是系统管理员B就在这个时候插入了一条具体分数的记录，当系统管理员A改结束后发现还有一条记录没有改过来，就好像发生了幻觉一样，这就叫幻读。

| 事务隔离级别                 | 脏读 | 不可重复读 | 幻读 |
| ---------------------------- | ---- | ---------- | ---- |
| 读未提交（read-uncommitted） | 是   | 是         | 是   |
| 不可重复读（read-committed） | 否   | 是         | 是   |
| 可重复读（repeatable-read）  | 否   | 否         | 是   |
| 串行化（serializable）       | 否   | 否         | 否   |

## 事务的传播行为

1、PROPAGATION_REQUIRED:如果当前没有事务，就创建一个新事务，如果当前存在事务，就加入该事务，该设置是最常用的设置。
2、PROPAGATION_SUPPORTS:支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行。
3、PROPAGATION_MANDATORY:支持当前事务，如果当前存在事务，就加入该事务，如果当前不存在事务，就抛出异常。
4、PROPAGATION_REQUIRES_NEW:创建新事务，无论当前存不存在事务，都创建新事务。5、PROPAGATION_NOT_SUPPORTED:以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
6、PROPAGATION_NEVER:以非事务方式执行，如果当前存在事务，则抛出异常。
7、PROPAGATION_NESTED:如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。

### Springboot中事务传播的坑

**同一个对象内事务方法互相调用默认失效，同一个类下的方法相互调用，即使设置了事务的隔离界别注解，依然以第一个执行的方法的事务为准。**

**原因：绕过了代理对象事务使用代理对象来执行的**。

解决：使用代理对象来调用方法

```java
引入aspectj
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

```java
开启aspectj动态代理功能，所有的动态代理都是aspectj创建的（即使没有接口也可以创建动态代理）
    exposeProxy = true 对外暴露代理对象
@EnableAspectJAutoProxy(exposeProxy = true)
    
从aopcontext中获取当前代理对象实现事务的传播
```

# 分布式事务

## CAP定理

CAP原则指的实在一个分布式系统中：

+ 一致性（Consistency）

  在分布式系统中的所有数据备份，在同一时刻是否同样的值，等同于所有接待您访问同一份最新的数据副本

+ 可用性（Availablity）

  在集群中一部分节点故障后，集群整体是否还能影响客户端的读写请求。（对数据更新具备更高的可用性）

+ 分区容错性（Partition tolerance）

  大多数人分布式系统都分布在多个子网络，每个子网络叫做一个区，分区容错的意思就是，区间通信可能失败，。比如，一台服务器放在中国，另一台服务器放在美国，这就是两个区，他们之间可能无法通信。

CAP原则指的是，这三个要素最多只能同时实现两点，不能三者兼顾。

**分布式系统中实现一致性的raft算法、paxos**

**[http://thesecretlivesofdata.com/raft/](http://thesecretlivesofdata.com/raft/)**

## BASE理论

是对CAP的延伸，思想史计时无法做到强一致性（CAP的一致性就是强一致性），也可以适当的采取弱一致性，即最终一致性。

+ 基本可用（Basically Available）

  基本可用是指分布式系统在出现故障的时候,允许损失部分可用性(例如响应时间、功能上的可用性)，允许损失部分可用性。需要注意的是，基本可用绝不等价于系统不可用。

  响应时间上的损失:正常情况下搜索引擎需要在0.5秒之内返回给用户相应的查询结果，但由于出现故障（比如系统部分机房发生断电或断网故障)，查询结果的响应时间增加到了1~2秒。

  功能上的损失:购物网站在购物高峰(如双十一)时，为了保护系统的稳定性，部分消费者可能会被引导到一个降级页面。

+ 软状态（Soft State）

  软状态是指允许系统存在中间状态，而该中间状态不会影响系统整体可用性。分布式存储中一般一份数据会有多个副本，允许不同副本同步的延时就是软状态的体现。mysql replication的异步复制也是一种体现。

+ 最终一致性（Eventual Consistency）

  最终一致性是指系统中的所有数据副本经过一定时间后，最终能够达到一致的状态。弱一致性和强一致性相反，最终一致性是弱一致性的一种特殊情况。

## 强一致性、弱一致性、最终一致性

从客户端角度，多进程并发访间时，更新过的数据在不同进程如何获取的不同策略，决定了不同的一致性。对于关系型数据库，要求更新过的数据能被后续的访问都能看到，这是强一致性。如果能容忍后续的部分或者全部访问不到，则是弱一致性。如果经过一段时间后要求能访问到更新后的数据，则是最终一致性

## 分布式事务的几种方案

[分布式事务的解决方案](https://zhuanlan.zhihu.com/p/183753774)

柔性事务+可靠消息+最终一致性方案

业务处理服务在业务事务提交之前，向实时消息.服务请求发送消息，实时消息服务只记录消息数据，而不是真正的发送。业务处理服务在业务事务提交之后:向实时消息服务确认发送。只有在得到确认发送指令后，实时消息服务才会真正发送。

# seata

[Springcloud基础](https://github.com/Raptor1998/SpringCloud-Basic)

[Springcloud笔记文档](https://github.com/Raptor1998/SpringCloud-Basic/blob/master/SpringCloud%20%E7%AC%AC%E4%BA%8C%E5%AD%A32020.03.05.mmap)

# rabbit延迟队列释放库存

![image-20211208190908469](E:\material\md\image-20211208190908469.png)

```java
/**
 * 死信队列
 *
 * @return
 */@Bean
public Queue orderDelayQueue() {
    /*
        Queue(String name,  队列名字
        boolean durable,  是否持久化
        boolean exclusive,  是否排他
        boolean autoDelete, 是否自动删除
        Map<String, Object> arguments) 属性
     */
    HashMap<String, Object> arguments = new HashMap<>();
    arguments.put("x-dead-letter-exchange", "order-event-exchange");
    arguments.put("x-dead-letter-routing-key", "order.release.order");
    arguments.put("x-message-ttl", 60000); // 消息过期时间 1分钟
    Queue queue = new Queue("order.delay.queue", true, false, false, arguments);

    return queue;
}

/**
 * 普通队列
 *
 * @return
 */
@Bean
public Queue orderReleaseQueue() {

    Queue queue = new Queue("order.release.order.queue", true, false, false);

    return queue;
}

/**
 * TopicExchange
 *
 * @return
 */
@Bean
public Exchange orderEventExchange() {
    /*
     *   String name,
     *   boolean durable,
     *   boolean autoDelete,
     *   Map<String, Object> arguments
     * */
    return new TopicExchange("order-event-exchange", true, false);

}


@Bean
public Binding orderCreateBinding() {
    /*
     * String destination, 目的地（队列名或者交换机名字）
     * DestinationType destinationType, 目的地类型（Queue、Exhcange）
     * String exchange,
     * String routingKey,
     * Map<String, Object> arguments
     * */
    return new Binding("order.delay.queue",
            Binding.DestinationType.QUEUE,
            "order-event-exchange",
            "order.create.order",
            null);
}

@Bean
public Binding orderReleaseBinding() {

    return new Binding("order.release.order.queue",
            Binding.DestinationType.QUEUE,
            "order-event-exchange",
            "order.release.order",
            null);
}
```

# 消息的可靠性

## 消息丢失

+ 消息发送出去，由于网络问题没有抵达服务器

  + 做好容错方法(try-catch)，发送消息可能会网络失败，失败后要有重试机制，可记录到数据库，采用定期扫描重发的方式

  + 做好日志记录，每个消息状态是否都被服务器收到都应该记录

  + 做好定期重发，如果消息没有发送成功，定期去数据库扫描未成功的消息进行重发

+ 消息抵达Broker，Broker要将消息写入磁盘(持久化)才算成功。此时Broker尚未持久化完成，宕机.
  + publisher也必须加入确认回调机制，确认成功的消息，修改数据库消息状态。

+ 自动ACK的状态下。消费者收到消息，但没来得及消息然后宕机
  + 一定开启手动ACK，消费成功才移除，失败或者没来得及处理就noAck并重新入队

## 消息重复

+ 消息消费成功，事务已经提交，ack时，机器宕机。导致没有ack成功，Broker的消息重新由unack变为ready，并发送给其他消费者

+ 消息消费失败，由于重试机制，自动又将消息发送出去
+ 成功消费，ack时宕机，消息由unack变为ready，Broker又重新发送
  + 消费者的业务消费接口应该设计为幂等性的。比如扣库存有工作单的状态标志
  + 使用防重表（redis/mysql)，发送消息每一个都有业务的唯一标识，处理过就不用处理
  + rabbitMQ的每一个消息都有redelivered字段，可以获取是否是被重新投递过来的，而不是第一次投递过来的

## 消息积压

+ 消费者宕机积压
+ 消费者消费能力不足宕机
+ 发送者发送流量太大
  + 上线更多的消费者，进行正常消费
  + 上线专门的队列消费服务，将消息先批量的取出来，记录数据库，离线慢慢处理

# 收单

1. 订单在支付页，不支付，一直刷新，订单过期了才支付，订单状态改为已经支付，但是库存已经释放了

   可使用支付宝的自动收单功能，只要一段时间不支付，就不能支付了

2. 由于时延等问题 ，订单解锁完成，正在等待锁库存的时候，异步通知才到

   订单解锁，手动调用收单

3. 网络阻塞问题，订单支付成功的异步通知一直不到达

   查询订单列表时，ajax获取当前未支付的订单状态，查询订单状态时，再获取一下支付宝此订单的状态

4. 其他各种问题

   每天晚上闲时下载支付宝对账单，——进行对账

# 秒杀

秒杀具有瞬间高并发的特点，针对这一特点，必须要做限流＋异步＋缓存（页面静态化)＋独立部署。

**限流方式:**

1. 前端限流，一些高并发的网站直接在前端页面开始限流，例如:小米的验证码设计

2. nginx限流，直接负载部分请求到错误的静态页面:令牌算法漏斗算法

3. 网关限流，限流的过滤器

4. 代码中使用分布式信号量

## Spring定时任务

```java
@Component
@EnableScheduling
//开启异步任务
@EnableAsync
@Slf4j
public class HelloSchedule {
    /**
     * 1. Spring的corn由6位组成，不予许第七位的年
     * 2. 在周几的位置，1-7代表周一到周日，MON-SUN
     * 3. 定时任务不应该阻塞。默认是阻塞的
     *  1） 可以让业务运行以异步的方式，提交到自己的线程池
     *        CompletableFuture.runAsync(()->{
     *         }.executor);
     *  2) 支持异步线程池；设置spring.task.schedule.pool.size=5
     *  3) 异步任务  @EnableAsync
     *
     */
    @Async
    @Scheduled(cron = "* * * ? * 4")
    public void hello() throws InterruptedException {
        log.info("hello ...");
        Thread.sleep(3000);
    }

}
```

## 商品上架的幂等性保证

在分布式环境中，多个应用的上架时间都一样，只需要保证有一台机器执行即可。

```java
@Autowired
private RedissonClient redissonClient;

//秒杀商品上架功能的锁
private final String upload_lock = "seckill:upload:lock";


//TODO 保证幂等性问题
// @Scheduled(cron = "*/5 * * * * ? ")
@Scheduled(cron = "0 0 1/1 * * ? ")
public void uploadSeckillSkuLatest3Days() {
    //1、重复上架无需处理
    log.info("上架秒杀的商品...");

    //分布式锁
    RLock lock = redissonClient.getLock(upload_lock);
    try {
        //加锁
        lock.lock(10, TimeUnit.SECONDS);
        //上架商品逻辑 
        seckillService.uploadSeckillSkuLatest3Days();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        lock.unlock();
    }
}
```

