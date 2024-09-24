package co.com.bancolombia.consumer;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.products.gateways.ProductsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class RestConsumer implements ProductsRepository {
    private static final String URL_PRODUCTS = "http://localhost:3100/api/products";
    private final Map<Integer, ResponseProduct> cache = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(RestConsumer.class.getName());

    @Override
    public List<Product> getAllProducts() {
        try {
            HttpResponse<String> response = configureConnection(URL_PRODUCTS);
            ObjectMapper objectMapper = new ObjectMapper();
            List<ResponseProduct> responseProductList = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ResponseProduct.class));
            return mapperProductlist(responseProductList);

        } catch (Exception e) {
            logger.info("Products not found");
            return new ArrayList<>();
        }
    }

    @Override
    public List<Product> getProductsById(List<Integer> ids) {
        List<CompletableFuture<ResponseProduct>> futures = ids.parallelStream()
                .map(productId -> CompletableFuture.supplyAsync(() -> fetchData(productId)))
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        return allOf.thenApply(v ->
                futures.parallelStream()
                        .map(CompletableFuture::join)
                        .map(responseProductOrigin ->
                                new ModelMapper().map(responseProductOrigin, Product.class))
                        .filter(product -> product.getId() != 0)
                        .collect(Collectors.toList())).join();
    }

    @Override
    public List<Integer> getSimilar(int id) {
        try {
            HttpResponse<String> response =
                    configureConnection("http://localhost:3100/api/product/" + id + "/similars");
            if (response.statusCode() == 404) {
                logger.info("Similar products with id " + id + " not found");
                return new ArrayList<>();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            List<Integer> productIds = objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
            return (productIds != null) ? productIds : new ArrayList<>();

        } catch (Exception e) {
            logger.info("Similar products with id " + id + " not found");
            return new ArrayList<>();
        }
    }


    private ResponseProduct fetchData(Integer productId) {
        if (cache.containsKey(productId)) {
            return cache.get(productId);
        }
        try {
            HttpResponse<String> response = configureConnection("http://localhost:3100/api/product/" + productId);
            if (response.statusCode() == 404) {
                logger.info("Product with id " + productId + " not found");
                return new ResponseProduct();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseProduct responseData = objectMapper.readValue(response.body(), ResponseProduct.class);

            if (responseData.getId() != 0) {
                cache.put(productId, responseData);
                return responseData;
            }
            return new ResponseProduct();

        } catch (Exception e) {
            logger.info("Product with id " + productId + " not found");
            return new ResponseProduct();
        }
    }

    private HttpResponse<String> configureConnection(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private List<Product> mapperProductlist(List<ResponseProduct> responseProductList) {
        ModelMapper modelMapper = new ModelMapper();
        return responseProductList.stream().map(responseProductOrigin ->
                        modelMapper.map(responseProductOrigin, Product.class))
                .collect(Collectors.toList());
    }
}
