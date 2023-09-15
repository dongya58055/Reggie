package dy.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import dy.entity.Orders;

@Mapper
public interface OrderMapper extends BaseMapper<Orders>{

}
