package test.blog2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import test.blog2.dto.MailDto;
import test.blog2.model.FileStoreS3;
import test.blog2.model.UploadFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final FileStoreS3 fileStoreS3;

    public void 메일보내기(MailDto mailDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        String fileFullPath = null;
        String fileUploadName = null;

        // 메일 제목 설정
        helper.setSubject(mailDto.getTitle());
        // 메일 내용 설정
        helper.setText(mailDto.getContent());
        // 발신자 설정
        helper.setFrom(mailDto.getEmail());
        // 수신자 설정
        helper.setTo(mailDto.getEmail());

         // 파일 전송
            if(!CollectionUtils.isEmpty(mailDto.getMultipartFiles())) {
                for(MultipartFile multipartFile: mailDto.getMultipartFiles()) {
                    helper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }

        mailSender.send(message);
        log.info("*** Email Send Complete *** ");
    }
}


