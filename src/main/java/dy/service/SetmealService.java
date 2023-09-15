package dy.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import dy.dto.SetmealDto;
import dy.entity.Setmeal;


public interface SetmealService extends IService<Setmeal>{
	/**
	 * 
	 * 描述:新增套餐，保存套餐和菜品的关系 两张表
	 *
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 * @see JavaDoc
	 */
	void saveWithDish(SetmealDto sdto);
	/**
	 * 
	 * 描述:删除套餐
	 * @param 参数说明:套餐和菜品的关系图 两张表
	 * @return 返回值:
	 * @exception 异常:
	 */
	void removeWithDish(List<Long> ids);
	/**
	 * 
	 * 描述:启停套餐
	 * @param 参数说明:
	 * @return 返回值:
	 * @exception 异常:
	 */
	void updateStatus(int status,List<Long> ids);
}
