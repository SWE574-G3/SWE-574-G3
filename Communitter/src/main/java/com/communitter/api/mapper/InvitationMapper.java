package com.communitter.api.mapper;

import com.communitter.api.dto.InvitationDto;
import com.communitter.api.model.Invitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    InvitationMapper INSTANCE = Mappers.getMapper(InvitationMapper.class);

    InvitationDto toDto(Invitation invitation);
}
