package com.symphony.logagent.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "error_knowledge")
@Getter
@Setter
@Data
public class ErrorKnowledgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String errorKey;

    @Column(length = 2000)
    private String description;

    @Column(length = 2000)
    private String remediation;
}
