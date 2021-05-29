package com.hackathon.speediestapi.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class PeriodicTest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer period;
}
