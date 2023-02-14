package cuculi.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Test
    public void testString(){
        ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("redisss", "hei");
    }
}
