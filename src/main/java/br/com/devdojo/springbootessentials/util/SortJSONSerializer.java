package br.com.devdojo.springbootessentials.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.Sort;

import java.io.IOException;

@JsonComponent
public class SortJSONSerializer extends JsonSerializer<Sort> {

    @Override
    public void serialize(Sort value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartArray();

        value.iterator().forEachRemaining(v -> {
            try {
                gen.writeObject(v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gen.writeEndArray();
    }

    @Override
    public Class<Sort> handledType() {
        return Sort.class;
    }

}
