package nk.demo.BlogAPIServer.Post;


import java.time.LocalDateTime;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@SequenceGenerator(name = "postTable_SEQ_GENERATOR", sequenceName = "postTable_SEQ", initialValue = 1, allocationSize = 1)
@Entity
@Table(name = "postTable")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PostEntity {

	@Id
	@GeneratedValue(strategy= GenerationType.SEQUENCE , generator="postTable_SEQ_GENERATOR")
	private Long 			postId;			//개시글 넘버(primary key)

	@Column(length = 30, nullable = false)
	private String 			title;			//제목

	@Column(nullable = false)
	private Long 			userId;			//작성자의 아이디(foreign key)

	@Lob
	@Column(nullable = false)
	private String 			contents;		//내용

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime 	regDate;		//등록일자

	@LastModifiedDate
	private LocalDateTime 		updateDate;		//수정일자
	
	@Builder
	public PostEntity(Long postId, String title, Long userId, String contents, LocalDateTime regDate, LocalDateTime updateDate) {
		this.postId = postId;
		this.title = title;
		this.userId = userId;
		this.contents = contents;
		this.regDate = regDate;
		this.updateDate =  updateDate;
	}
}
