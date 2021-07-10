package nk.demo.BlogAPIServer.Post;

import java.util.List;

import oracle.jdbc.proxy.annotation.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 게시판 레포지토리
 * @author gshgsh0831
 * **/
public interface PostRepository extends JpaRepository<PostEntity, Long> {
	PostEntity findByPostId(Long postId);

	List<PostEntity> findByUserId(Long userId);

	PostEntity findByTitle(String title);
	List<PostEntity> findByTitleContaining(String title);

	List<PostEntity> findByUserIdAndTitleContaining(Long userId, String title);

	PostEntity save(PostEntity post);

	void deleteByPostId(Long postId);

}
