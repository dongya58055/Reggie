package dy.service.impl;

import java.util.List;
import java.util.stream.Collectors;

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
		List<DishFlavor> flavors = dto.getFlavors();
		//根据id获取对应的名称
		Long DishId = dto.getId();
//		for (DishFlavor dishFlavor : flavors) {
//			dishFlavor.setDishId(DishId);
//			//dishFlavor.setIsDeleted(0);
//		}
		flavors = flavors.stream().map((item)->{
			item.setDishId(DishId);
			return item;
		}).collect(Collectors.toList());
		//保存菜品口味
		dfs.saveBatch(dto.getFlavors());
	}

	@Override
	@Transactional
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

	@Override
	@Transactional
	public void update(DishDto dto) {
		//更新dish信息
		this.updateById(dto);
		//更新口味信息
		//先清理口味数据 dishflavor的delete
		LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
		lqw.eq(DishFlavor::getDishId, dto.getId());
		dfs.remove(lqw);
		//在添加当前提交的口味数据 dishflavor的insert
		//先获取当前口味数据
		List<DishFlavor> flavors = dto.getFlavors();
		//根据id获取对应的名称
		Long DishId = dto.getId();
//		for (DishFlavor dishFlavor : flavors) {
//			dishFlavor.setDishId(DishId);
//			//dishFlavor.setIsDeleted(0);
//		}
		flavors = flavors.stream().map((item)->{
			item.setDishId(DishId);
			return item;
		}).collect(Collectors.toList());
		dfs.saveBatch(flavors);
	}

	
}
