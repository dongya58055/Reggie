package dy.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.HttpResource;

@RestController
@RequestMapping("/common")
public class CommonController {
	//文件上传和下载
	@Value("${reggie.basepath}")
	private String path;
	
	@PostMapping("/upload")
	public Result<String> upload(MultipartFile file) throws IllegalStateException, IOException{
		//file为临时文件，需要转存到指定位置，否则请求完后文件会删除
		//获取原先名字的后缀
		String originalFilename = file.getOriginalFilename();
		String lastname = originalFilename.substring(originalFilename.lastIndexOf("."));
		//uuid生成名字
		String name = String.valueOf(UUID.randomUUID())+lastname;
		//如果目录不存在，先进行目录创建
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		file.transferTo(new File(path+name));
		return Result.success(name);
	}
	
	@GetMapping("/download")
	public void downlocad(String name,HttpServletResponse response) throws IOException {
		//输入流 读取文件内容  对应路径加上前端传的文件名称
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(path+name)));
		//输出流 将文件写到浏览器
		ServletOutputStream outputStream = response.getOutputStream();
		byte[] bytes = new byte[1024];
		int len=0;
		while ((len=bis.read(bytes))!=-1) {
			outputStream.write(bytes, 0, len);
			outputStream.flush();
		}
		
		bis.close();
		outputStream.close();
	}
}
