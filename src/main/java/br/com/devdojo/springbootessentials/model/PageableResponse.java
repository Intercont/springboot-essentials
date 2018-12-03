package br.com.devdojo.springbootessentials.model;

import br.com.devdojo.springbootessentials.util.CustomSortDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para Wrappear o content da Paginação do Spring content:[]
 * @param <T>
 */
public class PageableResponse<T> extends PageImpl<T> {

    public PageableResponse(@JsonProperty("content") List<T> content,
                            @JsonProperty("number") int page,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") long totalElements,
                            @JsonProperty("pageable") JsonNode pageable,
                            @JsonProperty("last") boolean last,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("sort") @JsonDeserialize(using = CustomSortDeserializer.class) Sort sort,
                            @JsonProperty("first") boolean first,
                            @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, new PageRequest(page, size, sort), totalElements);
    }

    public PageableResponse(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    //para o parser do JSON utilizar
    public PageableResponse() {
        super(new ArrayList());
    }

    public PageableResponse(List<T> content) {
        super(content);
    }

}