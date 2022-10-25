package dy.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import dy.dto.DishDto;
import dy.entity.Category;
import dy.entity.Dish;
import dy.service.CategoryService;
import dy.service.DishFlavorService;
import dy.service.DishService;

//菜品管理
@RestController
@RequestMapping("/dish")
public class DishController {

	@Autowired
	DishFlavorService dfs;
	@Autowired
	DishService ds;
	//菜品分类
	@Autowired
	private CategoryService cs;
	//新增菜品
	@PostMapping
	public Result<String> add(@RequestBody DishDto dto) {
		ds.savedto(dto);
		return Result.success("新增成功");
	}
	
	//菜品查询
	@GetMapping("/page")
	public Result<Page> page(int page,int pageSize,String name) {
		//dish中只存了id 没有name 需要给前端返回name值
		Page<Dish> pageinfo =new Page<>(page,pageSize);
		Page<DishDto> pagedishdto = new Page<>();
		LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
		//过滤条件
		lqw.like(StringUtils.isNotBlank(name), Dish::getName, name);
		//排序
		lqw.orderByAsc(Dish::getUpdateTime);
		ds.page(pageinfo, lqw);
		//拷贝属性
		BeanUtils.copyProperties(pageinfo, pagedishdto, "records");
		List<Dish> records = pageinfo.getRecords();
		List<DishDto> List= records.stream().map((item)->{
			DishDto dishDto = new DishDto();
			BeanUtils.copyProperties(item, dishDto);
			long categoryId = item.getCategoryId();
			//根据ID查询分类对象
			Category category = cs.getById(categoryId);
			String categoryName = category.getName();
			dishDto.setCategoryName(categoryName);
			return dishDto;
		}).collect(Collectors.toList());
		pagedishdto.setRecords(List);
		return Result.success(pagedishdto);
		
	}
	//查询菜品信息
	@GetMapping("/id")
	public Result<DishDto> get(@PathVariable Long id){
		DishDto dishDto = ds.get(id);
		return Result.success(dishDto);
		
	}
}
