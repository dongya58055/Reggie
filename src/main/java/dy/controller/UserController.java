package dy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import dy.controller.Result;
import dy.entity.User;
import dy.service.UserService;
import dy.utils.SMSUtils;
import dy.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
	@Autowired
	private UserService us;
	//private Object String;
	
	/**
	 * 
	 * 描述:发送验证码
	 * @param 参数说明:手机号,生成的验证码保存在session中
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/sendMsg")
	public Result<String> sendMsg(@RequestBody User user,HttpSession session){
		//获取手机号
		String phone = user.getPhone();
		//手机号不为空
		if(StringUtils.isNotEmpty(phone)) {
			//获取4位验证码
			String code = ValidateCodeUtils.generateValidateCode(4).toString();
			//System.out.println(code);
			log.info("code=>"+code);
			//SMSUtils.sendMessage("阿里云短信测试","SMS_154950909", phone, code);
			//将验证码保存到session
			session.setAttribute(phone, code);
			return Result.success("短信发送成功");
		}
		return Result.error("短信发送失败");
	}
	
	/**
	 * 
	 * 描述:登录
	 * @param 参数说明:用map接收json的格式
	 * @return 返回值:
	 * @exception 异常:
	 */
	@PostMapping("/login")
	public Result<User> login(@RequestBody Map user,HttpSession session){
		//获取手机号
		String phone = user.get("phone").toString();
		//获取验证码
		String code = user.get("code").toString();
		//从session中获取验证码
		String codeinSession = session.getAttribute(phone).toString();
		//进行验证码的比对
		if (codeinSession!=null&&codeinSession.equals(code)) {
			//如果比对成功，就登录成功
			//如果是新用户，进行注册
			LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
			lqw.eq(User::getPhone, phone);
			User user2 = us.getOne(lqw);
			if (user2==null) {
				user2 = new User();
				user2.setPhone(phone);
				us.save(user2);
			}
			//登录成功后，将id保存在session中
			session.setAttribute("user", user2.getId());
			return Result.success(user2);
		}
		return Result.error("登录失败");
	}
}
