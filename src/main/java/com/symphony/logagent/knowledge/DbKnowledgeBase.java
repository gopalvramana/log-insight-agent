package com.symphony.logagent.knowledge;

import com.symphony.logagent.repository.ErrorKnowledgeRepository;

import java.util.Map;
import com.symphony.logagent.model.ErrorKnowledgeEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DbKnowledgeBase implements KnowledgeBase{

    private final ErrorKnowledgeRepository repo;

    public DbKnowledgeBase(ErrorKnowledgeRepository repo) {
        this.repo = repo;
    }

    @Override
    public Map<String, String> getKnownErrors() {
        return repo.findAll().stream()
                .collect(Collectors.toMap(
                        ErrorKnowledgeEntity::getErrorKey,
                        ErrorKnowledgeEntity::getDescription
                ));
    }

    @Override
    public Map<String, String> getKnownRemediations() {
        return repo.findAll().stream()
                .collect(Collectors.toMap(
                        ErrorKnowledgeEntity::getErrorKey,
                        ErrorKnowledgeEntity::getRemediation
                ));
    }
}
