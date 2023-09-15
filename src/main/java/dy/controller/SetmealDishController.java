package dy.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import dy.dto.SetmealDto;
import dy.entity.Category;
import dy.entity.Setmeal;
import dy.entity.SetmealDish;
import dy.service.CategoryService;
import dy.service.DishService;
import dy.service.SetmealDishService;
import dy.service.SetmealService;
import lombok.extern.slf4j.Slf4j;

//套餐管理
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealDishController {

	@Autowired
	private SetmealService ss;
	@Autowired
	private CategoryService cs;
	/**
	 * 
	 * 描述:添加套餐
	 *
	 * @param 参数说明:json格式
	 * @return 返回值:
	 * @exception 异常:
	 * @see JavaDoc
	 */
	@PutMapping
	public Result<String> save(@RequestBody SetmealDto sdto){
		ss.saveWithDish(sdto);
		return Result.success("保存成功");
	}
	
	/**
	 * 
	 * 描述:套餐查询
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@GetMapping("/page")
	public Result<Page> page(int page,int pageSize,String name){
		//分页构造器
		Page<Setmeal> pageInfo = new Page<>(page,pageSize);
		//需要setdto的categoryName
		Page<SetmealDto> pageDto = new Page<>();
		//表达式
		LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
		//查询条件是否有值
		lqw.like(name !=null,Setmeal::getName,name);
		//没有的话默认更新时间排序
		lqw.orderByDesc(Setmeal::getUpdateTime);
		ss.page(pageInfo,lqw);
		//泛型不一致 拷贝除了records存储的信息
		BeanUtils.copyProperties(pageInfo, pageDto,"records");
		List<Setmeal> records = pageInfo.getRecords();
		List<SetmealDto> list = records.stream().map((item)->{
			SetmealDto sDto = new SetmealDto();
			//对象拷贝除了name的属性
			BeanUtils.copyProperties(item, sDto);
			//获取套餐菜品的id
			Long categoryId = item.getCategoryId();
			//根据id查询对应的套餐名称
			Category category = cs.getById(categoryId);
			if(category!=null) {
				//给name赋值
				String categoryName = category.getName();
				sDto.setCategoryName(categoryName);
			}
			return sDto;
		}).collect(Collectors.toList());
		pageDto.setRecords(list);
		return Result.success(pageDto);
	}
	/**
	 * 
	 * 描述:删除和一起删除套餐
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@DeleteMapping
	public Result<String> remove(@RequestParam List<Long> ids){
		ss.removeWithDish(ids);
		return Result.success("删除成功");
	}
	/**
	 * 
	 * 描述:修改状态
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/status/{status}")
	public Result<String> status(@PathVariable int status,@RequestParam List<Long> ids){
		ss.updateStatus(status, ids);
		return Result.success("更改成功");
	}
	
	@GetMapping("/list")
	public Result<List<Setmeal>> list(Setmeal setmeal){
		LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
		lqw.eq(Setmeal::getCategoryId, setmeal.getCategoryId());
		lqw.eq(Setmeal::getStatus, setmeal.getStatus());
		lqw.orderByDesc(Setmeal::getUpdateTime);
		List<Setmeal> list = ss.list(lqw);
		return Result.success(list);
		
	}
}
