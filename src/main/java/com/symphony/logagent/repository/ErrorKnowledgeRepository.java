package com.symphony.logagent.repository;

import com.symphony.logagent.model.ErrorKnowledgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ErrorKnowledgeRepository extends JpaRepository<ErrorKnowledgeEntity, Long> {
    List<ErrorKnowledgeEntity> findAll();
}
