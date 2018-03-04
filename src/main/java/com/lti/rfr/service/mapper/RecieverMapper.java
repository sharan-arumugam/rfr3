package com.lti.rfr.service.mapper;

import com.lti.rfr.domain.*;
import com.lti.rfr.service.dto.RecieverDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Reciever and its DTO RecieverDTO.
 */
@Mapper(componentModel = "spring", uses = {FunctionalGroupMapper.class})
public interface RecieverMapper extends EntityMapper<RecieverDTO, Reciever> {

    @Mapping(source = "group.id", target = "groupId")
    RecieverDTO toDto(Reciever reciever);

    @Mapping(source = "groupId", target = "group")
    Reciever toEntity(RecieverDTO recieverDTO);

    default Reciever fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reciever reciever = new Reciever();
        reciever.setId(id);
        return reciever;
    }
}
