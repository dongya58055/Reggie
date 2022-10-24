package dy.service;


import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.IService;

import dy.entity.Employee;

@Transactional
public interface EmployeeService extends IService<Employee> {

}
