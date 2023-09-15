package dy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import dy.entity.User;
import dy.mapper.UserMapper;
import dy.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
}
