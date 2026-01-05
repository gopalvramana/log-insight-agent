package com.symphony.logagent.service;

import com.symphony.logagent.knowledge.KnowledgeBase;
import com.symphony.logagent.model.ErrorKnowledgeEntity;
import com.symphony.logagent.repository.ErrorKnowledgeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class KnowledgeService implements KnowledgeBase {

    private final ErrorKnowledgeRepository repository;

    public List<String> getKnownErrorPatterns(String errorKey){
        return repository.findByErrorKey(errorKey)
                .stream()
                .map(entity -> entity.getDescription())
                .toList();
    }

    public List<String> getKnownRemediations(String errorKey){
        return repository.findByErrorKey(errorKey)
                .stream()
                .map(entity -> entity.getRemediation())
                .toList();
    }


    @Override
    public Map<String, String> getKnownErrors() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(
                        ErrorKnowledgeEntity::getErrorKey,
                        ErrorKnowledgeEntity::getDescription
                ));
    }

    @Override
    public Map<String, String> getKnownRemediations() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(
                        ErrorKnowledgeEntity::getErrorKey,
                        ErrorKnowledgeEntity::getRemediation
                ));
    }



}
