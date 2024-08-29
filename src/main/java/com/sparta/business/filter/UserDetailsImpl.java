package com.sparta.business.filter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.business.entity.User;
import com.sparta.business.entity.UserRoleEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails, Serializable{

    /**
     * 원하는 형식으로 deserialize 할때는 다음 아래와 같은 형식으로 작성
     * activateDefaultTyping 활성화 하였기에 GrantedAuthority 복잡한 형태로 저장
     * user 의 role 필드가 캐시되었기에 authorities 필드는 캐시가 불필요하다고 판다.
     * 모든 필드에 JsonProperty 필요하다는 튜턴님의 의견 반영
     *     @JsonCreator
     *     public UserDetailsImpl(
     *         @JsonProperty("user") User user,
     *         @JsonProperty("auths") List<GrantAuthority> auths // 변경필요
     *         ....
     *     ) {
     *         this.user = user;
     *         this.auths = auths;
     *         ....
     *     }
     *     @Override
     *     public Collection<? extends GrantedAuthority> getAuthorities() {
     *         return this.authorities.stream()
     *             .map(SimpleGrantedAuthority::new)
     *             .collect(Collectors.toList());
     *     }
     */

    @JsonProperty("user")
    private User user;

    @JsonProperty("password")
    private String password;

    @JsonProperty("username")
    private String username;

    @JsonProperty("authorities")
    private List<GrantedAuthority> authorities;

    @JsonProperty("accountNonExpired")
    private boolean accountNonExpired;

    @JsonProperty("accountNonLocked")
    private boolean accountNonLocked;

    @JsonProperty("credentialsNonExpired")
    private boolean credentialsNonExpired;

    @JsonProperty("enabled")
    private boolean enabled;

    public UserDetailsImpl(){
    }

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        UserRoleEnum role = user.getRole();
        String authority = role.getAuthority();

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
