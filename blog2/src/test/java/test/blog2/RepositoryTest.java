package test.blog2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import test.blog2.model.Member;
import test.blog2.model.MemberAuth;
import test.blog2.repository.MemberRepository;
import test.blog2.service.MemberService;

@Slf4j
@Rollback(value = false)
@SpringBootTest
public class RepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Test
    public void save() {
        Member member = Member.builder()
//                .id(1L)
                .username("admin")
                .password("admin")
                .email("admin@admin.com")
                .role(MemberAuth.ADMIN)
                .build();
        log.info("** log: " + member.toString()+" **");
        memberService.관리자생성(member);
    }

    @Test
    public void test() {
        Member member = memberRepository.findByUsername("azxc").orElseGet(() -> {
            return new Member();
        });
        if (member.getUsername() == null) {
            log.info("정상 작동");
        }
        System.out.println("** username: "+member.getUsername());
    }

    @Test
    public void test2() {
        //똑같이 db에 없는 data를 가져오는 case. nullpoinExeption 발생하지 않고 정상작동됨
        Member member = memberService.회원찾기("qwdqwd");
        log.info(member.toString());
    }
}
