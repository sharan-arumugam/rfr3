package com.lti.rfr.service.mapper;

import com.lti.rfr.domain.*;
import com.lti.rfr.service.dto.FunctionalGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FunctionalGroup and its DTO FunctionalGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FunctionalGroupMapper extends EntityMapper<FunctionalGroupDTO, FunctionalGroup> {


    @Mapping(target = "functionalGroups", ignore = true)
    FunctionalGroup toEntity(FunctionalGroupDTO functionalGroupDTO);

    default FunctionalGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        FunctionalGroup functionalGroup = new FunctionalGroup();
        functionalGroup.setId(id);
        return functionalGroup;
    }
}
