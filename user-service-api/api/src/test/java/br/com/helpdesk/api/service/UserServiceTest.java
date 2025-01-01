package br.com.helpdesk.api.service;

import br.com.helpdesk.api.entity.User;
import br.com.helpdesk.api.mapper.UserMapper;
import br.com.helpdesk.api.repository.UserRepository;
import br.com.helpdesk.commons.models.exceptions.ResourceNotFoundException;
import br.com.helpdesk.commons.models.requests.CreateUserRequest;
import br.com.helpdesk.commons.models.requests.UpdateUserRequest;
import br.com.helpdesk.commons.models.responses.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static br.com.helpdesk.api.creator.CreatorUtils.generateMock;
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
        when(mapper.fromEntity(any(User.class))).thenReturn(generateMock(UserResponse.class));

        final var response = service.findById("1");

        Assertions.assertNotNull(response);
        Assertions.assertEquals(UserResponse.class, response.getClass());

        verify(repository).findById(anyString());
        verify(mapper).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindByIdWithInvalidIdThenThrowResourceNotFoundException() {
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        final var response = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.findById("1"));

        Assertions.assertEquals(ResourceNotFoundException.class, response.getClass());
        Assertions.assertEquals("Object not found 1, Type: User", response.getMessage());

        verify(repository).findById(anyString());
        verify(mapper, never()).fromEntity(any(User.class));
    }

    @Test
    void whenCallFindAllThenReturnListOfUsers() {
        when(repository.findAll()).thenReturn(List.of(new User(), new User()));
        when(mapper.fromEntity(any(User.class))).thenReturn(generateMock(UserResponse.class));

        final var response = service.findAll();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(UserResponse.class, response.getFirst().getClass());

        verify(repository).findAll();
        verify(mapper, times(2)).fromEntity(any(User.class));
    }

    @Test
    void whenCallSaveThenSuccess() {
        final var request = generateMock(CreateUserRequest.class);

        when(mapper.fromRequest(any())).thenReturn(new User());
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(repository.save(any(User.class))).thenReturn(new User());
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        service.save(request);

        verify(mapper).fromRequest(request);
        verify(encoder).encode(request.password());
        verify(repository).save(any(User.class));
        verify(repository).findByEmail(request.email());
    }

    @Test
    void whenCallSaveWithExistingEmailThenThrowDataIntegrityViolationException() {
        final var request = generateMock(CreateUserRequest.class);
        final var entity = generateMock(User.class);

        when(repository.findByEmail(request.email())).thenReturn(Optional.of(entity));

        final var response = Assertions.assertThrows(DataIntegrityViolationException.class, () -> service.save(request));

        Assertions.assertEquals(DataIntegrityViolationException.class, response.getClass());
        Assertions.assertEquals("Email [ " + request.email() + " ] already exists", response.getMessage());

        verify(repository).findByEmail(request.email());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void whenCallUpdateThenSuccess() {
        final var id = "1";
        final var request = generateMock(UpdateUserRequest.class);
        final var entity = generateMock(User.class).withId(id);

        when(repository.findById(anyString())).thenReturn(Optional.of(entity));
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(mapper.update(any(UpdateUserRequest.class), any(User.class))).thenReturn(new User());
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(repository.save(any(User.class))).thenReturn(new User());


        service.update(id, request);

        verify(repository).findById(id);
        verify(mapper).update(request, entity);
        verify(encoder).encode(request.password());
        verify(repository).save(any(User.class));
        verify(repository).findByEmail(request.email());
    }

    @Test
    void whenCallUpdateWithInvalidIdThenThrowResourceNotFoundException() {
        final var request = generateMock(UpdateUserRequest.class);

        when(repository.findById(anyString())).thenReturn(Optional.empty());

        final var response = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update("1", request));

        Assertions.assertEquals(ResourceNotFoundException.class, response.getClass());
        Assertions.assertEquals("Object not found 1, Type: User", response.getMessage());

        verify(repository).findById("1");
        verify(mapper, never()).update(any(UpdateUserRequest.class), any(User.class));
        verify(repository, never()).save(any(User.class));
        verify(repository, never()).findByEmail(anyString());
    }

    @Test
    void whenCallUpdateWithExistingEmailThenThrowDataIntegrityViolationException() {
        final var request = generateMock(UpdateUserRequest.class);
        final var entity = generateMock(User.class);

        when(repository.findById(anyString())).thenReturn(Optional.of(entity));
        when(repository.findByEmail(request.email())).thenReturn(Optional.of(entity));

        final var response = Assertions.assertThrows(DataIntegrityViolationException.class, () -> service.update("1", request));

        Assertions.assertEquals(DataIntegrityViolationException.class, response.getClass());
        Assertions.assertEquals("Email [ " + request.email() + " ] already exists", response.getMessage());

        verify(repository).findById("1");
        verify(repository).findByEmail(request.email());
        verify(mapper, never()).update(any(UpdateUserRequest.class), any(User.class));
        verify(repository, never()).save(any(User.class));
    }

}