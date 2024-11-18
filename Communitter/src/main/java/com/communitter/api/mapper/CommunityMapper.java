package com.communitter.api.mapper;

import com.communitter.api.dto.CommunityDto;
import com.communitter.api.model.Community;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommunityMapper {

    CommunityMapper INSTANCE = Mappers.getMapper(CommunityMapper.class);

    CommunityDto toDto(Community community);
}
