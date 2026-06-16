package com.beviamy.dreamers.Repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.beviamy.dreamers.models.Category;
import com.beviamy.dreamers.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);

    Boolean existsByName(String name);
}

