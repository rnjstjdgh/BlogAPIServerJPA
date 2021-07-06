package nk.demo.BlogAPIServer.Post.Dtos;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nk.demo.BlogAPIServer.Post.PostEntity;

@Getter @Setter
public class PostDto {

	private Long 			postId;			//개시글 넘버(primary key)
	private String 			title;			//제목
	private Long 			userId;			//작성자의 아이디(foreign key)
	private String 			contents;		//내용
	private LocalDateTime 	regDate;		//등록일자
	private LocalDateTime 	updateDate;		//등록일자

	public PostEntity toEntity() {
		return PostEntity.builder()
							.postId(postId)
							.title(title)
							.userId(userId)
							.contents(contents)
							.regDate(regDate)
							.updateDate(updateDate)
							.build();
	}
	
	@Builder
	public PostDto(Long postId, String title, Long userId, String contents, LocalDateTime regDate, LocalDateTime updateDate) {
		this.postId = postId;
		this.title = title;
		this.userId = userId;
		this.contents = contents;
		this.regDate = regDate;
		this.updateDate = updateDate;
	}
	
}
