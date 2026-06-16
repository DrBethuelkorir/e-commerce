package com.beviamy.dreamers.Repository;

import com.beviamy.dreamers.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

        List<Product> findByCategoryName(String category);
        List<Product> findByBrand(String brand);
        List<Product> findByCategoryNameAndBrand(String category, String brand);
        List<Product> findByProductNameAndBrand(String name, String brand);
        List<Product> findByProductName(String name);

    long countByBrandAndName(String brand, String name);
}
