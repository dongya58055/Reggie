package dy.controller;

import java.util.List;

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
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import dy.entity.Category;
import dy.entity.Employee;
import dy.mapper.CategoryMapper;
import dy.service.CategoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
	
	@Autowired
	private CategoryService cs;
	
	//新增分类
	@PostMapping
	public Result<String> save(@RequestBody Category category) {
		cs.save(category);
		return Result.success("添加成功");
	}
	
	//分页
	@GetMapping("/page")
	public Result<Page> page(int page, int pageSize) {
		//log.info("page={},pageSize={},name={}", page, pageSize);
		// 构造分页构造器
		Page pageinfo = new Page(page, pageSize);

		// 构造条件构造器
		LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
		// 排序条件
		lqw.orderByAsc(Category::getSort);
		// 执行查询
		cs.page(pageinfo, lqw);
		return Result.success(pageinfo);
	}
	
	//删除分类
	@DeleteMapping
	public Result<String> deleteByIds(@RequestParam Long ids){
		
		//cs.removeById(ids);
		cs.remove(ids);
		return Result.success("删除成功");
		
	}
	
	//修改
	@PutMapping
	public Result<String> update(@RequestBody Category category){
		cs.updateById(category);
		return Result.success("修改成功");
	}
	
	
	//查询菜品分类 在增加菜品中使用
	@GetMapping("/list")
	public Result<List<Category>> list(Category category) {
		//条件构造器
		LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
		lqw.eq(category.getType()!=null,Category::getType,category.getType());
		//按照顺序排序
		lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
		List<Category> list = cs.list(lqw);
		return Result.success(list);
	}
}
