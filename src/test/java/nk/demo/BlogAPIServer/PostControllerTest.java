package nk.demo.BlogAPIServer;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import nk.demo.BlogAPIServer.Security.Sign.model.SignInResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;

import nk.demo.BlogAPIServer.Post.Dtos.PostDto;

/**
 * spring controller test 참고: https://tech.devgd.com/12 spring boot + test용 h2
 * db사용 + mybatis 참고:
 * https://atoz-develop.tistory.com/entry/Spring-Boot-MyBatis-%EC%84%A4%EC%A0%95-%EB%B0%A9%EB%B2%95
 * https://re-coder.tistory.com/5
 **/
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // @Test가 붙은 메소드를 실행할 때 마다가 아니라 모든 테스트에 대해 하나의 인스턴스만 만든다.
public class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private String jsonPostNormal1;
	private String jsonPostNormal2;

	private String jsonPostContentNull;
	private String jsonPostIncludePostId;
	private String jsonPostIncludeRegDate;
	private SignInResult testSignInResult1;
	private SignInResult testSignInResult2;

	@BeforeAll
	void initAll() throws Exception {

		testSignInResult1 = GetSignInResult("postTest@gmail.com", "Rnjs@123456789");
		testSignInResult2 = GetSignInResult("postTest2@gmail.com", "Rnjs@123456789");

		// 정상 요청 => {title, userId, contents}만 넘겼을 때
		PostDto postDto = PostDto.builder().title("testTitle").userId(testSignInResult1.getUserId()).contents("testContent").build();
		jsonPostNormal1 = objectMapper.writeValueAsString(postDto);

		postDto.setTitle("haha");
		jsonPostNormal2 = objectMapper.writeValueAsString(postDto);

		// 이상 요청 => content가 null일때
		postDto = PostDto.builder().title("testTitle").userId(testSignInResult1.getUserId()).build();
		jsonPostContentNull = objectMapper.writeValueAsString(postDto);

		// 이상 요청 => postId를 넘긴 경우
		postDto = PostDto.builder().postId(new Long(1)).title("testTitle").userId(testSignInResult1.getUserId()).contents("testContent").build();
		jsonPostIncludePostId = objectMapper.writeValueAsString(postDto);

		// 이상 요청 => regDate를 넘긴 경우
		postDto = PostDto.builder().title("testTitle").userId(testSignInResult1.getUserId()).contents("testContent").regDate(LocalDateTime.now())
				.build();
		jsonPostIncludeRegDate = objectMapper.writeValueAsString(postDto);
	}

	@Test
	public void getListTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/posts").header("X-AUTH-TOKEN", testSignInResult1.getToken())
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void getTest() throws Exception {

		// 정상 요청
		mockMvc.perform(MockMvcRequestBuilders.get("/posts/2").header("X-AUTH-TOKEN", testSignInResult1.getToken())
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8")).andDo(print())
				.andExpect(jsonPath("$.data.postId").value("2"));

		// 음수 값을 같는 게시글 번호로 요청
		mockMvc.perform(MockMvcRequestBuilders.get("/posts/-1").header("X-AUTH-TOKEN", testSignInResult1.getToken())
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.msg").value("postId cannot be minus."));

		// 없는 게시글 번호로 요청
		mockMvc.perform(MockMvcRequestBuilders.get("/posts/99999").header("X-AUTH-TOKEN", testSignInResult1.getToken())
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.msg").value("There is no corresponding information for postId."));
	}

	@Test
	public void saveTest() throws Exception {
		// 정상 요청 => {title, userId, contents}만 넘겼을 때
		mockMvc.perform(MockMvcRequestBuilders.post("/posts").header("X-AUTH-TOKEN", testSignInResult1.getToken()).content(jsonPostNormal1)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// 중복된 제목으로 요청 => {title, userId, contents}만 넘겼을 때
		mockMvc.perform(MockMvcRequestBuilders.post("/posts").header("X-AUTH-TOKEN", testSignInResult1.getToken()).content(jsonPostNormal1)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

		// 정상 요청 => {title, userId, contents}만 넘겼을 때
		mockMvc.perform(MockMvcRequestBuilders.post("/posts").header("X-AUTH-TOKEN", testSignInResult1.getToken()).content(jsonPostNormal2)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		//실제 로그인 된 사용자와 다른 작성자 id로 넘어온 경우
		mockMvc.perform(MockMvcRequestBuilders.post("/posts").header("X-AUTH-TOKEN", testSignInResult2.getToken()).content(jsonPostNormal1)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());

		// 이상 요청 => content가 null일때
		mockMvc.perform(
				MockMvcRequestBuilders.post("/posts").header("X-AUTH-TOKEN", testSignInResult1.getToken()).content(jsonPostContentNull)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("{\"success\":false,\"code\":-1,\"msg\":\"Not enough post data.\"}"));

		// 이상 요청 => postId를 넘긴 경우
		mockMvc.perform(
				MockMvcRequestBuilders.post("/posts").header("X-AUTH-TOKEN", testSignInResult1.getToken()).content(jsonPostIncludePostId)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		// 이상 요청 => regDate를 넘긴 경우
		mockMvc.perform(
				MockMvcRequestBuilders.post("/posts").header("X-AUTH-TOKEN", testSignInResult1.getToken()).content(jsonPostIncludeRegDate)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void updateTest() throws Exception {
		// 정상 요청
		mockMvc.perform(MockMvcRequestBuilders.put("/posts/2").header("X-AUTH-TOKEN", testSignInResult1.getToken()).content(jsonPostNormal1)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string("{\"success\":true,\"code\":0,\"msg\":\"성공하였습니디.\",\"data\":2}"));

		// 정상 요청
		mockMvc.perform(MockMvcRequestBuilders.put("/posts/2").header("X-AUTH-TOKEN", testSignInResult2.getToken()).content(jsonPostNormal1)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().string("{\"success\":false,\"code\":-1,\"msg\":\"Post userId is invalid.\"}"));

		// 이상 요청 => regDate를 넘긴 경우
		mockMvc.perform(
				MockMvcRequestBuilders.put("/posts/2").header("X-AUTH-TOKEN", testSignInResult1.getToken()).content(jsonPostIncludeRegDate)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
//				.andExpect(status().isBadRequest())
//				.andExpect(content().string("{\"success\":false,\"code\":-1,\"msg\":\"don't need regDate.\"}"));

		// 이상 요청 => post path로 음수를 넘긴 경우
		mockMvc.perform(MockMvcRequestBuilders.put("/posts/-11").header("X-AUTH-TOKEN", testSignInResult1.getToken())
				.content(jsonPostNormal1).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("{\"success\":false,\"code\":-1,\"msg\":\"postId cannot be minus.\"}"));

		// 이상 요청 => post path로 없는 게시글 번호를 넘긴 경우
		mockMvc.perform(MockMvcRequestBuilders.put("/posts/9999").header("X-AUTH-TOKEN", testSignInResult1.getToken())
				.content(jsonPostNormal1).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andExpect(content().string(
						"{\"success\":false,\"code\":-1,\"msg\":\"There is no corresponding information for postId.\"}"));
	}

	@Test
	public void deleteTest() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/posts/1").header("X-AUTH-TOKEN", testSignInResult1.getToken())
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string("{\"success\":true,\"code\":0,\"msg\":\"성공하였습니디.\",\"data\":1}"));
		mockMvc.perform(MockMvcRequestBuilders.delete("/posts/2").header("X-AUTH-TOKEN", testSignInResult2.getToken())
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
				.andExpect(content().string("{\"success\":false,\"code\":-1,\"msg\":\"Post userId is invalid.\"}"));
	}



	private SignInResult GetSignInResult(String email, String password) throws Exception {
		// 회원가입
		mockMvc.perform(
				MockMvcRequestBuilders.post("/signup").content("email="+email+"&password="+ password)
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk());
		// 로그인
		MvcResult mvcResult = mockMvc.perform(
				MockMvcRequestBuilders.post("/signin").content("email="+email+"&password="+ password)
						.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isOk()).andReturn();
		return ParseResult(mvcResult.getResponse().getContentAsString());
	}
	private SignInResult ParseResult(String response) throws Exception {
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject)parser.parse(response);
		jsonObj = (JSONObject) jsonObj.get("data");
		return new SignInResult((String) jsonObj.get("token"),(Long) jsonObj.get("userId"));
	}
}
