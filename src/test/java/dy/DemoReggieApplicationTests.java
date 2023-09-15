package dy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class DemoReggieApplicationTests {

	@Autowired
	private StringRedisTemplate redis;
	@Test
	void contextLoads() {
		redis.opsForValue().set("city", "beijing");
	}

}
