package dy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dy.entity.Orders;
import dy.service.OrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {
	@Autowired
	private OrderService os;
	
	/**
	 * 
	 * 描述:提交订单
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/submit")
	public Result<String> submit(@RequestBody Orders orders){
		os.submit(orders);
		return Result.success("下单成功");
		
	}
}
