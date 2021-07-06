package nk.demo.BlogAPIServer.Post;

import java.util.ArrayList;
import java.util.List;

import nk.demo.BlogAPIServer.Post.Dtos.BasicPostDto;
import nk.demo.BlogAPIServer.Post.Dtos.PostDto;
import org.apache.logging.log4j.message.LoggerNameAwareMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import nk.demo.BlogAPIServer.CustomException.ApiValidationException;

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
			throw new ApiValidationException("postId cannot be minus.");
		PostEntity postEntity = postRepository.findByPostId(postId);
		if(postEntity == null)
			throw new ApiValidationException("There is no corresponding information for postId.");
		return convertEntityToDto(postEntity);
	}
	
	/**
	 * 하나의 게시글 등록
	 * @param basicPostDto
	 * @return
	 * **/
	public Long save(BasicPostDto basicPostDto) {
		if(basicPostDto.getContents() == null || basicPostDto.getTitle() == null || basicPostDto.getUserId() == 0)
			throw new ApiValidationException("Not enough post data.");

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
			throw new ApiValidationException("postId cannot be minus.");

		if(basicPostDto.getContents() == null || basicPostDto.getTitle() == null || basicPostDto.getUserId() == 0)
			throw new ApiValidationException("Not enough post data.");

		if(postRepository.findByPostId(postId) ==null)
			throw new ApiValidationException("There is no corresponding information for postId.");

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
			throw new ApiValidationException("postId cannot be minus.");
		if(postRepository.findByPostId(postId) ==null)
			throw new ApiValidationException("There is no corresponding information for postId.");
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
