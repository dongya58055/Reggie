package dy.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import dy.common.BaseContext;
import dy.common.DIYExo;
import dy.entity.AddressBook;
import dy.entity.OrderDetail;
import dy.entity.Orders;
import dy.entity.ShoppingCart;
import dy.entity.User;
import dy.mapper.OrderMapper;
import dy.service.AddressBookService;
import dy.service.OrderDeatilService;
import dy.service.OrderService;
import dy.service.ShoppingCartService;
import dy.service.UserService;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

	@Autowired
	private ShoppingCartService scs;

	@Autowired
	private UserService us;

	@Autowired
	private AddressBookService abs;
	
	@Autowired
	private OrderDeatilService ods;
	/**
	 * 用户下单
	 */

	private long id;

	@Override
	public void submit(Orders orders) {
		// 获取用户id
		Long currentId = BaseContext.getCurrentId();
		// 查询购物车信息
		// SQL:select * from where userid =
		LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
		lqw.eq(ShoppingCart::getUserId, currentId);
		List<ShoppingCart> list = scs.list(lqw);
		// 对列表进行优化
		if (list == null || list.size() == 0) {
			throw new DIYExo("购物车为空，不能下单");
		}
		// 查询用户数据
		User user = us.getById(currentId);
		// 查询用户地址
		AddressBook addressBook = abs.getById(orders.getAddressBookId());
		if (addressBook == null) {
			throw new DIYExo("地址为空，不能下单");
		}
		// 订单表插入数据
		// 订单号
		long orderId = IdWorker.getId();
		// 金额 原子性 可以多线程保证
		AtomicInteger amount = new AtomicInteger(0);
		List<OrderDetail> orderDetails=list.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
		
		orders.setNumber(String.valueOf(orderId));
		orders.setId(orderId);
		orders.setOrderTime(LocalDateTime.now());
		orders.setCheckoutTime(LocalDateTime.now());
		orders.setStatus(2);
		orders.setAmount(new BigDecimal(amount.get()));// 计算总金额
		orders.setUserId(currentId);
		orders.setUserName(user.getName());
		orders.setConsignee(addressBook.getConsignee());
		orders.setPhone(addressBook.getPhone());
		orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
				+ (addressBook.getCityName() == null ? "" : addressBook.getCityName())
				+ (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
				+ (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
		
		this.save(orders);
		// 订单明细表插入多条数据
		ods.saveBatch(orderDetails);
		//删除购物车数据
		//SQL delete from where userid 
		scs.remove(lqw);
	}

}
