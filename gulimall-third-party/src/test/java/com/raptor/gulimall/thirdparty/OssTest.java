package com.raptor.gulimall.thirdparty;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;

/**
 * @author raptor
 * @description OssTest
 * @date 2021/10/15 21:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OssTest {

    @Autowired
     OSSClient ossClient;


    @Test
    public void context() {
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "raptor-project";
// 填写文件名。文件名包含路径，不包含Bucket名称。例如exampledir/exampleobject.txt。
        String objectName = "gulimall/yingmu.jpg";
        try {
            String content = "C:\\Users\\Administrator\\Pictures\\Camera Roll\\QQ图片20210604092048.jpg";
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
        } catch (OSSException e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

}
