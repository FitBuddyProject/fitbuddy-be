package com.fitbuddy.service.repository.fitness;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ActionTemplate {
    private final MongoTemplate template;
}
