package test.blog2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.blog2.model.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
}
