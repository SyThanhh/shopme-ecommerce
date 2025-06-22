package com.shopme.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.product.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	@Query("SELECT p FROM Product p WHERE p.enabled = true "
		     + "AND (p.category.id = :categoryId OR p.category.allParentIDs LIKE CONCAT('%', :categoryIDMatch, '%')) "
		     + "ORDER BY p.name ASC")
		public Page<Product> listByCategory(@Param("categoryId") Integer categoryId, 
		                                    @Param("categoryIDMatch") String categoryIDMatch, 
		                                    Pageable pageable);

	    public Product findByAlias(String alias);
	    
	    
	    @Query(value = "SELECT * FROM products p WHERE p.enabled = true " +
	               "AND MATCH(name, short_description, full_description) AGAINST (?1 IN NATURAL LANGUAGE MODE)",
	       nativeQuery = true)
	    public Page<Product> search(String keyword, Pageable pageable);

}
