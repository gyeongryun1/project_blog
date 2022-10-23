package test.blog2.service;

import com.amazonaws.services.s3.model.DeleteObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import test.blog2.dto.MailDto;
import test.blog2.model.*;
import test.blog2.dto.FileForm;
import test.blog2.dto.ReplySaveRequestDto;
import test.blog2.repository.BoardRepository;
import test.blog2.repository.FileRepository;
import test.blog2.repository.MemberRepository;
import test.blog2.repository.ReplyRepository;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final FileStore fileStore;
    private final FileStoreS3 fileStoreS3;
    private final FileRepository fileRepository;
    private final MailService mailService;

    @Transactional
    public void 글쓰기(Board board, Member member, FileForm form) throws IOException, MessagingException {

        board.setCount(0L);
        board.setMember(member);
        Board saveBoard = boardRepository.save(board);

        파일저장(board, member, form);

        // 메일 보내기
        MailDto mailDto = MailDto.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .title(board.getTitle())
                .content(board.getContent())
//                .uploadFiles(uploadFiles)
                .multipartFiles(form.getFiles())
                .build();
        log.info(" *** MailDto : " + mailDto.toString());
        mailService.메일보내기(mailDto);
    }



    @Transactional(readOnly = true)
    public Page<Board> 글목록(Pageable pageable) {
         return boardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Board 글상세보기2(Long id) {
        return boardRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다");});
    }

    @Transactional
    public Board 글상세보기1(Long id, Model model) {
        Board findBoard = boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다");
        });
        findBoard.plusCount();
        return findBoard;
    }

    @Transactional(readOnly = true)
    public Set<UploadFile> 파일가져오기(Long id) throws MalformedURLException {
        Board findBoard = boardRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("파일 가져오기 실패 : 아이디를 찾을 수 없습니다");
        });
        Set<UploadFile> uploadFiles = findBoard.getUploadFiles();
        return uploadFiles;
    }

    @Transactional
    public void 글삭제하기(Long id) {
         boardRepository.deleteById(id);
    }


    @Transactional
    public void 글수정하기(Board board, Long id, FileForm form, Member member) throws IOException {
        Board findBoard = boardRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("글 찾기 실패: 보드 아이디를 찾을 수 없습니다");
        });
        findBoard.setTitle(board.getTitle());
        findBoard.setContent(board.getContent());


        // 파일 삭제
        for (UploadFile uploadFile : findBoard.getUploadFiles()) {
            fileRepository.deleteById(uploadFile.getId());
//            fileStoreS3.fileDelete(uploadFile.getStoreFileName());

        }
        log.info("** 파일 삭제 완료");
        파일저장(board, member, form);
        log.info("** 파일 저장 완료");
        /** 해당 함수 종료시 트랜잭션이 종료하면서 더티체킹에 의해 자동 업데이트가 됨- flush */
    }


    @Transactional
    public void 댓글쓰기(ReplySaveRequestDto dto) {
       replyRepository.mSvae(dto.getMemberId(),dto.getBoardId(), dto.getContent());
    }

    @Transactional
    public void 댓글삭제(Long replyId) {
        replyRepository.deleteById(replyId);
    }

    @Transactional
    public void 글숨기기(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        board.get().setHide("hide");
    }
    @Transactional
    public void 글보이기(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        board.get().setHide(null);
    }

    private void 파일저장(Board board, Member member, FileForm form) throws IOException {

        Set<UploadFile> uploadFiles = null;

        // db에 파일 이름 저장, aws에 파일 저장
        if(form.getFiles() != null){
            uploadFiles = fileStoreS3.storeFiles(form.getFiles());
            board.setUploadFiles(uploadFiles);
            for (UploadFile uploadFile : uploadFiles) {
                uploadFile.setBoard(board);
                fileRepository.save(uploadFile);
            }}
    }


//    @Transactional
//    public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {
//        Member findMember = memberRepository.findById(replySaveRequestDto.getMemberId()).orElseThrow(()-> {
//                    return new IllegalArgumentException("댓글 쓰기 실패: 유저 아이디를 찾을 수 없습니다");
//                });
//
//        Board findBoard = boardRepository.findById(replySaveRequestDto.getBoardId()).orElseThrow(()->{
//            return new IllegalArgumentException("댓글 쓰기 실패: 보드 아이디를 찾을 수 없습니다");
//                });
//
//        Reply reply = new Reply();
//        reply.update(findMember,findBoard,replySaveRequestDto);
//
//        //        Reply reply = Reply.builder()
////                .member(findMember)
////                .board(findBoard)
////                .content(replySaveRequestDto.getContent())
////                .build();
//        replyRepository.save(reply);
//    }
}
