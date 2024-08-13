package com.fitbuddy.service.service;

import com.fitbuddy.service.config.enumerations.Buddy;
import com.fitbuddy.service.repository.buddy.BuddyRepository;
import com.fitbuddy.service.repository.buddy.BuddyTemplate;
import com.fitbuddy.service.repository.dto.MyBuddyDto;
import com.fitbuddy.service.repository.entity.MyBuddy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BuddyService {
    private final BuddyRepository repository;
    private final BuddyTemplate template;
    private final ModelMapper mapper;
    public List<MyBuddy> myBuddies(String userUuid) {
        return repository.findMyBuddiesByUserUuid(userUuid);
    }

    public MyBuddy makeFriend(MyBuddyDto myBuddy) {

        if( template.isExist(myBuddy.getBuddy(), myBuddy.getUserUuid()) ) throw new IllegalStateException("이미 친구가 된 버디입니다.");
        MyBuddy buddy = repository.save(mapper.map(myBuddy.beforeInsert(), MyBuddy.class));
        template.addFriend(buddy);
        return buddy;
    }

    public boolean changePrimaryBuddy(MyBuddyDto myBuddy) {
        return template.changePrimaryBuddy(myBuddy);
    }

    public List<Buddy> dictionary(String userUuid) {
        return template.dictionary(userUuid);
    }

    public Boolean earnExp(MyBuddyDto myBuddy) {
        return template.earnExp(myBuddy);
    }
}
