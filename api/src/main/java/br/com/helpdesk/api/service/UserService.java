package br.com.helpdesk.api.service;

import br.com.helpdesk.api.mapper.UserMapper;
import br.com.helpdesk.api.repository.UserRepository;
import br.com.helpdesk.commons.models.exceptions.ResourceNotFoundException;
import br.com.helpdesk.commons.models.responses.UserResponse;
import lombok.RequiredArgsConstructor;
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
}
