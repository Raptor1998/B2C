package com.raptor.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.raptor.gulimall.search.config.ElasticConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author raptor
 * @description GulimallSearchTest
 * @date 2021/10/27 17:24
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallSearchTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        User user = new User();
        user.setUsername("张三");
        user.setAge(12);
        user.setGender("男");
        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);
        IndexResponse index = restHighLevelClient.index(indexRequest, ElasticConfig.COMMON_OPTIONS);

        System.out.println(index);
    }

    @Data
    class User {
        private String username;
        private String gender;
        private Integer age;
    }


}
