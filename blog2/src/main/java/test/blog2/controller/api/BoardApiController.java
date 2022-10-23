package test.blog2.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.blog2.dto.MailDto;
import test.blog2.model.Board;
import test.blog2.dto.FileForm;
import test.blog2.model.PrincipalDetail;
import test.blog2.dto.ReplySaveRequestDto;
import test.blog2.dto.ResponseDto;
import test.blog2.service.BoardService;
import test.blog2.service.MailService;

import javax.mail.MessagingException;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BoardApiController {

    private final BoardService boardService;

    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable Long id,@ModelAttribute Board board, @ModelAttribute FileForm form,@AuthenticationPrincipal PrincipalDetail principalDetail) throws IOException {
        boardService.글수정하기(board,id,form, principalDetail.getMember() );
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable Long id) {
        boardService.글삭제하기(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
    @DeleteMapping("/admin/board/{id}")
    public ResponseDto<Integer> deleteByIdAdmin(@PathVariable Long id) {
        boardService.글삭제하기(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
    @PostMapping("/admin/board/hide/{id}")
    public ResponseDto<Integer> hideByIdAdmin(@PathVariable Long id) {
        boardService.글숨기기(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }
    @PostMapping("/admin/board/show/{id}")
    public ResponseDto<Integer> showByIdAdmin(@PathVariable Long id) {
        boardService.글보이기(id);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }

    @PostMapping("/api/board")
    public ResponseDto<Integer> save(@ModelAttribute FileForm form, @ModelAttribute Board board, @AuthenticationPrincipal PrincipalDetail principal) throws IOException, MessagingException {
        boardService.글쓰기(board, principal.getMember(), form);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); //자바 오브젝트를 Json으로 변환해서 리턴(Jackson)
    }

    @PostMapping("/api/board/{id}/reply")
    public ResponseDto<Integer> replySave(@Validated @RequestBody ReplySaveRequestDto replySaveRequestDto) {
            boardService.댓글쓰기(replySaveRequestDto);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); //자바 오브젝트를 Json으로 변환해서 리턴(Jackson)
    }

    @DeleteMapping("/api/board/{boardId}/reply/{replyId}")
    public ResponseDto<Integer> replyDelete(@PathVariable Long replyId) {
        log.info(replyId.toString());
        boardService.댓글삭제(replyId);
        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);

    }

}
