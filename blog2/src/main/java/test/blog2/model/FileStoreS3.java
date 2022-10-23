package test.blog2.model;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStoreS3 {

//    @Value("${fileS3.dir}")
//    private String fileDir;

    private final AmazonS3Client amazonS3Client;

    private String S3Bucket = "seotest1";

//    public String getFullPath(String filename) {
//        return fileDir + filename;
//    }
    public String getFullPath(String filename) {
        return amazonS3Client.getUrl(S3Bucket,filename).toString();
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

        if (multipartFile.isEmpty()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다");
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createFileName(originalFileName);

        Long size = multipartFile.getSize();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        // ContentType 설정
        objectMetadata.setContentType(multipartFile.getContentType());
        // 파일 크기 설정
        objectMetadata.setContentLength(size);

        log.info("***" + multipartFile.getOriginalFilename() +"***");

        // S3에 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(S3Bucket, storeFileName, multipartFile.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        // 접근가능한 url 가져오기
        String filePath = amazonS3Client.getUrl(S3Bucket, storeFileName).toString();
        log.info("*** filePath ={} ***" ,filePath);
        return new UploadFile(originalFileName, storeFileName);
    }

    public void fileDelete(String fileName) {
        DeleteObjectRequest request = new DeleteObjectRequest(S3Bucket, fileName);
        amazonS3Client.deleteObject(request);

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
