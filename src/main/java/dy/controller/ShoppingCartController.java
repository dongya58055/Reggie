package dy.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import dy.common.BaseContext;
import dy.entity.ShoppingCart;
import dy.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	@Autowired
	private ShoppingCartService scs;
	
	/**
	 * 
	 * 描述:添加购物车
	 * @param 参数说明: json传参
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/add")
	public Result<ShoppingCart> add(@RequestBody ShoppingCart sCart) {
		// 设置用户id，指定当前是哪个用户的购物车
		Long currentId = BaseContext.getCurrentId();
		sCart.setUserId(currentId);
		// 查询当前菜品或套餐是否在购物车中
		Long dishId = sCart.getDishId();
		LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
		lqw.eq(ShoppingCart::getUserId, sCart.getUserId());
		if (dishId != null) {
			// 当前添加的是菜品
			lqw.eq(ShoppingCart::getDishId, sCart.getDishId());
		} else {
			lqw.eq(ShoppingCart::getSetmealId, sCart.getSetmealId());
		}
		// 查询当前菜品是否在购物车中
		ShoppingCart one = scs.getOne(lqw);
		if (one != null) {
			// 说明已经存在了 直接数量加1
			Integer number = one.getNumber();
			one.setNumber(++number);
			scs.updateById(one);
		} else {
			// 如果不存在，需要手动添加数量为1
			sCart.setNumber(1);
			// 自动注入的映射是两个 userid和createtime 如果只tablefied注解一个会报错
			sCart.setCreateTime(LocalDateTime.now());
			scs.save(sCart);
			one = sCart;
		}
		return Result.success(one);
	}
	/**
	 * 
	 * 描述:减少购物车数
	 * @param 参数说明:id的json传参
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/sub")
	public Result<ShoppingCart> sub(@RequestBody ShoppingCart sCart){
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
		ShoppingCart one = scs.getOne(lqw);
		if (one.getNumber()==1) {
			//删除要先将数量改成0 否则前端依旧会判断存在1个
			one.setNumber(one.getNumber()-1);
			scs.remove(lqw);
		}else {
			//数量减一
			one.setNumber(one.getNumber()-1);
			scs.updateById(one);
		}
		return Result.success(one);
	}
	/**
	 * 
	 * 描述:查看购物车
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/list")
	public Result<List<ShoppingCart>> list(){
		LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
		lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
		lqw.orderByAsc(ShoppingCart::getCreateTime);
		List<ShoppingCart> list = scs.list(lqw);
		return Result.success(list);
	}
	
	@DeleteMapping("/clean")
	public Result<String> clean(){
		LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
		lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
		scs.remove(lqw);
		return Result.success("清除成功");
		
	}
}
