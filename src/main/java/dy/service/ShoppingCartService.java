package dy.service;

import com.baomidou.mybatisplus.extension.service.IService;

import dy.controller.Result;
import dy.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart>{
	Result<ShoppingCart> sub(ShoppingCart sCart);
}
