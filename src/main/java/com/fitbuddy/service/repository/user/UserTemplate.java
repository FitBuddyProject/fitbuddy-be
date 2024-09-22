package com.fitbuddy.service.repository.user;

import com.fitbuddy.service.repository.dto.UserDto;
import com.fitbuddy.service.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class UserTemplate {
    private final MongoTemplate mongoTemplate;

    public boolean isDuplicated(String phone) {
        Query query = Query.query(Criteria.where("phone").is(phone));
        return mongoTemplate.exists(query, User.class);
    }

    public Boolean signOut(UserDto userDto) {
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(userDto.getUuid())));
        Update update = Update.update("sendable", Boolean.FALSE)
                              .set("lastModifiedDate", LocalDateTime.now())
                              .set("lastSignInDate", LocalDateTime.now());
        return mongoTemplate.updateFirst(query, update, User.class).getModifiedCount() > 0L;
    }

    public Boolean syncUser(String uuid, String refreshToken, String pushToken) {

        Query query = Query.query(Criteria.where("_id").is(new ObjectId(uuid)));
        Update update = Update.update("pushToken", pushToken)
                              .set("refreshToken", refreshToken)
                              .set("lastModifiedDate", LocalDateTime.now())
                              .set("lastSignInDate", LocalDateTime.now());
        return mongoTemplate.updateFirst(query, update, User.class).getModifiedCount() > 0L;
    }

    public Boolean syncPushToken(UserDto user) {
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(user.getUuid())));
        Update update = Update.update("pushToken", user.getPushToken());
        return mongoTemplate.updateFirst(query, update, User.class).getModifiedCount() > 0L;
    }

    public Boolean syncTired(UserDto user) {
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(user.getUuid())));
        Update update = Update.update("tired", user.getTired());
        return mongoTemplate.updateFirst(query, update, User.class).getModifiedCount() > 0L;
    }

    public Boolean sendable(UserDto user) {
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(user.getUuid())));
        Update update = Update.update("sendable", user.getSendable());
        return mongoTemplate.updateFirst(query, update, User.class).getModifiedCount() > 0L;
    }
}
