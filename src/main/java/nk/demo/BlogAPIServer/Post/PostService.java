package nk.demo.BlogAPIServer.Post;

import java.util.ArrayList;
import java.util.List;

import nk.demo.BlogAPIServer.Post.Dtos.BasicPostDto;
import nk.demo.BlogAPIServer.Post.Dtos.PostDto;
import nk.demo.BlogAPIServer.Security.User.Dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import nk.demo.BlogAPIServer.CustomException.ClientDataValidationException;

import javax.transaction.Transactional;


/**
 * 게시판 서비스
 * @author gshgsh0831
 * **/
@Service
public class PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	/**
	 * 전체 게시글 리스트 리턴
	 * @return
	 * **/
	public List<PostDto> getList(){
		List<PostEntity> postEntityList = postRepository.findAll();
		if(postEntityList.size() == 0)
			throw new ClientDataValidationException("There is no corresponding information.");
		List<PostDto> postDtoList = new ArrayList<>();
		for(PostEntity postEntity : postEntityList)
			postDtoList.add(this.convertEntityToDto(postEntity));
        
		return postDtoList;
	}

	/**
	 * {title}이 제목에 포함된 게시글 리스트 리턴
	 * @return
	 * **/
	public List<PostDto> getList(String title){
		List<PostEntity> postEntityList = postRepository.findByTitleContaining(title);
		if(postEntityList.size() == 0)
			throw new ClientDataValidationException("There is no corresponding information for title.");
		List<PostDto> postDtoList = new ArrayList<>();
		for(PostEntity postEntity : postEntityList)
			postDtoList.add(this.convertEntityToDto(postEntity));

		return postDtoList;
	}

	/**
	 * {userId}에 해당하는 사용자가 작성한 게시글 리스트 리턴
	 * @return
	 * **/
	public List<PostDto> getList(Long userId){
		List<PostEntity> postEntityList = postRepository.findByUserId(userId);
		if(postEntityList.size() == 0)
			throw new ClientDataValidationException("There is no corresponding information for userId.");
		List<PostDto> postDtoList = new ArrayList<>();
		for(PostEntity postEntity : postEntityList)
			postDtoList.add(this.convertEntityToDto(postEntity));

		return postDtoList;
	}

	/**
	 * {userId}에 해당하는 사용자가 작성하였고 {title}이 제목인 게시글 리스트 리턴
	 * @return
	 * **/
	public List<PostDto> getList(Long userId, String title){
		List<PostEntity> postEntityList = postRepository.findByUserIdAndTitleContaining(userId, title);
		if(postEntityList.size() == 0)
			throw new ClientDataValidationException("There is no corresponding information for userId and title");
		List<PostDto> postDtoList = new ArrayList<>();
		for(PostEntity postEntity : postEntityList)
			postDtoList.add(this.convertEntityToDto(postEntity));

		return postDtoList;
	}
	
	/**
	 * {postId}에 해당하는 게시글 리턴
	 * @param postId
	 * @return
	 * **/
	public PostDto get(Long postId) {
		if(postId < 0)
			throw new ClientDataValidationException("postId cannot be minus.");
		PostEntity postEntity = postRepository.findByPostId(postId);
		if(postEntity == null)
			throw new ClientDataValidationException("There is no corresponding information for postId.");
		return convertEntityToDto(postEntity);
	}
	
	/**
	 * 하나의 게시글 등록
	 * @param basicPostDto
	 * @return
	 * **/
	public Long save(BasicPostDto basicPostDto) {
		if(basicPostDto.getContents() == null || basicPostDto.getTitle() == null || basicPostDto.getUserId() == 0)
			throw new ClientDataValidationException("Not enough post data.");

		UserDto currentLoginUser = (UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentLoginUser.getUserId() != basicPostDto.getUserId())
			throw new ClientDataValidationException("Post userId is invalid.");

		if(postRepository.findByTitle(basicPostDto.getTitle()) != null)
			throw new ClientDataValidationException("title is overlap");

		PostEntity postEntity = basicPostDto.toEntity();
		postRepository.save(postEntity);
		return postEntity.getPostId();
	}

	/**
	 * {postId}에 해당하는 게시글 수정
	 * @param basicPostDto
	 * @return
	 * **/
	public Long update(Long postId, BasicPostDto basicPostDto) {
		if(postId <= 0)
			throw new ClientDataValidationException("postId cannot be minus.");

		if(basicPostDto.getContents() == null || basicPostDto.getTitle() == null || basicPostDto.getUserId() == 0)
			throw new ClientDataValidationException("Not enough post data.");
		UserDto currentLoginUser = (UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentLoginUser.getUserId() != basicPostDto.getUserId())
			throw new ClientDataValidationException("Post userId is invalid.");
		if(postRepository.findByPostId(postId) ==null)
			throw new ClientDataValidationException("There is no corresponding information for postId.");

		PostEntity postEntity = basicPostDto.toEntity();
		postEntity.setPostId(postId);
		postRepository.save(postEntity);
		return postEntity.getPostId();
	}
	
	/**
	 * {postId}에 해당하는 게시글 삭제
	 * @param postId
	 * @return
	 * **/
	@Transactional
	public Long delete(Long postId) {
		if(postId <= 0) 
			throw new ClientDataValidationException("postId cannot be minus.");

		PostEntity postEntity = postRepository.findByPostId(postId);
		if(postEntity ==null)
			throw new ClientDataValidationException("There is no corresponding information for postId.");

		UserDto currentLoginUser = (UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentLoginUser.getUserId() != postEntity.getUserId())
			throw new ClientDataValidationException("Post userId is invalid.");

		postRepository.deleteByPostId(postId);
		return postId;
	}
	
    private PostDto convertEntityToDto(PostEntity postEntity){
        return PostDto.builder()
        				.postId(postEntity.getPostId())
        				.title(postEntity.getTitle())
        				.userId(postEntity.getUserId())
        				.contents(postEntity.getContents())
        				.regDate(postEntity.getRegDate())
						.updateDate(postEntity.getUpdateDate())
        				.build();
    }
}
