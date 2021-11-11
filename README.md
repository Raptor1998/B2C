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