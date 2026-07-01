package com.beviamy.dreamers.Repository;

import com.beviamy.dreamers.models.Category;
import com.beviamy.dreamers.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

        List<Product> findByCategoryName(String category);
        List<Product> findByBrand(String brand);
        List<Product> findByCategoryNameAndBrand(String category, String brand);
        List<Product> findByNameAndBrand(String name, String brand);
        List<Product> findByName(String name);

    long countByBrandAndName(String brand, String name);

    Optional<Product> findByNameAndBrandAndPriceAndCategory(String name, String brand, BigDecimal price, Category category);
}
