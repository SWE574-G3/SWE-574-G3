package com.communitter.api.mapper;

import com.communitter.api.dto.UserDto;
import com.communitter.api.dto.UserFollowDto;
import com.communitter.api.key.UserFollowKey;
import com.communitter.api.model.User;
import com.communitter.api.model.UserFollow;
import java.util.Date;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    UserFollowDto toUserFollowDto(UserFollow userFollow);
}