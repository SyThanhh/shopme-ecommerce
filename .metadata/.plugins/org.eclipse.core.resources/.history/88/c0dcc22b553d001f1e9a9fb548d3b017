package com.shopme.admin.user;

import com.shopme.common.entity.Role;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest // Cấu hình kiểm thử JPA với các thành phần cần thiế
@AutoConfigureTestDatabase(replace = Replace.NONE) // chỉ định sd csdl ở app ko đc thay thế
@Rollback(false) // Giữ lại các thay đổi trong cơ sở dữ liệu sau khi kiểm thử kết thúc.
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository repo;

    @Test
    public void testCreateRole() {
        Role roleAdmin = new Role("Admin", "manage everything");
        Role savedRole = repo.save(roleAdmin);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }
    
    @Test
    public void testCreateRoles() {
    	Role roleSalesperson = new Role("Salesperson", "Manage product price, " + "customers , shipping, orders and sales report");
    	
    	Role roleEditor = new Role("Editor", "manage categories , brands, " + "products, articles and menus");
    	
    	
    	Role roleShipper = new Role("Shipper", "View product, view orders " + "and update order status");
    	
    	
    	Role roleAssistant = new Role("Assistant", "manage question and views");
    	
    	repo.saveAll(List.of(roleSalesperson, roleShipper, roleAssistant, roleEditor));
    }
}
