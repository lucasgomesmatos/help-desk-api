package br.com.helpdesk.api.controller.impl;

import br.com.helpdesk.api.entity.User;
import br.com.helpdesk.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static br.com.helpdesk.api.creator.CreatorUtils.generateMock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

}