package br.com.helpdesk.api.controller.impl;

import br.com.helpdesk.api.controller.UserController;
import br.com.helpdesk.api.entity.User;
import br.com.helpdesk.api.service.UserService;
import br.com.helpdesk.commons.models.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> findById(String id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }
}
