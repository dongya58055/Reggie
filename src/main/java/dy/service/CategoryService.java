package dy.service;

import com.baomidou.mybatisplus.extension.service.IService;

import dy.entity.Category;

public interface CategoryService extends IService<Category>{

	public void remove(Long ids);
	
}
