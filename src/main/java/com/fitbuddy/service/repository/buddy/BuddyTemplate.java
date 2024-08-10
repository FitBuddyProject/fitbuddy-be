package com.fitbuddy.service.repository.buddy;

import com.fitbuddy.service.config.enumerations.Buddy;
import com.fitbuddy.service.repository.dto.MyBuddyDto;
import com.fitbuddy.service.repository.entity.MyBuddy;
import com.fitbuddy.service.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BuddyTemplate {
    private final MongoTemplate mongoTemplate;

    public boolean changePrimaryBuddy(MyBuddyDto myBuddy) {
        String trueUUID = myBuddy.getUuid();
        String userUUID = myBuddy.getUserUuid();
        Query queryMulti = Query.query(Criteria.where("userUuid").is(userUUID).and("uuid").not().is(new ObjectId(trueUUID)));
        Update updateMulti = Update.update("isPrimary", Boolean.FALSE);

        Query query = Query.query(Criteria.where("id").is(userUUID));
        Update update = Update.update("isPrimary", Boolean.TRUE);

        mongoTemplate.findAndModify(queryMulti, updateMulti, MyBuddy.class);
        return mongoTemplate.updateFirst(query,update, MyBuddy.class).getModifiedCount() > 0;
    }

    public void addFriend(MyBuddy buddy) {
        String userUuid = buddy.getUserUuid();
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(userUuid)));
        Update update = new Update();
        update.addToSet("buddies", buddy);
        mongoTemplate.updateFirst(query, update, User.class);
    }

    public List<Buddy> dictionary(String userUuid) {
        Query query = Query.query(Criteria.where("userId").is(userUuid));
        return mongoTemplate.findDistinct(query, "buddy", MyBuddy.class, Buddy.class);
    }
}
