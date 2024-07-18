package com.shopme.admin.user.exproter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public abstract class AbstractExporter {
	
	public void setResponseHeader(HttpServletResponse response, String contentType, 
			String extension, String prefix ) throws IOException {
		DateFormat dataFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dataFormatter.format(new Date());
		String fileName = prefix + timestamp + extension;
		
		response.setContentType(contentType);
		
		// đặt tiêu đề của phản hồi để trình duyệt tải tệp về với tên tệp đã được tạo ở trên.
		String headerKey = "Content-Disposition"; 
		String headerValue="attachment; filename="+fileName;
		
		response.setHeader(headerKey, headerValue);
	}
	
}
