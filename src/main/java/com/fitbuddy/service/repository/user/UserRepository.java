package com.fitbuddy.service.repository.user;

import com.fitbuddy.service.repository.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByPhone(String phone);
}
