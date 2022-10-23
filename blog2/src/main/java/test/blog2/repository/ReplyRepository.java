package test.blog2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import test.blog2.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying
    @Query(value = "INSERT INTO reply(memberId,boardId,content,createDate) VALUES(?1,?2,?3,now())",nativeQuery = true)
    int mSvae(Long memberId, Long boardId, String content); //업데이트된 갯수를 리턴해줌

}
