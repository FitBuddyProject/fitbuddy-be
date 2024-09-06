package com.fitbuddy.service.repository.action;

import com.fitbuddy.service.repository.dto.request.ActionRequest;
import com.fitbuddy.service.repository.entity.Action;
import com.fitbuddy.service.repository.entity.Athlete;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ActionTemplate {
    private final MongoTemplate mongoTemplate;

    public List<Athlete> calendar(ActionRequest request) {
        Query query = Query.query(
                Criteria.where("userUuid").is(request.getUserUuid())
                .and("date").gte(request.getStartDate())
                .and("date").lte(request.getEndDate())
        );

        query.with(Sort.by(new Sort.Order(Sort.Direction.DESC, "date"))).limit(10);

        return mongoTemplate.find(query, Athlete.class);
    }

    public Athlete detail(String uuid) {
        Query query = Query.query(Criteria.where("userUuid").is(new ObjectId(uuid)));
        return mongoTemplate.findOne(query, Athlete.class);
    }

    public List<Action> histories(ActionRequest request) {
        Criteria criteria = Criteria.where("userUuid").is(request.getUserUuid())
                .and("start").gte(request.getStartDate().atStartOfDay())
                .and("start").lte(request.getEndDate().plusDays(1).atStartOfDay().minusNanos(1));


        if(Objects.nonNull(request.getLastKey())) criteria.and("uuid").gt(new ObjectId(request.getLastKey()));

        Query query = Query.query(criteria);
        query.with(Sort.by(new Sort.Order(Sort.Direction.DESC, "start"))).limit(10);
        return mongoTemplate.find(query, Action.class);
    }

    public void saveAction(Action action) {
        Query query = Query.query(Criteria.where("uuid").is(new ObjectId(action.getUuid())));
        Document document = new Document();
        mongoTemplate.getConverter().write(action, document);
        Update update = Update.fromDocument(document);
        mongoTemplate.upsert(query, update, Action.class);
    }

    public void saveAthlete(Athlete athlete) {
        Query query = Query.query(Criteria.where("uuid").is(new ObjectId(athlete.getUuid())));
        Document document = new Document();
        mongoTemplate.getConverter().write(athlete, document);
        Update update = Update.fromDocument(document);
        mongoTemplate.upsert(query, update, Athlete.class);
    }
}
