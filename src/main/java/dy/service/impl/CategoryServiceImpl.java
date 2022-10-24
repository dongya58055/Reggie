package dy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import dy.common.DIYExo;
import dy.entity.Category;
import dy.entity.Dish;
import dy.entity.Setmeal;
import dy.mapper.CategoryMapper;
import dy.service.CategoryService;
import dy.service.DishService;
import dy.service.SetmealService;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
	@Autowired
	private DishService ds;
	@Autowired
	private SetmealService ss;

	//@Override
	public void remove(Long ids) {
		// 判断菜品中是否存在菜品分类的ID
		LambdaQueryWrapper<Dish> dishlqw = new LambdaQueryWrapper<>();
		dishlqw.eq(Dish::getCategoryId, ids);
		// 统计count数是否大于0
		long count = ds.count(dishlqw);
		if (count != 0) {
			// 给出错误异常
			throw new DIYExo("存在菜品绑定关系,无法删除");
		}
		// 判断菜品中是否存在套餐分类的ID
		LambdaQueryWrapper<Setmeal> setlqw = new LambdaQueryWrapper<>();
		setlqw.eq(Setmeal::getCategoryId, ids);
		// 统计count数是否大于0
		long count1 = ds.count(dishlqw);
		if (count1 != 0) {
			// 给出错误异常
			throw new DIYExo("存在套餐绑定关系,无法删除");
		}
		
		//都没有表示不存在关系
		super.removeById(ids);
	}
	
}
