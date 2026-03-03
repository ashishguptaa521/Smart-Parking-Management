package com.smartparking.mapper;

import com.smartparking.dto.UserDTO;
import com.smartparking.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
