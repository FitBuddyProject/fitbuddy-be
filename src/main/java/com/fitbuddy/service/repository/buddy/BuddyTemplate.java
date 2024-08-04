package com.fitbuddy.service.repository.buddy;

import com.fitbuddy.service.repository.dto.MyBuddyDto;
import com.fitbuddy.service.repository.entity.MyBuddy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BuddyTemplate {
    private final MongoTemplate mongoTemplate;

    public boolean changePrimaryBuddy(MyBuddyDto myBuddy) {
        String trueUUID = myBuddy.getUuid();
        String userUUID = myBuddy.getUserUuid();
        Query queryMulti = Query.query(Criteria.where("userUuid").is(userUUID).and("uuid").not().is(trueUUID));
        Update updateMulti = Update.update("isPrimary", Boolean.FALSE);

        Query query = Query.query(Criteria.where("uuid").is(userUUID));
        Update update = Update.update("isPrimary", Boolean.TRUE);

        mongoTemplate.findAndModify(queryMulti, updateMulti, MyBuddy.class);
        return mongoTemplate.updateFirst(query,update, MyBuddy.class).getModifiedCount() > 0;
    }
}
