package test.blog2.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import test.blog2.model.Member;
import test.blog2.model.PrincipalDetail;
import test.blog2.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {

    private final MemberRepository memberRepository;
    /** 스프링이 로그인처리를 할때 username, password 두개를 가로채는데
     * password는 알아서 처리하기 때문에 username이 DB에 있는지만 확인해주면 된다 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /** DB에서 username이 없어도 예외가 뜨지 않음. why? */

        Member member = memberRepository.findByUsername(username).orElseThrow(()->{
            return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다." +username);
        });

        return new PrincipalDetail(member); /** 시큐리티 세션에 유저 정보가 저장된다 */
    }
}
