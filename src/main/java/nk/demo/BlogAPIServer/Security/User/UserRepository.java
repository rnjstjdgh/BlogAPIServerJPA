package nk.demo.BlogAPIServer.Security.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nk.demo.BlogAPIServer.Post.PostEntity;

/**
 * 게시판 레포지토리
 * @author gshgsh0831
 * **/
public interface UserRepository extends JpaRepository<UserEntity,Long> {
	
	List<UserEntity> findAll();
	
	UserEntity findByuserId(Long userId);

	UserEntity save(UserEntity user);
	
//	void update(UserEntity user);
	
	void deleteByUserId(Long userId);
	
	UserEntity findByEmail(String email);
	
}
