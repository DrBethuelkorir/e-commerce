package com.beviamy.dreamers.service.products;

import com.beviamy.dreamers.Repository.CategoryRepository;
import com.beviamy.dreamers.Request.CreateProductRequest;
import com.beviamy.dreamers.Request.UpdateProductRequest;
import com.beviamy.dreamers.exeption.ProductNotFoundExeption;
import com.beviamy.dreamers.models.Category;
import com.beviamy.dreamers.models.Product;
import com.beviamy.dreamers.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(CreateProductRequest request) {
        // Find or create category
        Category category = categoryRepository.findByName(request.getCategory());

        if (category == null) {
            category = new Category();
            category.setName(request.getCategory());
            category = categoryRepository.save(category);
        }

        // Create product using setters
        Product product = new Product();
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setDescription(request.getDescription());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long id) {
        return this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundExeption("Product not found"));
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long id) {
        return productRepository.findById(id)
                .map(existingProduct -> updateAproduct(existingProduct,request))
                .map(productRepository :: save)
                .orElseThrow(() -> new ProductNotFoundExeption("Product not found"));
    }
    private Product updateAproduct(Product existingProduct,UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public void deleteProductById(Long id) {
    this.productRepository.findById(id).ifPresentOrElse(productRepository::delete,
            () -> {throw new ProductNotFoundExeption("Product not found");
    });
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return this.productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return this.productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return this.productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return this.productRepository.findByNameAndBrand(name,brand);
    }

    @Override
    public long countProductsByBrandAndName(String brand, String name) {
        return this.productRepository.countByBrandAndName(brand,name);
    }
}
