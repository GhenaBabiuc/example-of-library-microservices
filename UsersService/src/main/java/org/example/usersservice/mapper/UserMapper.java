package org.example.usersservice.mapper;

import org.example.usersservice.model.User;
import org.example.usersservice.model.dto.CreateUserRequest;
import org.example.usersservice.model.dto.UpdateUserRequest;
import org.example.usersservice.model.dto.UserRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserRecord toRecord(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "membershipDate", ignore = true)
    User toEntity(CreateUserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "membershipDate", ignore = true)
    void updateEntity(UpdateUserRequest request, @MappingTarget User user);
}
