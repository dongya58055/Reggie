package dy.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import dy.entity.OrderDetail;
import dy.mapper.OrderDetailMapper;
import dy.service.OrderDeatilService;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDeatilService{

}
