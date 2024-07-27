package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// cấu hình class này để hiển thị ảnh bên clients
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		//User
	    // Tên thư mục chứa ảnh người dùng
	    String dirName = "user-photos";
	    
	    exposeDirectory(dirName, registry);
	    
	    //Category
	    String categoryDirName = "../categories-images";
	    exposeDirectory(categoryDirName, registry);
	  

	    // Brand
	    String brandDirName = "../brands-logos";
	    exposeDirectory(brandDirName, registry);
	}

	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		 // Tạo đường dẫn tới thư mục ảnh người dùng
	    Path path = Paths.get(pathPattern);
	    
	    // Lấy đường dẫn tuyệt đối của thư mục ảnh người dùng
	    String absolutePath = path.toFile().getAbsolutePath();
	    
	    String logicalPath = pathPattern.replace("..", "") + "/**";
	    
	    // Cấu hình bộ xử lý tài nguyên cho các ảnh người dùng
	    registry.addResourceHandler(logicalPath)
	        .addResourceLocations("file:/" + absolutePath + "/");
	}

}
