package com.solbeg.nmailservice.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.solbeg.nmailservice.model.deserializer.UserRequestDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonDeserialize(using = UserRequestDeserializer.class)
public class UserRequest {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
