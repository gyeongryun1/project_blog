package test.blog2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import test.blog2.dto.MemberDto;
import test.blog2.dto.ResponseDto;
import test.blog2.model.KakaoProfile;
import test.blog2.model.Member;
import test.blog2.model.OAuthToken;
import test.blog2.model.PrincipalDetail;
import test.blog2.repository.MemberRepository;
import test.blog2.service.MemberService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth/joinProc")
    public String save(@Validated @ModelAttribute MemberDto memberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}",bindingResult);
//            return "/member/joinForm";
            return "member/joinForm";
        }
        memberService.회원가입(memberDto);

        return "redirect:/"; //자바 오브젝트를 Json으로 변환해서 리턴(Jackson)
    }

    @GetMapping("/admin/member")
    public String adminMember(Model model,@AuthenticationPrincipal PrincipalDetail principal) {
        List<Member> members = memberService.전체회원();
        model.addAttribute("role", String.valueOf(principal.getMember().getRole()));
        model.addAttribute("members", members);
//        return "/admin/member";
        return "admin/member";
    }
    @GetMapping("/user/updateForm")
    public String updateForm(@AuthenticationPrincipal PrincipalDetail principal, Model model) {
        model.addAttribute("principal", principal);
        model.addAttribute("role", String.valueOf(principal.getMember().getRole()));

//        return "/member/updateForm";
        return "member/updateForm";
    }

    @GetMapping("/auth/joinForm")
    public String joinForm(MemberDto memberDto) {

//        return "/member/joinForm";
        return "member/joinForm";
    }

    @GetMapping("/auth/loginForm")
    public String loginForm() {

//        return "/member/loginForm";
        return "member/loginForm";
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallBack(String code) {
        MemberDto kakaoMember = memberService.카카오로그인(code);

        // 로그인 처리
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoMember.getUsername(), kakaoMember.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("자동 로그인을 진행합니다");
        return "redirect:/";
    }



}
