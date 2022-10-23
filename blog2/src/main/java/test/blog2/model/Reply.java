package test.blog2.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import test.blog2.dto.ReplySaveRequestDto;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String content;

    @CreationTimestamp
    private Timestamp createDate;

    @ManyToOne
    @JoinColumn(name="memberId")
    private Member member;
    @ManyToOne
    @JoinColumn(name="boardId")
    private Board board;


    public void update(Member member, Board board,ReplySaveRequestDto replySaveRequestDto) {
        setMember(member);
        setBoard(board);
        setContent(replySaveRequestDto.getContent());
    }


}
