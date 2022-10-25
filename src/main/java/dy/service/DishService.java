package dy.service;

import com.baomidou.mybatisplus.extension.service.IService;

import dy.dto.DishDto;
import dy.entity.Dish;

public interface DishService extends IService<Dish>{

	//新增菜品 同时插入两张表的数据 dish dishflavor
	public void savedto(DishDto dto);
	//根据ID查询菜品信息和口味
	public DishDto get(Long id);
}
