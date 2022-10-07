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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 封装
 * @author youzhengjie 2022-09-26 18:06:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoginUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    private User user;

    private List<String> permissons;

    /**
     * 框架所需要的权限集合
     */
    @JSONField(serialize = false) //禁止序列化该属性
    private Set<SimpleGrantedAuthority> grantedAuthoritySet;

    public LoginUser(User user, List<String> permissons){
        this.user=user;
        //loadUserByUsername方法中从数据库查询出来的权限列表
        this.permissons=permissons;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //如果grantedAuthoritySet为null才去生成GrantedAuthority类型的权限集合
        if(Objects.isNull(grantedAuthoritySet)){
            this.grantedAuthoritySet=permissons
                    .stream()
                    .distinct()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            return grantedAuthoritySet;
        }
        return grantedAuthoritySet;
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
