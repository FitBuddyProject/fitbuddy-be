package com.fitbuddy.service.repository.buddy;

import com.fitbuddy.service.repository.entity.MyBuddy;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BuddyRepository extends MongoRepository<MyBuddy, String> {
    List<MyBuddy> findMyBuddiesByUserUuid( String userUuid);
}
