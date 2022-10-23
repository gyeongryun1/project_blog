package test.blog2.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.blog2.dto.MemberDto;
import test.blog2.model.Member;
import test.blog2.dto.ResponseDto;
import test.blog2.model.PrincipalDetail;
import test.blog2.service.MemberService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

//    @PostMapping("/auth/joinProc")
//    public ResponseDto<Integer> save(@Validated @RequestBody MemberDto memberDto, Model model) {
//        memberService.회원가입(memberDto);
//        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); //자바 오브젝트를 Json으로 변환해서 리턴(Jackson)
//    }

    @PutMapping("/member")
    public ResponseDto<Integer> update(Model model,@RequestBody Member member) {
        memberService.회원수정(member);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
    @GetMapping("/admin/block/{id}")
    public ResponseDto<Integer> block(@PathVariable Long id) {
        memberService.회원차단(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
    @GetMapping("/admin/unBlock/{id}")
    public ResponseDto<Integer> unBlock(@PathVariable Long id) {
        memberService.회원차단해제(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

}
