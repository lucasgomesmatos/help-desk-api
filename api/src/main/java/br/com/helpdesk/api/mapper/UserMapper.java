package br.com.helpdesk.api.mapper;

import br.com.helpdesk.api.entity.User;
import br.com.helpdesk.commons.models.responses.UserResponse;
import org.mapstruct.Mapper;


@Mapper(
        componentModel = "spring",
        nullValueCheckStrategy = org.mapstruct.NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserResponse fromEntity(final User entity);
}
