package nk.demo.BlogAPIServer.Security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import nk.demo.BlogAPIServer.CustomException.AccessDeniedException;
import nk.demo.BlogAPIServer.CustomException.AuthenticationEntryPointException;
import nk.demo.BlogAPIServer.Response.CommonResult;
import springfox.documentation.annotations.ApiIgnore;

//import 생략

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/JWTException")
@ApiIgnore
public class ExceptionController {

	@GetMapping(value = "/EmptyJWTToken")
	public CommonResult entrypointException() {
		throw new AuthenticationEntryPointException("JWT: You do not have permission to access this resource.");
	}
	
	@GetMapping(value = "/accessdenied")
	public CommonResult accessdeniedException() {
	        throw new AccessDeniedException("JWT: A resource that can not be accessed with the privileges it has.");
	}
}