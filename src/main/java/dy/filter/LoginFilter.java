package dy.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;

import com.alibaba.fastjson2.JSON;

import dy.common.BaseContext;
//import dy.common.BaseContext;
import dy.controller.Result;
import dy.entity.Employee;
import lombok.extern.slf4j.Slf4j;

//登录过滤器
@WebFilter(filterName = "loginfilter", urlPatterns = "/*")
@Slf4j
public class LoginFilter implements Filter {
	//创建路径匹配器，支持通配符
	public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		//获取本次的uri
		String requestURI = request.getRequestURI();
		//不需要处理的请求
		String[] urls= new String[] {
			"/employee/login",
			"/employee/logout",
			"/backend/**",
			"/front/**",
			"/common/**",
			"/user/sendMsg",
            "/user/login"
		};
		//创建方法，判断请求是否需要处理
		boolean check = check(requestURI, urls);
		//如果不需要处理 直接放行
		if (check) {
			chain.doFilter(request, response);
			return;
		};
		
		//判断后台登录状态，如果已登录，直接放行
		if(request.getSession().getAttribute("employee")!=null) {
			
			//Long id = (Long) request.getSession().getAttribute("employee");
//			Long id = Thread.currentThread().getId();
//			log.info("线程id是{}",id);
			//log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
			//获取进程
			Long id = (Long) request.getSession().getAttribute("employee");
		//	log.info("线程id{}",id);
			BaseContext.setCurrentId(id);
		//	System.out.println("BaseContext.setCurrentId的值是"+id);
			chain.doFilter(request, response);
			return;
		}
		//判断移动登录状态，如果已登录，直接放行
				if(request.getSession().getAttribute("user")!=null) {
					
					//Long id = (Long) request.getSession().getAttribute("employee");
//					Long id = Thread.currentThread().getId();
//					log.info("线程id是{}",id);
					//log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
					//获取进程
					Long id = (Long) request.getSession().getAttribute("user");
				//	log.info("线程id{}",id);
					BaseContext.setCurrentId(id);
				//	System.out.println("BaseContext.setCurrentId的值是"+id);
					chain.doFilter(request, response);
					return;
				}
		//如果未登录则返回结果,通过结果发送给前端
		response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
		return;
		
		
	}
	
	//判断方法 真就放行
	public boolean check(String requestURI,String urls[]) {
		for (String url : urls) {
			boolean match = PATH_MATCHER.match(url, requestURI);
			if(match) {
				return true;
			}
		}
		return false;
	}
}
