package com.fitbuddy.service.service;

import com.fitbuddy.service.repository.buddy.BuddyRepository;
import com.fitbuddy.service.repository.buddy.BuddyTemplate;
import com.fitbuddy.service.repository.dto.MyBuddyDto;
import com.fitbuddy.service.repository.entity.MyBuddy;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BuddyService {
    private final BuddyRepository repository;
    private final BuddyTemplate template;
    private final ModelMapper mapper;
    public List<MyBuddy> myBuddies(String uuid) {
        return repository.findMyBuddiesByUserUuid(uuid);
    }

    public MyBuddy makeFriend(MyBuddyDto myBuddy) {
        return repository.save(mapper.map(myBuddy, MyBuddy.class));
    }

    public boolean changePrimaryBuddy(MyBuddyDto myBuddy) {
        return template.changePrimaryBuddy(myBuddy);
    }
}
