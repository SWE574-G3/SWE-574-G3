package com.communitter.api.mapper;

import com.communitter.api.dto.CommunityDto;
import com.communitter.api.model.Community;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommunityMapper {

    CommunityMapper INSTANCE = Mappers.getMapper(CommunityMapper.class);

    @Mapping(target = "isPublic", expression = "java(mapBoolean(community.isPublic()))")
    CommunityDto toDto(Community community);

    default Boolean mapBoolean(Boolean value) {
        return value != null ? value : false;
    }
}
