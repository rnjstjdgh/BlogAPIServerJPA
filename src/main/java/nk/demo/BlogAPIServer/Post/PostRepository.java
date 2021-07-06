package nk.demo.BlogAPIServer.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 게시판 레포지토리
 * @author gshgsh0831
 * **/
public interface PostRepository extends JpaRepository<PostEntity, Long> {
	PostEntity findByPostId(Long postId);

	PostEntity save(PostEntity post);

	void deleteByPostId(Long postId);
}
