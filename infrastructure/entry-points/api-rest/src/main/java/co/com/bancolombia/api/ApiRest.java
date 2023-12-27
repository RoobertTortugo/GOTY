package co.com.bancolombia.api;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.usecase.products.ProductsUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ApiRest {
    private final ProductsUseCase productsUseCase;
    @GetMapping("/goty/products")
    public List<Product> getAllProducts() {
        return productsUseCase.allProducts();
    }
    @GetMapping("/goty/similars/{id}")
    public List<Product> similarProducts(@PathVariable("id") int id) {
        return productsUseCase.getSimilarProduct(id);
    }
}
