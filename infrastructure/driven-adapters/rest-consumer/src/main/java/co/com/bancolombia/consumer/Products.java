package co.com.bancolombia.consumer;

import co.com.bancolombia.model.product.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Products {
    private List<Product> products;
}
