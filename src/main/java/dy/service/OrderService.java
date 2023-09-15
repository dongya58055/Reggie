package dy.service;

import com.baomidou.mybatisplus.extension.service.IService;

import dy.entity.Orders;

public interface OrderService extends IService<Orders>{
	public void submit(Orders orders);
}
