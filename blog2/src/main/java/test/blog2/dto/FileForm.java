package test.blog2.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FileForm {

    private Long id;
    private List<MultipartFile> files;

}
