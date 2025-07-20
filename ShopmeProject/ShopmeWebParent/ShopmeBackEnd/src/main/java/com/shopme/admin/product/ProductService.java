package com.shopme.admin.product;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.contstant.SystemConstant;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> findAll() {
		return (List<Product>) productRepository.findAll();
	}
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper, Integer categoryId) {
		Pageable pageable = helper.createPageable(SystemConstant.PRODUCTS_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();
		Page<Product> page = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page = productRepository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
			} else {
				page = productRepository.findAll(keyword, pageable);
			}
		} else {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page = productRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
			} else {		
				page = productRepository.findAll(pageable);
			}
		}
		
		helper.updateModelAttributes(pageNum, page);
	}
	
	// search product in order
	
	
	public void searchProducts(int pageNum, PagingAndSortingHelper helper) {
		Pageable pageable = helper.createPageable(SystemConstant.PRODUCTS_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();		
		Page<Product> page = productRepository.searchProductsByName(keyword, pageable);		
		helper.updateModelAttributes(pageNum, page);
	}
	
	
	@Transactional
	public Product saveProduct(Product product) {
	    if (product.getId() == null) {
	        product.setCreatedTime(new Date());
	    }
	    
	    // Thiết lập alias
	    if (product.getAlias() == null || product.getName() == null || product.getName().isEmpty()) {
	        String defaultAlias = product.getName() != null ? product.getName().replace(" ", "-") : "default-alias";
	        product.setAlias(defaultAlias);
	    } else {
	        product.setAlias(product.getAlias().replace(" ", "-"));
	    }

	    // Cập nhật thời gian
	    product.setUpdatedTime(new Date());


	    return productRepository.save(product);
	}
	
	

	public Product saveProductPrice(Product productInForm) throws ProductNotFoundException {
		try {
			Product productInDB = productRepository.findById(productInForm.getId()).get();
			productInDB.setPrice(productInForm.getPrice());
			productInDB.setCost(productInForm.getCost());
			productInDB.setDiscountPercent(productInForm.getDiscountPercent());
			
			return productRepository.save(productInDB);
		} catch (Exception e) {
			throw new ProductNotFoundException("Could nopt find any product with ID " + productInForm.getId());
		}
	
	}
	
	public String checkUnique(Integer id, String name) {
		boolean checkProduct = (id == null || id == 0);
		Product productByName = productRepository.findByName(name);
		if(checkProduct) {
			if(productByName != null) return "Duplicate";
		} else {
			if(productByName != null && productByName.getId() != id) return "Duplicate";
		}
		return "OK";
		
	}

	public void updateStatusProduct(Integer id, boolean enabled) {
		productRepository.updateEnabledStatus(id, enabled);
		
	}

	public void deleteById(Integer id) throws CategoryNotFoundException {
		Long countById = productRepository.countById(id);

		if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any Product with ID " + id);
		}

		productRepository.deleteById(id);
	}
	
	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return productRepository.findById(id).get();
		} catch (Exception e) {
			throw new ProductNotFoundException("Could nopt find any product with ID " + id);
		}
		
	}
	
	
}
