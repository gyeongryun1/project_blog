package test.blog2.model;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }


    public Set<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        Set<UploadFile> storeResult = new HashSet<>();
        if (!multipartFiles.isEmpty()) {
            for (MultipartFile multipartFile : multipartFiles) {
                UploadFile uploadFile = storeFile(multipartFile);
                storeResult.add(uploadFile);
            }
        }
        log.info("*** store files complete ***");
        return storeResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createFileName(originalFileName);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        log.info("*** store file complete ***");

        return new UploadFile(originalFileName, storeFileName);
    }

    private String createFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." +ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }

}
