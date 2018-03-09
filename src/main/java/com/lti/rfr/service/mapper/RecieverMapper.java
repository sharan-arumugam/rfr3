package com.lti.rfr.service.mapper;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lti.rfr.domain.Receiver;
import com.lti.rfr.service.dto.ImtDTO;
import com.lti.rfr.service.dto.ReceiverDTO;
import com.lti.rfr.web.rest.FunctionalGroupResource;

/**
 * Mapper for the entity Reciever and its DTO RecieverDTO.
 */
@Service
public class RecieverMapper {

    ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(RecieverMapper.class);

    @Autowired
    FunctionalGroupResource groupResource;

    Function<List<String>, String> serialize = list -> {
        if (isNotEmpty(list)) {
            try {
                return mapper.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return "[]";
    };

    @SuppressWarnings("unchecked")
    Function<String, List<String>> deserialize = string -> {
        if (isNotBlank(string)) {
            try {
                return mapper.readValue(string, List.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<String>();
    };

    public ReceiverDTO toDto(Receiver reciever) {

        ReceiverDTO receiverDTO = new ReceiverDTO(reciever);
        receiverDTO.setGroups(deserialize.apply(reciever.getGroups()));
        receiverDTO.setSelectedImts(deserialize.apply(reciever.getSelectedImts()));
        receiverDTO.setSelectedImt1s(deserialize.apply(reciever.getSelectedImt1s()));
        receiverDTO.setSelectedImt2s(deserialize.apply(reciever.getSelectedImt2s()));

        List<ImtDTO> selectTree;
        try {
            selectTree = groupResource.getAllFunctionalGroupMaster(receiverDTO);
            log.info("selectTree:: " + selectTree
                    .stream()
                    .map(ImtDTO::isSelected)
                    .collect(toList()));

            receiverDTO.setSelectTree(selectTree);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return receiverDTO;
    }

    public Receiver toEntity(ReceiverDTO recieverDTO) {

        Receiver recieverEntity = new Receiver(recieverDTO);
        recieverEntity.setGroups(serialize.apply(recieverDTO.getGroups()));
        recieverEntity.setSelectedImts(serialize.apply(recieverDTO.getSelectedImts()));
        recieverEntity.setSelectedImt1s(serialize.apply(recieverDTO.getSelectedImt1s()));
        recieverEntity.setSelectedImt2s(serialize.apply(recieverDTO.getSelectedImt2s()));

        return recieverEntity;
    }

}
