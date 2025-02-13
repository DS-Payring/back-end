package com.backend.payring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String password;

    @Column(name = "profile_image")
    private String profileImage;

    @Column
    private String payUrl;

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountEntity> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMemberEntity> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "user",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentEntity> payments = new ArrayList<>();

    private String emailVerificationNum;
    private boolean emailVerified;

    public static UserEntity createForEmailVerification(String email, String verificationNum) {
        return UserEntity.builder()
                .email(email)
                .emailVerificationNum(verificationNum)
                .emailVerified(false)
                .userName("") // 임시값
                .build();
    }

    public void updateEmailVerification(String verificationNum) {
        this.emailVerificationNum = verificationNum;
        this.emailVerified = false;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void updateUserInfo(String userName, String password, String profileImage, String payUrl) {
        this.userName = userName;
        this.password = password;
        this.profileImage = profileImage;
        this.payUrl = payUrl;
    }

    public void addAccount(AccountEntity account) {
        this.accounts.add(account);
        account.setUser(this);
    }

    public void updateUserInfo(String userName, String payUrl) {
        this.userName = userName;
        this.payUrl = payUrl;
    }
}
