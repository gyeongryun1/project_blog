package test.blog2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import test.blog2.model.UploadFile;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
public class MailDto {

    private String username;
    private String email;
    private String title;
    private String content;
    //    private Set<UploadFile> uploadFiles;
    private List<MultipartFile> multipartFiles;
}
