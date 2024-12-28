package br.com.helpdesk.api.service;

import br.com.helpdesk.api.mapper.UserMapper;
import br.com.helpdesk.api.repository.UserRepository;
import br.com.helpdesk.commons.models.exceptions.ResourceNotFoundException;
import br.com.helpdesk.commons.models.requests.CreateUserRequest;
import br.com.helpdesk.commons.models.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserResponse findById(final String id) {
        return userMapper.fromEntity(
                userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Object not found"
                                + id + " , Type: " + UserResponse.class.getSimpleName()
                        ))
        );
    }

    public void save(CreateUserRequest createUserRequest) {
        verifyIfEmailExists(createUserRequest.email(), null);
        userRepository.save(userMapper.fromRequest(createUserRequest));
    }

    private void verifyIfEmailExists(final String email, final String id) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    if (!user.getId().equals(id)) {
                        throw new DataIntegrityViolationException("Email [ " + email + " ] already exists");
                    }
                });
    }
}
