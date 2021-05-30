package com.hackathon.speediestapi.service;

import com.hackathon.speediestapi.domain.ConnectionEntity;
import com.hackathon.speediestapi.domain.dto.ConnectionStatsDTO;
import com.hackathon.speediestapi.repository.ConnectionRepository;
import com.hackathon.speediestapi.util.UtilsImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

        minAcceptableValidation(connection.getDownloadAverage(), connection.getUploadAverage());
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

        minAcceptableValidation(connectionEntity.getDownloadAverage(), connectionEntity.getUploadAverage());
        repository.save(connectionEntity);
    }

    public List<ConnectionEntity> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    public Optional<ConnectionEntity> findById(UUID id) {
        return repository.findById(id);
    }

    private void minAcceptableValidation(Double downloadAverage, Double uploadAverage) {
        if(downloadAverage < UtilsImpl.downloadMinAcceptable) {
            sendNotification("Média Download", "A média de Download está abaixo do que é esperada.");
        }

        if(uploadAverage < UtilsImpl.uploadMinAcceptable) {
            sendNotification("Média Upload", "A média de Upload está abaixo do que é esperada.");
        }
    }

    private void sendNotification(String title, String message) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "key=AAAATdtW4yY:APA91bGHuP6AFV2UPLKuPE1g-cCprUZASBwynz8O0iU9J93BhphDyKRT1x43ceg5DRJCc_VkC2fPfX39Z40ndqZmSSxvw-ICRYst91fNGQWXfnulJ-_z4-pmbFoOFRLCTd7f9Byn7PfQ");

        String body = jsonBuilderForNotification(title, message);
        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        String response = restTemplate.postForObject("https://fcm.googleapis.com/fcm/send", request, String.class);
    }

    private String jsonBuilderForNotification(String title, String message) {
        JSONObject body = new JSONObject();
        JSONObject notification = new JSONObject();

        try {
            notification.put("body", message);
            notification.put("title", title);
            body.put("notification", notification);
            body.put("priority", "high");
            body.put("to", "/topics/all");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return body.toString();
    }
}
