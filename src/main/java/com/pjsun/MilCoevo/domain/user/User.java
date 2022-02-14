package com.pjsun.MilCoevo.domain.user;

import com.pjsun.MilCoevo.domain.BaseEntity;
import com.pjsun.MilCoevo.domain.BooleanToYNConverter;
import com.pjsun.MilCoevo.domain.member.Member;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@ToString(exclude = {"members"})
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(length = 1)
    private boolean isAvailable;

    private LocalDateTime lastLoginDate;

    @OneToMany(mappedBy = "user")
    private List<Member> members = new ArrayList<>();

    /* 연관관계 메서드 */
    public void addMember(Member member) {
        members.add(member);
        member.setUser(this);
    }

    /* 생성자 */
    @Builder(builderClassName = "of", builderMethodName = "of")
    private User(String email, String password, Role role, boolean isAvailable) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.isAvailable = isAvailable;
    }

    @Builder(builderClassName = "createByUserBuilder", builderMethodName = "createByUserBuilder")
    public static User createByUser(String email, String password) {
        return createUser(email, password, Role.ROLE_USER);
    }

    @Builder(builderClassName = "createByAdminBuilder", builderMethodName = "createByAdminBuilder")
    public static User createByAdmin(String email, String password) {
        return createUser(email, password, Role.ROLE_ADMIN);
    }

    private static User createUser(String email, String password, Role role) {
        Assert.hasText(email, () -> "[User] email must not be empty");
        Assert.notNull(role, () -> "[User] role must not be null");

        return User.of()
                .email(email).password(password).role(role)
                .isAvailable(true)
                .build();
    }

    /* 비즈니스 로직 */
    public void recordLastLoginDate() {
        this.lastLoginDate = LocalDateTime.now();
    }

    public void changeAvailability(boolean condition) {
        this.isAvailable = condition;
    }

    public void changeRole(Role role) {
        Assert.notNull(role, () -> "[User] role must not be null");

        this.role = role;
    }
}
