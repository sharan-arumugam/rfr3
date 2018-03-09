package com.lti.rfr.service.dto;

import java.io.Serializable;
import java.util.List;

import com.lti.rfr.domain.Receiver;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false, of = { "psNumber" })
public class ReceiverDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String psNumber;
    private String appleMail;
    private String name;
    private List<String> groups;
    private List<String> selectedImts;
    private List<String> selectedImt1s;
    private List<String> selectedImt2s;

    private List<ImtDTO> selectTree;

    public ReceiverDTO() {
    }

    public ReceiverDTO(Receiver reciever) {
        id = reciever.getId();
        psNumber = reciever.getPsNumber();
        appleMail = reciever.getAppleMail();
        name = reciever.getName();
    }
}
