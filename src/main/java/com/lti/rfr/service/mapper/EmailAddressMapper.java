package com.lti.rfr.service.mapper;

import com.lti.rfr.domain.*;
import com.lti.rfr.service.dto.EmailAddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity EmailAddress and its DTO EmailAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {FunctionalGroupMapper.class})
public interface EmailAddressMapper extends EntityMapper<EmailAddressDTO, EmailAddress> {

    @Mapping(source = "psNumber.id", target = "psNumberId")
    EmailAddressDTO toDto(EmailAddress emailAddress);

    @Mapping(source = "psNumberId", target = "psNumber")
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
