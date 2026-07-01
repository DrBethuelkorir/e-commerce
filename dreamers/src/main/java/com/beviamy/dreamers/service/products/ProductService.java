package com.beviamy.dreamers.service.products;

import com.beviamy.dreamers.Repository.CategoryRepository;
import com.beviamy.dreamers.Repository.ProductRepository;
import com.beviamy.dreamers.Request.CreateProductRequest;
import com.beviamy.dreamers.Request.UpdateProductRequest;
import com.beviamy.dreamers.exeption.ProductNotFoundExeption;
import com.beviamy.dreamers.models.Category;
import com.beviamy.dreamers.models.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(CreateProductRequest request) {
        // 1. Find or create category
        Category category = categoryRepository.findByName(request.getCategory());
        if (category == null) {
            category = new Category();
            category.setName(request.getCategory());
            category = categoryRepository.save(category);
        }

        // 2. Check if product exists (by name, brand, price, category)
        Optional<Product> existingProduct = productRepository.findByNameAndBrandAndPriceAndCategory(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                category
        );

        if (existingProduct.isPresent()) {
            // 3. Product exists - update quantity
            Product product = existingProduct.get();
            product.setQuantity(product.getQuantity() + request.getQuantity());
            return productRepository.save(product);
        } else {
            // 4. Product doesn't exist - create new
            Product product = new Product();
            product.setName(request.getName());
            product.setBrand(request.getBrand());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            product.setDescription(request.getDescription());
            product.setCategory(category);
            return productRepository.save(product);
        }
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundExeption("Product not found"));
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long id) {
        return productRepository.findById(id)
                .map(existingProduct -> updateAproduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundExeption("Product not found"));
    }

    private Product updateAproduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(BigDecimal.valueOf(request.getPrice()));
        existingProduct.setQuantity(request.getQuantity());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory());
        if (category != null) {
            existingProduct.setCategory(category);
        }
        return existingProduct;
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(
                productRepository::delete,
                () -> { throw new ProductNotFoundExeption("Product not found"); }
        );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByNameAndBrand(name, brand);
    }

    @Override
    public long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}