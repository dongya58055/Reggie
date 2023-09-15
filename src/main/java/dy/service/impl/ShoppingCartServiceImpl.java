package dy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import dy.common.BaseContext;
import dy.controller.Result;
import dy.entity.ShoppingCart;
import dy.mapper.ShoppingCartMapper;
import dy.service.ShoppingCartService;
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService{
	
	@Override
	public Result<ShoppingCart> sub(ShoppingCart sCart) {
		//设置用户id，指定当前是哪个用户的购物车
				Long currentId = BaseContext.getCurrentId();
				sCart.setUserId(currentId);
				//判断当前是菜品还是套餐
				Long dishId = sCart.getDishId();
				//匹配用户id
				LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
				lqw.eq(ShoppingCart::getUserId,sCart.getUserId());
				if (dishId!=null) {
					//为菜品
					lqw.eq(ShoppingCart::getDishId, sCart.getDishId());
				}else {
					lqw.eq(ShoppingCart::getSetmealId, sCart.getSetmealId());
				}
				//SQL:select * from  where userid= and dish = 
				ShoppingCart one = this.getOne(lqw);
				if (one.getNumber()==1) {
					//删除要先将数量改成0 否则前端依旧会判断存在1个
					one.setNumber(one.getNumber()-1);
					super.remove(lqw);
				}else {
					//数量减一
					one.setNumber(one.getNumber()-1);
					this.updateById(one);
				}
				return Result.success(one);
	}

}
