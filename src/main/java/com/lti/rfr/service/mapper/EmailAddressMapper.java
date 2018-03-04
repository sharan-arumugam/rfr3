package com.lti.rfr.service.mapper;

import com.lti.rfr.domain.*;
import com.lti.rfr.service.dto.EmailAddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EmailAddress and its DTO EmailAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {FunctionalGroupMapper.class})
public interface EmailAddressMapper extends EntityMapper<EmailAddressDTO, EmailAddress> {

    @Mapping(source = "group.id", target = "groupId")
    EmailAddressDTO toDto(EmailAddress emailAddress);

    @Mapping(source = "groupId", target = "group")
    EmailAddress toEntity(EmailAddressDTO emailAddressDTO);

    default EmailAddress fromId(Long id) {
        if (id == null) {
            return null;
        }
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setId(id);
        return emailAddress;
    }
}
