package com.hackathon.speediestapi.repository;

import com.hackathon.speediestapi.domain.ConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectionRepository extends JpaRepository<ConnectionEntity, UUID> {
    List<ConnectionEntity> findByName(String name);
}
