    package com.beviamy.dreamers.service.products;

    import com.beviamy.dreamers.Request.CreateProductRequest;
    import com.beviamy.dreamers.Request.UpdateProductRequest;
    import com.beviamy.dreamers.models.Product;

    import java.util.List;

    public interface IProductService {
        Product addProduct(CreateProductRequest product);
        Product getProductById(Long id);
        Product updateProduct(UpdateProductRequest product, Long id);
        void deleteProductById(Long id);
        List<Product> getAllProducts();
        List<Product> getProductByName(String name);
        List<Product> getProductByCategory(String category);
        List<Product> getProductByBrand(String brand);
        List<Product> getProductByCategoryAndBrand(String category, String brand);
        List<Product> getProductByBrandAndName(String brand, String name);
        long countProductsByBrandAndName(String brand,String name);


    }
