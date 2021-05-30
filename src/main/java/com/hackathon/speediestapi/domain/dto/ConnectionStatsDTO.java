package com.hackathon.speediestapi.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ConnectionStatsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Double> downloadAverages = new ArrayList<>();
    private List<Double> uploadAverages = new ArrayList<>();
    private Double downloadGeneralAverage;
    private Double uploadGeneralAverage;
    private Date lastTest;
}
