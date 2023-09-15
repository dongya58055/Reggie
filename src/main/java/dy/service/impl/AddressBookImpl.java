package dy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import dy.entity.AddressBook;
import dy.mapper.AddressBookMapper;
import dy.service.AddressBookService;
@Service
public class AddressBookImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService{
	
}
