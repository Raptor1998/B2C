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

![image-20211013154643458](E:\material\md\image-20211013154643458.png)

![image-20211013154656262](E:\material\md\image-20211013154656262.png)

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

