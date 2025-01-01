package br.com.helpdesk.api.controller.impl;

import br.com.helpdesk.api.entity.User;
import br.com.helpdesk.api.repository.UserRepository;
import br.com.helpdesk.commons.models.requests.CreateUserRequest;
import br.com.helpdesk.commons.models.requests.UpdateUserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static br.com.helpdesk.api.creator.CreatorUtils.generateMock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Test
    void findFindByIdUserWithSuccess() throws Exception {
        final var entity = generateMock(User.class);
        final var userId = repository.save(entity).getId();

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(entity.getName()))
                .andExpect(jsonPath("$.email").value(entity.getEmail()))
                .andExpect(jsonPath("$.password").value(entity.getPassword()))
                .andExpect(jsonPath("$.profiles").isArray())
        ;

        repository.deleteById(userId);
    }


    @Test
    void findFindByIdUserWithNotFound() throws Exception {

        mockMvc.perform(get("/api/users/{id}", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Object not found 1, Type: User"))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.path").value("/api/users/1"))
        ;

    }

    @Test
    void findAllUsersWithSuccess() throws Exception {
        final var entity1 = generateMock(User.class);
        final var entity2 = generateMock(User.class);
        repository.saveAll(List.of(entity1, entity2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[1]").isNotEmpty())
                .andExpect(jsonPath("$[0].profiles").isArray())
        ;

        repository.deleteAll(List.of(entity1, entity2));
    }

    @Test
    void saveUserWithSuccess() throws Exception {
        final var validEmail = UUID.randomUUID() + "@mail.com";
        final var request = generateMock(CreateUserRequest.class)
                .withEmail(validEmail);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isCreated());

        repository.delete(repository.findByEmail(validEmail).get());
    }

    @Test
    void saveUserWithConflict() throws Exception {
        final var validEmail = "teste@mail.com";
        final var entity = generateMock(User.class)
                .withEmail(validEmail);

        repository.save(entity);

        final var request = generateMock(CreateUserRequest.class)
                .withEmail(entity.getEmail());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email [ " + entity.getEmail() + " ] already exists"))
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.CONFLICT.getReasonPhrase()))
                .andExpect(jsonPath("$.path").value("/api/users"))
        ;

        repository.delete(entity);
    }

    @Test
    void saveUserWithNameNullThenBadRequest() throws Exception {
        final var validEmail = "teste@mail.com";
        final var request = generateMock(CreateUserRequest.class)
                .withEmail(validEmail)
                .withName("");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Exception in validation atributtes"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.error").value("Validation Exception"))
                .andExpect(jsonPath("$.path").value("/api/users"))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'name' && @.message == 'Name must contain between 3 and 50 characters')]").exists())
        ;
    }

    @Test
    void saveUserWithEmailNullThenBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class)
                .withEmail("");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Exception in validation atributtes"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.error").value("Validation Exception"))
                .andExpect(jsonPath("$.path").value("/api/users"))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'email' && @.message == 'Email is required')]").exists())
        ;
    }

    @Test
    void saveUserWithInvalidEmailThenBadRequest() throws Exception {
        final var request = generateMock(CreateUserRequest.class)
                .withEmail("invalid");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Exception in validation atributtes"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.error").value("Validation Exception"))
                .andExpect(jsonPath("$.path").value("/api/users"))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'email' && @.message == 'Invalid email')]").exists())
        ;
    }

    @Test
    void saveUserWithPasswordNullThenBadRequest() throws Exception {
        final var validEmail = "teste@mail.com";
        final var request = generateMock(CreateUserRequest.class)
                .withEmail(validEmail)
                .withPassword("");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Exception in validation atributtes"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.error").value("Validation Exception"))
                .andExpect(jsonPath("$.path").value("/api/users"))
                .andExpect(jsonPath("$.errors[?(@.fieldName == 'password' " +
                        "&& @.message == 'Password must contain between 6 and 50 characters')]").exists())
        ;
    }

    @Test
    void updateWithSuccess() throws Exception {
        final var validEmail = UUID.randomUUID() + "@mail.com";
        final var entity = generateMock(User.class)
                .withEmail(validEmail);
        final var userId = repository.save(entity).getId();

        final var request = generateMock(CreateUserRequest.class)
                .withEmail(validEmail);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.email").value(request.email()))
                .andExpect(jsonPath("$.profiles").isArray())
        ;

        repository.delete(entity);
    }

    @Test
    void updateWithConflict() throws Exception {
        final var validEmail = "testeUpdate@mail.com";

        final var entity1 = generateMock(User.class)
                .withEmail(validEmail);

        repository.save(entity1);

        final var entity2 = generateMock(User.class)
                .withEmail(UUID.randomUUID() + "@mail.com");

        final var userId = repository.save(entity2).getId();


        final var request = generateMock(UpdateUserRequest.class)
                .withEmail(validEmail);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email [ " + validEmail + " ] already exists"))
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.CONFLICT.getReasonPhrase()))
                .andExpect(jsonPath("$.path").value("/api/users/" + userId))
        ;

        repository.delete(entity1);
        repository.delete(entity2);
    }

    @Test
    void updateWithNotFound() throws Exception {
        final var validEmail = UUID.randomUUID() + "@mail.com";
        final var request = generateMock(UpdateUserRequest.class)
                .withEmail(validEmail);

        mockMvc.perform(put("/api/users/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Object not found 1, Type: User"))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.path").value("/api/users/1"))
        ;
    }

    private static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}