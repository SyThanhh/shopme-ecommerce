package com.shopme.admin.brand.exporter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.user.exproter.AbstractExporter;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

public class BrandCsvExporter extends AbstractExporter {
	
	public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
			
			// type file and header  response
			super.setResponseHeader(response, "text/csv" ,".csv", "brands_");
			
			// response.getWriter(): Lấy PrintWriter từ đối tượng phản hồi HTTP
			//để ghi dữ liệu trực tiếp vào phản hồi, giúp trình duyệt tải về tệp CSV.
			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
					CsvPreference.STANDARD_PREFERENCE);//Tạo một đối tượng CsvBeanWriter để ghi dữ liệu vào phản hồi HTTP.
			
			String[] csvHeaer = {"Brand ID", "Brand Name"};
			String[] fieldMapping = {"id", "name"};
			
			csvWriter.writeHeader(csvHeaer);
			
			for (Brand brand : listBrands) {
				csvWriter.write(brand, fieldMapping);
			}
			
			csvWriter.close();
		}
}
