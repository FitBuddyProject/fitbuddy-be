package com.fitbuddy.service.repository.user;

import com.fitbuddy.service.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserTemplate {
    private final MongoTemplate mongoTemplate;

    public boolean isDuplicated(String phone) {
        Query query = Query.query(Criteria.where("phone").is(phone));
        return mongoTemplate.exists(query, User.class);
    }
}
