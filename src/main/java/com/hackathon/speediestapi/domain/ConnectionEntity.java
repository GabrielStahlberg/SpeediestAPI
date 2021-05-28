package com.hackathon.speediestapi.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Entity(name = "tb_connection")
public class ConnectionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "connection_id")
    private UUID id;

    @Column(name = "connection_name")
    @NotBlank
    private String name;

    @Column(name = "connection_download_average")
    @NotNull
    private Double downloadAverage;

    @Column(name = "connection_upload_average")
    @NotNull
    private Double uploadAverage;

    @Column(name = "connection_date")
    private Date date = new Date();
}
