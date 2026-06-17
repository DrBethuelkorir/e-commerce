package com.beviamy.dreamers.Controllers;

import com.beviamy.dreamers.APIResonse;
import com.beviamy.dreamers.Request.CreateProductRequest;
import com.beviamy.dreamers.Request.UpdateProductRequest;
import com.beviamy.dreamers.exeption.ProductNotFoundExeption;
import com.beviamy.dreamers.models.Product;
import com.beviamy.dreamers.service.products.IProductService;
import com.beviamy.dreamers.service.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<APIResonse> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                return ResponseEntity.status(NO_CONTENT)
                        .body(new APIResonse("No products found", products.isEmpty()));
            }
            return ResponseEntity.ok(new APIResonse("Products found successfully", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error retrieving products: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResonse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new APIResonse("Product found successfully", product));
        } catch (ProductNotFoundExeption e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("Product not found with ID: " + id, null));
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<APIResonse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductByName(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new APIResonse("No products found with name: " + name, null));
            }
            return ResponseEntity.ok(new APIResonse("Products found successfully", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<APIResonse> getProductsByCategory(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductByCategory(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new APIResonse("No products found in category: " + name, null));
            }
            return ResponseEntity.ok(new APIResonse("Products found successfully", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/brand/{name}")
    public ResponseEntity<APIResonse> getProductsByBrand(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductByBrand(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new APIResonse("No products found for brand: " + name, null));
            }
            return ResponseEntity.ok(new APIResonse("Products found successfully", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/brand-category")
    public ResponseEntity<APIResonse> getProductsByBrandAndCategory(
            @RequestParam String brand,
            @RequestParam String category) {
        try {
            List<Product> products = productService.getProductByCategoryAndBrand(brand, category);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new APIResonse("No products found for brand: " + brand + " and category: " + category, null));
            }
            return ResponseEntity.ok(new APIResonse("Products found successfully", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/brand-name")
    public ResponseEntity<APIResonse> getProductsByBrandAndName(
            @RequestParam String brand,
            @RequestParam String name) {
        try {
            List<Product> products = productService.getProductByBrandAndName(brand, name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND)
                        .body(new APIResonse("No products found for brand: " + brand + " and name: " + name, null));
            }
            return ResponseEntity.ok(new APIResonse("Products found successfully", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error: " + e.getMessage(), null));
        }
    }

        @PostMapping
        public ResponseEntity<APIResonse> createProduct(@RequestBody CreateProductRequest product) {
            try {
                Product createdProduct = productService.addProduct(product);
                return ResponseEntity.status(CREATED)
                        .body(new APIResonse("Product created successfully", createdProduct));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(BAD_REQUEST)
                        .body(new APIResonse("Invalid product data: " + e.getMessage(), null));
            } catch (Exception e) {
                return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                        .body(new APIResonse("Error creating product: " + e.getMessage(), null));
            }
        }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResonse> updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest product) {
        try {
            Product updatedProduct = productService.updateProduct(product, id);
            return ResponseEntity.ok(new APIResonse("Product updated successfully", updatedProduct));
        } catch (ProductNotFoundExeption e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("Product not found with ID: " + id, null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error updating product: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResonse> deleteProductById(@PathVariable Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new APIResonse("Product deleted successfully", null));
        } catch (ProductNotFoundExeption e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new APIResonse("Product not found with ID: " + id, null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error deleting product: " + e.getMessage(), null));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<APIResonse> countProductsByBrandAndName(
            @RequestParam String brand,
            @RequestParam String name) {
        try {
            Long count = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new APIResonse("Product count retrieved successfully", count));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new APIResonse("Error counting products: " + e.getMessage(), null));
        }
    }
}