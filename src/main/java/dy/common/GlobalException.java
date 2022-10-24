package dy.common;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Iterator;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dy.controller.Result;
import lombok.extern.slf4j.Slf4j;

//全局异常处理
@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalException {
	//处理所有异常
	@ExceptionHandler
	public Result<String> doEx(SQLIntegrityConstraintViolationException ex){
	//public Result<String> doEx(Exception ex){
		//打印错误日志
		//log.error(ex.getMessage());
		//判断是否为sql错误 是的话截出来并打印 ### Error updating database.  Cause: 
		//java.sql.SQLIntegrityConstraintViolationException: Duplicate entry 'dy1'
		if(ex.getMessage().contains("Duplicate entry")) {
			log.error(ex.getMessage());
			String[] error = ex.getMessage().split(" ");
			//名字在数组的第2位
			String msg = error[2]+"已存在";
			return Result.error(msg);
		}
		return Result.error("未知错误");
		
	}
	//自定义异常
	@ExceptionHandler
	public Result<String> doEx(DIYExo ex){
	//public Result<String> doEx(Exception ex){
		//打印错误日志
		//log.error(ex.getMessage());
		//判断是否为sql错误 是的话截出来并打印 ### Error updating database.  Cause: 
		//java.sql.SQLIntegrityConstraintViolationException: Duplicate entry 'dy1'
//		if(ex.getMessage().contains("Duplicate entry")) {
//			log.error(ex.getMessage());
//			String[] error = ex.getMessage().split(" ");
//			//名字在数组的第2位
//			String msg = error[2]+"已存在";
//			return Result.error(msg);
//		}
		return Result.error(ex.getMessage());
		
	}
}
