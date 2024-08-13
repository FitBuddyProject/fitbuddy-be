package com.fitbuddy.service.repository.dto;

import com.fitbuddy.service.config.enumerations.Action;
import com.fitbuddy.service.config.enumerations.Buddy;
import com.fitbuddy.service.etc.uuid.Uuid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import static com.fitbuddy.service.etc.validations.MyBuddy.*;

@Data
public class MyBuddyDto {

    @NotEmpty(message = "UUID는 필수 입니다.", groups = {EarnExp.class, ChangePrimary.class})
    private String uuid;
    @NotEmpty(message = "사용자 고유 값은 필수입니다.", groups = {MakeFriends.class, ChangePrimary.class})
    private String userUuid;
    @NotNull(message = "Buddy 타입을 확인하세요.", groups = {MakeFriends.class})
    private Buddy buddy;
    private Boolean isPrimary = true;
    @NotEmpty(message = "Buddy 이름을 지어주세요.", groups = {MakeFriends.class})
    private String name;
    @Min( value = 1, message = "경험치는 1이 최소 입니다.", groups = {EarnExp.class})
    @NotNull(message = "경험치는 필수입니다.", groups = {EarnExp.class})
    private Long exp = 0L;

    private Action action;
    private LocalDateTime whenStart;
    private LocalDateTime whenEnd;

    private boolean isNew = false;

    public MyBuddyDto beforeInsert(){
        this.uuid = Uuid.generate();
        this.isNew = Boolean.TRUE;
        return this;
    }

}
