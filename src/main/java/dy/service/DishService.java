package dy.service;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.IService;

import dy.dto.DishDto;
import dy.entity.Dish;

public interface DishService extends IService<Dish> {

	// 新增菜品 同时插入两张表的数据 dish dishflavor
	void savedto(DishDto dto);

	// 根据ID查询菜品信息和口味
	DishDto get(Long id);

	// 修改菜品
	@Transactional
	void update(DishDto dto);
	/**
	 * 
	 * 描述:菜品起售状态
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	
}
