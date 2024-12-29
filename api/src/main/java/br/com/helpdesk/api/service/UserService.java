package br.com.helpdesk.api.service;

import br.com.helpdesk.api.entity.User;
import br.com.helpdesk.api.mapper.UserMapper;
import br.com.helpdesk.api.repository.UserRepository;
import br.com.helpdesk.commons.models.exceptions.ResourceNotFoundException;
import br.com.helpdesk.commons.models.requests.CreateUserRequest;
import br.com.helpdesk.commons.models.requests.UpdateUserRequest;
import br.com.helpdesk.commons.models.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder;

    public UserResponse findById(final String id) {
        return mapper.fromEntity(find(id));
    }

    public void save(CreateUserRequest request) {
        verifyIfEmailExists(request.email(), null);
        repository.save(
                mapper.fromRequest(request)
                        .withPassword(encoder.encode(request.password()))
        );
    }


    public List<UserResponse> findAll() {
        return repository.findAll().stream()
                .map(mapper::fromEntity)
                .toList();
    }

    public UserResponse update(final String id, final UpdateUserRequest request) {
        User entity = find(id);
        verifyIfEmailExists(request.email(), id);

        final var newEntity = mapper.update(request, entity)
                .withPassword(
                        request.password() != null
                                ? encoder.encode(request.password())
                                : entity.getPassword()
                );

        return mapper.fromEntity(repository.save(newEntity));
    }


    private void verifyIfEmailExists(final String email, final String id) {
        repository.findByEmail(email)
                .ifPresent(user -> {
                    if (!user.getId().equals(id)) {
                        throw new DataIntegrityViolationException("Email [ " + email + " ] already exists");
                    }
                });
    }

    private User find(final String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Object not found"
                        + id + " , Type: " + User.class.getSimpleName()
                ));
    }
}
