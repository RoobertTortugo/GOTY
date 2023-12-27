package co.com.bancolombia.model.products.gateways;

import co.com.bancolombia.model.product.Product;

import java.util.List;

public interface ProductsRepository {
    List<Product> getAllProducts();

    List<Integer> getSimilar(int id);
    List<Product> getProductsById(List<Integer> ids);
}
