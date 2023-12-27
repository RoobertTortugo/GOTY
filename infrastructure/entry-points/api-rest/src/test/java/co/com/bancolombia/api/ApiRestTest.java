package co.com.bancolombia.api;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.products.ProductsUseCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiRestTest {

    private ProductsUseCase productsUseCase;
    private ApiRest apiRest;
    List<Product> products;

    @BeforeEach
    void setUp() {
        productsUseCase = mock(ProductsUseCase.class);
        apiRest = new ApiRest(productsUseCase);
        products = List.of(
                Product.builder().id(1).name("product1").price(100).build(),
                Product.builder().id(2).name("product2").price(200).build(),
                Product.builder().id(3).name("product3").price(300).build()
        );
    }

    @Test
    void apiRestAllProductsTest() {
        when(productsUseCase.allProducts()).thenReturn(products);
        var response = apiRest.getAllProducts();
        assertEquals(3, response.size());
        assertEquals("product1", response.get(0).getName());
    }

    @Test
    void apiRestSimilarTest() {
        when(productsUseCase.getSimilarProduct(anyInt())).thenReturn(products);
        var response = apiRest.similarProducts(1);
        assertEquals(3, response.size());
        assertEquals("product2", response.get(1).getName());
    }
}
