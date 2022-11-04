package test.blog2.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import test.blog2.model.*;
import test.blog2.service.BoardService;
import test.blog2.service.MemberService;

import java.net.MalformedURLException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;
    private final FileStore fileStore;
    private final FileStoreS3 fileStoreS3;
    private final MemberService memberService;

    @GetMapping("/board/{id}/attach/{uploadFileName}")
    public ResponseEntity<Resource> fileDownloadS3(@PathVariable Long id,@PathVariable String uploadFileName, Model model) throws MalformedURLException {

        Set<UploadFile> uploadFiles = boardService.파일가져오기(id);
        String storeFileName = null;
        for (UploadFile uploadFile : uploadFiles) {
            if (uploadFile.getUploadFileName().equals(uploadFileName)) {
                storeFileName = uploadFile.getStoreFileName();
            }
        }

//        String encodedUploadFileNames = encode(uploadFile.getUploadFileName(), StandardCharsets.UTF_8);
        String contentDispositions = "attachment; filename=\"" + uploadFileName + "\"";
        UrlResource resource = new UrlResource(fileStoreS3.getFullPath(storeFileName));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDispositions).body(resource);
    }
//    @GetMapping("/board/{id}/attach/{uploadFileName}")
    public ResponseEntity<Resource> fileDownload(@PathVariable Long id,@PathVariable String uploadFileName, Model model) throws MalformedURLException {

        Set<UploadFile> uploadFiles = boardService.파일가져오기(id);
        String storeFileName = null;
        for (UploadFile uploadFile : uploadFiles) {
            if (uploadFile.getUploadFileName().equals(uploadFileName)) {
                storeFileName = uploadFile.getStoreFileName();
            }
        }

//        String encodedUploadFileNames = encode(uploadFile.getUploadFileName(), StandardCharsets.UTF_8);
        String contentDispositions = "attachment; filename=\"" + uploadFileName + "\"";
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDispositions).body(resource);
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable Long id,Model model) {
        Board findBoard = boardService.글상세보기2(id);
        model.addAttribute("board", findBoard);
//        return "/board/updateForm";
        return "board/updateForm";
    }

    @GetMapping("/board/{id}")
    public String findById(Model model, @PathVariable Long id, @AuthenticationPrincipal PrincipalDetail principal) throws MalformedURLException {
        Board findBoard = boardService.글상세보기1(id, model);
        Set<UploadFile> uploadFiles = boardService.파일가져오기(id);

        model.addAttribute("board", findBoard);
        model.addAttribute("principal", principal);
        model.addAttribute("uploadFiles", uploadFiles);

//        return "/board/detail";
        return "board/detail";
    }

    @GetMapping("/")
    public String index(Model model, @PageableDefault(size=3, sort = "id", direction = Sort.Direction.DESC)Pageable pageable, @AuthenticationPrincipal PrincipalDetail principal) {
        Page<Board> boards = boardService.글목록(pageable);
        model.addAttribute("boards", boards);
        if (!(principal == null)) {
            String currentRole = String.valueOf(principal.getMember().getRole());
            log.info(currentRole);
            model.addAttribute("role",currentRole);
        }
        return "index";
    }

    @GetMapping("/admin/adminHome")
    public String adminHome(Model model,@AuthenticationPrincipal PrincipalDetail principal) {
        model.addAttribute("role", String.valueOf(principal.getMember().getRole()));
//        return "/admin/adminHome";
        return "admin/adminHome";
    }

    @GetMapping("/admin/boards")
    public String adminBoard(Model model,@PageableDefault(size=10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable, @AuthenticationPrincipal PrincipalDetail principal) {
        Page<Board> boards = boardService.글목록(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("role", String.valueOf(principal.getMember().getRole()));

//        return "admin/boards";
        return "admin/boards";
    }

    @GetMapping("/board/saveForm")
    public String save(Model model, @AuthenticationPrincipal PrincipalDetail principal) {
        model.addAttribute("role", String.valueOf(principal.getMember().getRole()));
//        return "/board/saveForm";
        return "board/saveForm";
    }

    @GetMapping("/board/updateForm")
    public String update() {

//        return "/board/update";
        return "board/update";
    }
}
