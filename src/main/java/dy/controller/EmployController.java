package dy.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import dy.entity.Employee;
import dy.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployController {
	@Autowired
	private EmployeeService es;

	// hr可以存储session，em为用户和密码的实体
	@PostMapping("login")
	public Result<Employee> login(HttpServletRequest hr, @RequestBody Employee em) {
		// 将页面的密码进行md5加密
		String password = em.getPassword();
		password = DigestUtils.md5DigestAsHex(password.getBytes());

		// 用username查询数据库
		LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
		lqw.eq(Employee::getUsername, em.getUsername());
		// 条件查询到一个数据 转换成实体类
		Employee employee = es.getOne(lqw);
		// 如果没有查到显示失败
		if (employee == null) {
			return Result.error("用户不存在");
		}

		// 密码不一致就登录失败
		if (!employee.getPassword().equals(password)) {
			return Result.error("密码错误");
		}
		// 员工状态为禁用也登录不了
		if (employee.getStatus() == 0) {
			return Result.error("用户被禁用");
		}
		// 将员工id存入session并返回登录成功
		hr.getSession().setAttribute("employee", employee.getId());
		return Result.success(employee);

	}

	// 退出
	@PostMapping("logout")
	public Result<String> logout(HttpServletRequest hr) {
		// 清理session中数据
		hr.getSession().removeAttribute("employee");
		return Result.success("退出成功");

	}

	// 新增员工
	@PostMapping
	public Result<String> save(HttpServletRequest hr, @RequestBody Employee em) {
		// 给出初始密码
		String password = "123456";
		em.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
		 //em.setCreateTime(LocalDateTime.now());
		 //em.setUpdateTime(LocalDateTime.now());
		 //获取当前登录用户的id，传给创建人
		// long emId = (long) hr.getSession().getAttribute("employee");
		// em.setCreateUser(emId);
		// em.setUpdateUser(emId);
		es.save(em);
		return Result.success("创建成功");

	}

	// 分页查询
	@GetMapping("/page")
	public Result<Page> page(int page, int pageSize, String name) {
		log.info("page={},pageSize={},name={}", page, pageSize, name);
		// 构造分页构造器
		Page pageinfo = new Page(page, pageSize);

		// 构造条件构造器
		LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
		// 名称查询
		lqw.like(StringUtils.isNotBlank(name), Employee::getName, name);
		// 排序条件
		lqw.orderByDesc(Employee::getUpdateTime);
		// 执行查询
		es.page(pageinfo, lqw);

		return Result.success(pageinfo);
	}

	// 禁用/启动
	@PutMapping
	public Result<String> update(HttpServletRequest request, @RequestBody Employee employee) {
		// 获取更新人
		// employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
		// employee.setUpdateTime(LocalDateTime.now());
		// 修改信息
//		Long id = Thread.currentThread().getId();
//		log.info("线程id是{}",id);
//		long id = Thread.currentThread().getId();
//	    log.info("线程id为：{}",id);
		es.updateById(employee);
		return Result.success("更新成功");
	}

	// 编辑 
	@GetMapping("/{id}")
	public Result<Employee> getById(@PathVariable Long id) {
		Employee byId = es.getById(id);
		if(byId != null){
            return Result.success(byId);
        }
        return Result.error("没有查询到对应员工信息");

	}
}
