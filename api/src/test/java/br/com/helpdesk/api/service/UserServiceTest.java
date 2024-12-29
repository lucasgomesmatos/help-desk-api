package br.com.helpdesk.api.service;

import br.com.helpdesk.api.entity.User;
import br.com.helpdesk.api.mapper.UserMapper;
import br.com.helpdesk.api.repository.UserRepository;
import br.com.helpdesk.commons.models.exceptions.ResourceNotFoundException;
import br.com.helpdesk.commons.models.responses.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private BCryptPasswordEncoder encoder;


    @Test
    void whenCallFindByIdWithValidIdThenReturnUser() {
        when(repository.findById(anyString())).thenReturn(Optional.of(new User()));
        when(mapper.fromEntity(any(User.class))).thenReturn(mock(UserResponse.class));

        final var response = service.findById("1");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(UserResponse.class, response.getClass());

        verify(repository, times(1)).findById(anyString());
        verify(mapper, times(1)).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindByIdWithInvalidIdThenThrowResourceNotFoundException() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        final var response = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById("1"));

        Assertions.assertEquals(ResourceNotFoundException.class, response.getClass());
        Assertions.assertEquals("Object not found 1, Type: User", response.getMessage());

        verify(repository, times(1)).findById(anyString());
        verify(mapper, never()).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindAllThenReturnListOfUsers() {
        when(repository.findAll()).thenReturn(List.of(new User(), new User()));
        when(mapper.fromEntity(any(User.class))).thenReturn(mock(UserResponse.class));

        final var response = service.findAll();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(UserResponse.class, response.getFirst().getClass());

        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).fromEntity(any(User.class));
    }

}