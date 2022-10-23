package test.blog2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import test.blog2.dto.MemberDto;
import test.blog2.model.KakaoProfile;
import test.blog2.model.Member;
import test.blog2.model.MemberAuth;
import test.blog2.model.OAuthToken;
import test.blog2.repository.MemberRepository;

import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void 회원가입(MemberDto memberDto) {
        Member member = Member.builder()
                        .username(memberDto.getUsername())
                        .password(memberDto.getPassword())
                        .email(memberDto.getEmail())
                        .oauth(memberDto.getOauth())
                        .build();

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(MemberAuth.MEMBER);
        log.info("****");
        Member savedMember = memberRepository.save(member);
        log.info("*****"+savedMember.toString());

    }
    @Transactional
    public void 관리자생성(Member member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(MemberAuth.ADMIN);
        Member savedMember = memberRepository.save(member);
        log.info("*****"+savedMember.toString());

    }

    @Transactional
    public void 회원수정(Member member) {
        Member persistence = memberRepository.findById(member.getId()).orElseThrow(() -> {
            return new IllegalArgumentException("회원수정 실패: 해당 아이디를 찾을 수 없습니다");
        });



        if (persistence.getOauth() == null || persistence.getOauth() == "") {
            String rawPassword = member.getPassword();
            String encPassword = passwordEncoder.encode(rawPassword);
            persistence.setPassword(encPassword);
            persistence.setEmail(member.getEmail());
        }

        memberRepository.flush();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Transactional(readOnly = true)
    public Member 회원찾기(String username) {

        Member member = memberRepository.findByUsername(username).orElseGet(()->{
            return new Member();
        });

        return member;
    }

    @Transactional(readOnly = true)
    public List<Member> 전체회원() {
        return memberRepository.findAll();


    }

    @Transactional
    public void 회원차단(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        member.get().setRole(MemberAuth.BLOCK);
    }

    @Transactional
    public void 회원차단해제(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        member.get().setRole(MemberAuth.MEMBER);
    }

    @Transactional
    public MemberDto 카카오로그인(String code) {

            /** POST 방식으로 key=value 데이터 요청
             *  Retrofit2
             *  Okhttp
             *  RestTemplate
             */
            RestTemplate rt = new RestTemplate();
            /** HttpHeader 오브젝트 생성 */
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            /** HttpBody 오브젝트 생성 */
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", "34dc765c12bd0dfe35c15917ee571b87");
            params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
            params.add("code", code);

            /** HttpHeader와 HttpBody를 하나의 오브젝트에 담기 */
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

            /** Http 요청하기 - Post방식으로 . 그리고 response의 변수의 응답 받음 */
            ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);

            /** Gson,Json Simple, ObjectMapper (응답을 OAuthToken에 넣기) */
            ObjectMapper objectMapper = new ObjectMapper();

            OAuthToken oAuthToken = null;
            try {
                oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
            } catch (JsonProcessingException e) {
                log.info("** objectMapper Fail **");
                e.printStackTrace();
            }

            log.info(" ** 카카오 엑세스 토큰 : " + oAuthToken.getAccess_token());

            RestTemplate rt2 = new RestTemplate();

            /** HttpHeader 오브젝트 생성 */
            HttpHeaders headers2 = new HttpHeaders();
            headers2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
            headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            /** HttpHeader와 HttpBody를 하나의 오브젝트에 담기 */
            HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

            /** Http 요청하기 - Post방식으로 . 그리고 response의 변수의 응답 받음 */
            ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoProfileRequest, String.class);

            ObjectMapper objectMapper2 = new ObjectMapper();
            KakaoProfile kakaoProfile = null;

            /** 응답받은 데이터 objectMapping */
            try {
                kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
            } catch (JsonProcessingException e) {
                log.info("** objectMapper Fail **");
                e.printStackTrace();
            }

            log.info("** 카카오 아이디(번호) : " + kakaoProfile.getId());
            log.info("** 카카오 이메일 : " + kakaoProfile.kakao_account.getEmail());

            /** 애플리케이션에 회원가입 or 로그인 시키는 로직 */

            String kakaoUsername = kakaoProfile.kakao_account.getEmail() + "_" + kakaoProfile.getId();
            String kakaoEmail = kakaoProfile.kakao_account.getEmail();

            /** pwd 수정 필요 */
            String tempPWD = "kakao";
            log.info("** 카카오 블로그 서버 유저네임 : " + kakaoUsername);
            log.info("** 카카오 블로그 서버 이메일 : " + kakaoEmail);
            log.info("** 블로그 서버 패스워드 : " + tempPWD);

            MemberDto kakaoMember = MemberDto.builder()
                    .username(kakaoUsername)
                    .email(kakaoEmail)
                    .password(tempPWD.toString())
                    .oauth("kakao")
                    .build();

            log.info("********" + kakaoMember.toString());

            Member originMember = 회원찾기(kakaoMember.getUsername());
//            Member originMember = memberService.회원찾기(kakaoMember.getUsername());

            if(originMember.getUsername() == null) {
                log.info("** 기존에 가입되어 있지 않은 회원입니다");
                  회원가입(kakaoMember);
                  log.info("kakao Oauth " + kakaoMember.getOauth());
//                memberService.회원가입(kakaoMember);
            }
        //        return "카카오 인증 완료: 코드값: "+code;
        //        return "카카오 토큰 요청 완료: 토큰에 대한 응답값 : "+response.getBody();
        //        return response2.getBody();

        return kakaoMember;
       }
    }

