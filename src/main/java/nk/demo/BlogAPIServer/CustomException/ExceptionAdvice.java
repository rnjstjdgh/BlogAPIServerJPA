package nk.demo.BlogAPIServer.CustomException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import nk.demo.BlogAPIServer.Response.CommonResult;
import nk.demo.BlogAPIServer.Response.ResponseService;

@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "nk.demo.BlogAPIServer")
public class ExceptionAdvice {

    private final ResponseService responseService;

	@ExceptionHandler(ApiValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected CommonResult postExceptionHandler(Exception ex) {
		return responseService.getFailResult(ex.getMessage());
    }
	
	@ExceptionHandler(SignFailedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	protected CommonResult emailSignFailedHandler(SignFailedException ex) {
	    return responseService.getFailResult(ex.getMessage());
	}

	@ExceptionHandler(CustomNullPointException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	protected CommonResult customNullPointExceptionHandler(SignFailedException ex) {
		return responseService.getFailResult(ex.getMessage());
	}
	
	@ExceptionHandler(AuthenticationEntryPointException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public CommonResult authenticationEntryPointException(HttpServletRequest request, AuthenticationEntryPointException ex) {
	        return responseService.getFailResult(ex.getMessage());
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public CommonResult AccessDeniedException(HttpServletRequest request, AccessDeniedException ex) {
	        return responseService.getFailResult(ex.getMessage());
	}
}