package test.blog2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.blog2.model.UploadFile;

public interface FileRepository extends JpaRepository<UploadFile,Long> {
}
