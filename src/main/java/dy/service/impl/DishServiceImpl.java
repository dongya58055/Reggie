package dy.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import dy.dto.DishDto;
import dy.entity.Dish;
import dy.entity.DishFlavor;
import dy.mapper.DishMapper;
import dy.service.DishFlavorService;
import dy.service.DishService;

@Service

public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{

	@Autowired
	private DishFlavorService dfs;
	
	@Transactional
	public void savedto(DishDto dto) {
		//保存菜品表的信息
		super.save(dto);
		//菜品中的菜品分类ID同时插入到口味的分类ID
		Long DishId = dto.getId();
		List<DishFlavor> flavors = dto.getFlavors();
		for (DishFlavor dishFlavor : flavors) {
			dishFlavor.setDishId(DishId);
		}
		//保存菜品口味
		dfs.saveBatch(dto.getFlavors());
	}

	@Override
	public DishDto get(Long id) {
		//查询菜品信息
		Dish dish = this.getById(id);
		
		DishDto dishdto = new DishDto();
		BeanUtils.copyProperties(dish, dishdto);
		//查询对应的口味信息dish_fla
		LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
		lqw.eq(DishFlavor::getDishId,dish.getId());
		List<DishFlavor> flavors = dfs.list(lqw);
		dishdto.setFlavors(flavors);
		return dishdto;
	}

}
