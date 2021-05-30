package com.hackathon.speediestapi.service;

import com.hackathon.speediestapi.domain.ConnectionEntity;
import com.hackathon.speediestapi.domain.dto.ConnectionStatsDTO;
import com.hackathon.speediestapi.repository.ConnectionRepository;
import com.hackathon.speediestapi.util.UtilsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository repository;

    public ConnectionStatsDTO findConnectionStats() {
        List<ConnectionEntity> connections = repository.findByName("My_Connection");
        ConnectionStatsDTO connectionStats = new ConnectionStatsDTO();
        connectionStats.setLastTest(connections.get(connections.size() - 1).getDate());

        for(ConnectionEntity connection : connections) {
            connectionStats.getDownloadAverages().add(connection.getDownloadAverage());
            connectionStats.getUploadAverages().add(connection.getUploadAverage());
        }

        connectionStats.setDownloadGeneralAverage(UtilsImpl.calculateGeneralAverage(connectionStats.getDownloadAverages()));
        connectionStats.setUploadGeneralAverage(UtilsImpl.calculateGeneralAverage(connectionStats.getUploadAverages()));

        return connectionStats;
    }

    public ConnectionEntity createConnection(ConnectionEntity connection) {
        double downloadAvgRounded = UtilsImpl.getDoubleValueRounded(connection.getDownloadAverage().doubleValue(), 1);
        double uploadAvgRounded = UtilsImpl.getDoubleValueRounded(connection.getUploadAverage().doubleValue(), 1);

        try {
            Date date = UtilsImpl.dateFormatter(connection.getDate());
            connection.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        connection.setName("My_Connection");
        connection.setLocation("Araraquara-SP");
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
        return repository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    public Optional<ConnectionEntity> findById(UUID id) {
        return repository.findById(id);
    }
}
