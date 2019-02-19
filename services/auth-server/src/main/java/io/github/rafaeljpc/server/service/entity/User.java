package io.github.rafaeljpc.server.service.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class User extends BaseIdEntity implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String email;
    private String username;
    private String password;
    private boolean enabled;

    @Column(name = "account_locked")
    private boolean accountNonLocked;

    @Column(name = "account_expired")
    private boolean accountNonExpoired;

    @Column(name = "credentials_expired")
    private boolean credentialsNonExpoired;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_user",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles;

    @Override
    public boolean isEnabled() { return enabled; }

    @Override
    public boolean isAccountNonExpired() { return !accountNonExpoired; }

    @Override
    public boolean isCredentialsNonExpired() { return !credentialsNonExpoired; }

    @Override
    public boolean isAccountNonLocked() { return !accountNonLocked; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        roles.forEach( r -> {
            authorities.add(new SimpleGrantedAuthority(r.getName()));
            r.getPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName())));
        });

        return authorities;
    }

}
