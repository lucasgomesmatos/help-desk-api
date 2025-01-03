package br.com.helpdesk.api.mapper;

import br.com.helpdesk.api.entity.User;
import br.com.helpdesk.commons.models.requests.CreateUserRequest;
import br.com.helpdesk.commons.models.requests.UpdateUserRequest;
import br.com.helpdesk.commons.models.responses.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserResponse fromEntity(final User entity);

    @Mapping(target = "id", ignore = true)
    User fromRequest(final CreateUserRequest createUserRequest);

    @Mapping(target = "id", ignore = true)
    User update(UpdateUserRequest updateUserRequest, @MappingTarget User entity);
}
