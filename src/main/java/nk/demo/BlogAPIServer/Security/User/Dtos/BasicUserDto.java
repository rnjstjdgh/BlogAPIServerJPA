package nk.demo.BlogAPIServer.Security.User.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nk.demo.BlogAPIServer.Security.User.UserEntity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

@Getter @Setter
public class BasicUserDto implements UserDetails {


    private String 				email;
    private String 				password;
    private String 				role;



    public UserEntity toEntity(){
        return UserEntity.builder()
                            .email(email)
                            .password(password)
                            .role(role)
                            .build();
    }

    @Builder
    public BasicUserDto(int userId, String email, String password, String role, LocalDateTime regDate){
        this.email = email;
        this.password = password;
        this.role = role;
    }


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)	//직렬화 과정에서 제외시킨다는 의미
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)	//직렬화 과정에서 제외시킨다는 의미
    @Override
    public String getUsername() {
        return this.email;
    }

    //계정이 만료되었는지
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠기지 않았는지
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //계정 패스워드가 만료 안됬는지
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 사용 가능한지
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

}
