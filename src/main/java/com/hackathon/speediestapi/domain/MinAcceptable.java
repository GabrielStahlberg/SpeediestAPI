package com.hackathon.speediestapi.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class MinAcceptable implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer downloadValue;
    private Integer uploadValue;
}
