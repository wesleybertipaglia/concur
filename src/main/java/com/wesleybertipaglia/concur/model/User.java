package com.wesleybertipaglia.concur.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotEmpty(message = "Name is required")
    private String name;

    @Indexed(unique = true)
    @NotEmpty(message = "Username is required")
    private String username;

    @Indexed(unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
