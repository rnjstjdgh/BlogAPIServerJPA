package nk.demo.BlogAPIServer.Post.Dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nk.demo.BlogAPIServer.Post.PostEntity;

import java.time.LocalDateTime;

@Getter @Setter
public class BasicPostDto {

    private String 			title;			//제목
    private Long 			userId;			//작성자의 아이디(foreign key)
    private String 			contents;		//내용

    public PostEntity toEntity() {
        return PostEntity.builder()
                .title(title)
                .userId(userId)
                .contents(contents)
                .build();
    }

    @Builder
    public BasicPostDto(String title, Long userId, String contents) {
        this.title = title;
        this.userId = userId;
        this.contents = contents;
    }
}
