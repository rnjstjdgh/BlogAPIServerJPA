package nk.demo.BlogAPIServer.Security.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@SequenceGenerator(name = "userTable_SEQ_GENERATOR", sequenceName = "userTable_SEQ", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "userTable")
@Builder            // builder를 사용할수 있게 합니다.
@Getter             // user 필드값의 getter를 자동으로 생성합니다.
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 인자없는 생성자를 자동으로 생성합니다.
@EntityListeners(AuditingEntityListener.class)
public class UserEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE , generator="userTable_SEQ_GENERATOR")
    private Long 				userId;

    @Column(length = 30, nullable = false)
    private String 				email;

    @Column(length = 300, nullable = false)
    private String 				password;

    @Column(length = 30, nullable = false)
    private String 				role;

    @CreatedDate
    @Column(updatable = false)
	private LocalDateTime 		regDate;		//등록일자

    @LastModifiedDate
    private LocalDateTime 		updateDate;		//수정일자


    @Builder
    public UserEntity(Long userId, String email, String password, String role, LocalDateTime regDate, LocalDateTime updateDate){
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.regDate = regDate;
        this.updateDate = updateDate;
    }
}