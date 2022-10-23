package test.blog2.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/** 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료하면 UserDetails 타입의 오브젝트를 세션에 저장해준다. */
@Getter
public class PrincipalDetail implements UserDetails {

    private Member member;

    public Member getMember() {
        return member;
    }

    public PrincipalDetail(Member member) {
        this.member = member;
    }



    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();

        collectors.add(()-> {return "ROLE_" + member.getRole();});
        return collectors;
    }

    @Override
    public String toString() {
        return "PrincipalDetail{" +
                "member=" + member +
                '}';
    }
}
