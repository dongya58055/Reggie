package dy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import dy.common.DIYExo;
import dy.dto.SetmealDto;
import dy.entity.Dish;
import dy.entity.Setmeal;
import dy.entity.SetmealDish;
import dy.mapper.SetmealMapper;
import dy.service.DishService;
import dy.service.SetmealDishService;
import dy.service.SetmealService;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService{

	//菜品和套餐的关系
	@Autowired
	SetmealDishService sds;
	/**
	 * 接口实现
	 */
	@Override
	//两表联动
	@Transactional
	public void saveWithDish(SetmealDto sdto) {
		//保存套餐的信息
		this.save(sdto);
		//插入套餐和菜品的关联信息
		List<SetmealDish> setmealDishes = sdto.getSetmealDishes();
		//进行id的复制
		setmealDishes.stream().map((item)->{
			item.setSetmealId(sdto.getId());
			return item;
		}).collect(Collectors.toList());
		sds.saveBatch(setmealDishes);
	}

	/**
	 * 删除两表
	 */
	@Override
	@Transactional
	public void removeWithDish(List<Long> ids) {
		//查询状态是否可删除,1为可售 
		//selec count(*) from setmeal where id in ids and status =1
		LambdaQueryWrapper<Setmeal> lqwsm = new LambdaQueryWrapper<>();
		lqwsm.in(Setmeal::getId, ids);
		lqwsm.eq(Setmeal::getStatus, 1);
		//查询出来的条件数，包含了多删除的思想
		long count = super.count(lqwsm);
		//如果不能删除，抛出异常
		if(count>0) {
			throw new DIYExo("套餐正在售卖中，无法删除");
		}
		//可以删除，先删除套餐表的数据
		super.removeByIds(ids);
		//删除关系表中的数据
		//delete from setmealdish where setmealid in ids
		LambdaQueryWrapper<SetmealDish> lqwsmd = new LambdaQueryWrapper<>();
		lqwsmd.in(SetmealDish::getSetmealId, ids);
		sds.remove(lqwsmd);
	}

	@Override
	public void updateStatus(int status,List<Long> ids) {
		//update setmeal set status = id where id in ;
		LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
		lqw.in(Setmeal::getId,ids);
		//select * from setmeal where id in;
		List<Setmeal> list = super.list(lqw);
		for (Setmeal item : list) {
			item.setStatus(status);
			this.updateById(item);
		}
	}


}
