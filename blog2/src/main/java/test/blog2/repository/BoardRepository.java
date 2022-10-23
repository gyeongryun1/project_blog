package test.blog2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.blog2.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
