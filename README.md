# B2C

尚硅谷谷粒商城的demo，**不是完整代码**。

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

