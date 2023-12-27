package co.com.bancolombia.usecase.products;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.products.gateways.ProductsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductUseCaseTest {
    private ProductsUseCase productsUseCase;
    private ProductsRepository productsRepository;

    private List<Integer> ids;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        productsRepository = mock(ProductsRepository.class);
        productsUseCase = new ProductsUseCase(productsRepository);
        ids = List.of(1, 2, 3);
        products = List.of(
                Product.builder().id(1).name("Product 1").build(),
                Product.builder().id(2).name("Product 2").build(),
                Product.builder().id(3).name("Product 3").build()
        );
    }

    @Test
    void allProducts() {
        when(productsRepository.getAllProducts()).thenReturn(products);
        var response = productsUseCase.allProducts();
        List<Product> products = productsUseCase.allProducts();
        Assertions.assertEquals(3, response.size());
        Assertions.assertEquals("Product 1", response.get(0).getName());
    }

    @Test
    void getSimilarProduct() {
        when(productsRepository.getSimilar(1)).thenReturn(ids);
        when(productsRepository.getProductsById(ids)).thenReturn(products);
        var response = productsUseCase.getSimilarProduct(1);
        Assertions.assertEquals(3, response.size());
        Assertions.assertEquals("Product 1", response.get(0).getName());
    }
}
