package dy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootApplication
//过滤器
@ServletComponentScan
//事务
@EnableTransactionManagement
//扫描数据
@MapperScan({"dy.mapper"})
public class DemoReggieApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoReggieApplication.class, args);
		
		log.info("项目启动成功");
	}

}
