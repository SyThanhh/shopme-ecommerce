package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE) // chỉ định sd csdl ở app ko đc thay thế
@Rollback(false) // Giữ lại các thay đổi trong cơ sở dữ liệu sau khi kiểm thử kết thúc.
public class BrandRepositoryTests {

	@Autowired
	private BrandRepository brandRepo;
	
	@Test
	public void testCreateBrand1() {
		Category laptops = new Category(6);
		
		Brand acer = new Brand("Lenovo");
		
		acer.getCategories().add(laptops);
		acer.getCategories().forEach(p -> System.out.println(p));
		
		Brand savedBrand = brandRepo.save(acer);
		
		assertThat(savedBrand).isNotNull();
		
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	
	@Test
	public void testCreateBrand2() {
		Category cellphones = new Category(4);
		Category tables = new Category(7);
		
		Brand acer = new Brand("Sony");
		
		acer.getCategories().add(cellphones);
		acer.getCategories().add(tables);
		acer.getCategories().forEach(p -> System.out.println(p));
		
		Brand savedBrand = brandRepo.save(acer);
		
		assertThat(savedBrand).isNotNull();
		
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand3() {
		
		Brand acer = new Brand("Asus");
		
		acer.getCategories().add(new Category(29));
		acer.getCategories().add(new Category(24));
		acer.getCategories().forEach(p -> System.out.println(p));
		
		Brand savedBrand = brandRepo.save(acer);
		
		assertThat(savedBrand).isNotNull();
		
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		
		List<Brand> listBrands = (List<Brand>) brandRepo.findAll();
		
		listBrands.forEach(System.out::println);
		
		assertThat(listBrands).isNotEmpty();
	}
	
	@Test
	public void testGetById() {
		Brand brand = brandRepo.findById(1).get();
		
		assertThat(brand.getName()).isEqualTo("Asus");
	}
	
	@Test
	public void testUpdateName() {
		String newName = "Samsung Electronics";
		Brand samsung = brandRepo.findById(3).get();
		
		samsung.setName(newName);
		
		Brand savedBrand = brandRepo.save(samsung);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDeleteBandById() {
		
		Integer id = 3;
		brandRepo.deleteById(id);
		
		Optional<Brand> brand = brandRepo.findById(3);
		assertThat(brand.isEmpty());
	}
}
