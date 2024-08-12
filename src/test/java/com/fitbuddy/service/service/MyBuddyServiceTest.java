package com.fitbuddy.service.service;


import com.fitbuddy.service.config.enumerations.Buddy;
import com.fitbuddy.service.repository.buddy.BuddyRepository;
import com.fitbuddy.service.repository.buddy.BuddyTemplate;
import com.fitbuddy.service.repository.dto.MyBuddyDto;
import com.fitbuddy.service.repository.entity.MyBuddy;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;
import static org.mockito.Mockito.*;


@ExtendWith(value = {SpringExtension.class})
@Profile(value = "junit")
public class MyBuddyServiceTest {

    @Mock
    private  BuddyRepository repository;
    @Mock
    private  BuddyTemplate template;
    @Spy
    private  ModelMapper mapper;
    @InjectMocks
    private BuddyService service;


    @Nested
    @DisplayName("친구 만들기 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_METHOD)
    class MakeFriends {
        private MyBuddyDto buddyDto;
        private MyBuddy buddy;

        @BeforeEach
        public void setUp () {
            mapper.getConfiguration()
                    .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                    .setFieldMatchingEnabled(true);

            buddyDto = new MyBuddyDto();
            buddyDto.setUserUuid("UUID");
            buddyDto.setBuddy(Buddy.CHICKEN);
            buddyDto.setName("병아리");

            buddy = mapper.map(buddyDto.beforeInsert(), MyBuddy.class);
        }

        @Test
        @DisplayName(value = "실패 - 이미 보유하고 있는 버디일 경우")
        public void alreadyExistBuddy () {
            //when
            when(template.isExist(buddyDto.getBuddy(), buddyDto.getUserUuid())).thenReturn(Boolean.TRUE);

            assertThatThrownBy(() -> service.makeFriend(buddyDto))
                    .hasMessage("이미 친구가 된 버디입니다.")
                    .isInstanceOf(IllegalStateException.class);


        }

        @Test
        @DisplayName(value = "성공 - 친구 추가 성공")
        public void success  (){
            when(template.isExist(buddyDto.getBuddy(), buddyDto.getUserUuid())).thenReturn(Boolean.FALSE);
            doReturn(buddy).when(mapper).map(any(), any());
            doReturn(buddy).when(repository).save(buddy);
            doNothing().when(template).addFriend(buddy);

            assertThat(service.makeFriend(buddyDto))
                    .usingRecursiveComparison()
                    .ignoringExpectedNullFields()
                    .isEqualTo(buddy);
        }
    }


    @Nested
    @DisplayName("메인 친구 설정")
    class ChangePrimaryBuddy {

    }
}
