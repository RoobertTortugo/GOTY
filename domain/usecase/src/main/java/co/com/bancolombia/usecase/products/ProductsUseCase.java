package co.com.bancolombia.usecase.products;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.products.gateways.ProductsRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductsUseCase {

    private final ProductsRepository productsRepository;

    public List<Product> allProducts() {
        return productsRepository.getAllProducts();
    }

    public List<Product> getSimilarProduct(int id) {
        return productsRepository.getProductsById(productsRepository.getSimilar(id));
    }
}
