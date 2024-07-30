package com.solbeg.nmailservice.model.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.solbeg.nmailservice.model.UserRequest;

import java.io.IOException;

public class UserRequestDeserializer extends JsonDeserializer<UserRequest> {
    @Override
    public UserRequest deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        String email = jsonNode.get("username").asText();
        String firstName = jsonNode.get("firstName").asText();
        String lastName = jsonNode.get("lastName").asText();
        String role = jsonNode.get("role").asText();
        Long id = jsonNode.get("id").asLong();
        return UserRequest.builder()
                .id(id)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .build();
    }
}
