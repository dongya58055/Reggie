package dy.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import dy.entity.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{

}
