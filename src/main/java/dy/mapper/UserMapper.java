package dy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import dy.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
