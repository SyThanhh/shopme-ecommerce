package com.shopme.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.category.CategoryRepository;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategroyRepositoryTests {
								
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Test
	public void testListEnabledCategories() {
		List<Category> categories = categoryRepository.findAllEnabled();
		categories.forEach(cat -> {
			System.out.println(cat.getName() + " (" + cat.isEnabled() + " )");
		});
	}
	
	@Test
	public void testFindCatgegoryByAlias() {
		String alias = "electronics";
		
		Category category = categoryRepository.findByAliasEnabled(alias);
		
		assertThat(category).isNotNull();
	}
}
