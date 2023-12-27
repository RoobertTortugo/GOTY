package co.com.bancolombia.consumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ResponseProduct {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("url")
    private String url;

    @JsonProperty("price")
    private int price;

}