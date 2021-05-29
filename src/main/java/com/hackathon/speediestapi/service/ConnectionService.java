package com.hackathon.speediestapi.service;

import com.hackathon.speediestapi.domain.ConnectionEntity;
import com.hackathon.speediestapi.repository.ConnectionRepository;
import com.hackathon.speediestapi.util.UtilsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository repository;

    public ConnectionEntity createConnection(ConnectionEntity connection) {
        double downloadAvgRounded = getDoubleValueRounded(connection.getDownloadAverage().doubleValue(), 1);
        double uploadAvgRounded = getDoubleValueRounded(connection.getUploadAverage().doubleValue(), 1);

        connection.setDownloadAverage(Double.valueOf(downloadAvgRounded));
        connection.setUploadAverage(Double.valueOf(uploadAvgRounded));
        return repository.save(connection);
    }

    public void createScheduledConnection() {
        ConnectionEntity connectionEntity = new ConnectionEntity();
        connectionEntity.setName("My_Connection");
        connectionEntity.setLocation("Araraquara-SP");
        connectionEntity.setDurationMinutes(UtilsImpl.generateRandomInteger(5, 1000));
        connectionEntity.setDownloadAverage(UtilsImpl.generateRandomInteger(60, 100) + (UtilsImpl.generateRandomInteger(0, 10) / 10.0));
        connectionEntity.setUploadAverage(UtilsImpl.generateRandomInteger(15, 50) + (UtilsImpl.generateRandomInteger(0, 10) / 10.0));
        connectionEntity.setDate(new Date());
        repository.save(connectionEntity);
    }

    public List<ConnectionEntity> findAll() {
        return repository.findAll();
    }

    public Optional<ConnectionEntity> findById(UUID id) {
        return repository.findById(id);
    }

    private double getDoubleValueRounded(double value, int scale) {
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }
}
