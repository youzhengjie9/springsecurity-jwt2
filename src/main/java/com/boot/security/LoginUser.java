package com.boot.security;

import com.alibaba.fastjson.annotation.JSONField;
import com.boot.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 封装
 * @author youzhengjie 2022-09-26 18:06:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoginUser implements UserDetails, Serializable {

    private User user;

    private List<String> permissons;

    /**
     * 框架所需要的权限集合
     */
    @JSONField(serialize = false) //禁止序列化该属性
    private Set<SimpleGrantedAuthority> grantedAuthoritySet;

    public LoginUser(User user, List<String> permissons){
        this.user=user;
        this.permissons=permissons;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//         grantedAuthoritySet = Optional.ofNullable(grantedAuthoritySet)
//                .orElseGet(() -> permissons
//                        .stream()
//                        .distinct()
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toSet()));

//        return grantedAuthoritySet;
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
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

        return (user.getDelFlag()==0 && user.getStatus()==0);
    }
}
