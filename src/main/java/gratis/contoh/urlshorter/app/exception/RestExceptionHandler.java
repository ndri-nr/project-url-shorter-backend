package gratis.contoh.urlshorter.app.exception;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import gratis.contoh.urlshorter.app.model.response.BaseResponse;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(WebExchangeBindException.class)
	public Mono<ResponseEntity<Object>> handleMissingRequestValueException(WebExchangeBindException ex) {
		List<String> errors = new ArrayList<String>();
		
		ex.getFieldErrors().forEach(item -> errors.add(item.getField() + " " + item.getDefaultMessage()));
	    
		BaseResponse<String> errorResponse = BaseResponse.<String>builder()
				.errors(errors)
				.message(HttpStatus.BAD_REQUEST.name())
				.status(HttpStatus.BAD_REQUEST.value())
				.build();
	    return Mono.just(ResponseEntity
	    		.status(HttpStatus.BAD_REQUEST)
	    		.body(errorResponse));
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public Mono<ResponseEntity<Object>> handleAccessDeniedException(AccessDeniedException ex) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
	    
		BaseResponse<String> errorResponse = BaseResponse.<String>builder()
				.errors(errors)
				.message(HttpStatus.FORBIDDEN.name())
				.status(HttpStatus.FORBIDDEN.value())
				.build();
	    return Mono.just(ResponseEntity
	    		.status(HttpStatus.FORBIDDEN)
	    		.body(errorResponse));
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public Mono<ResponseEntity<Object>> handleAuthenticationException(AuthenticationException ex) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
	    
		BaseResponse<String> errorResponse = BaseResponse.<String>builder()
				.errors(errors)
				.message(HttpStatus.UNAUTHORIZED.name())
				.status(HttpStatus.UNAUTHORIZED.value())
				.build();
	    return Mono.just(ResponseEntity
	    		.status(HttpStatus.UNAUTHORIZED)
	    		.body(errorResponse));
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public Mono<ResponseEntity<Object>> handleDataIntegrityException(DataIntegrityViolationException ex) {
		List<String> errors = new ArrayList<String>();
		errors.add(ex.getMessage());
	    
		BaseResponse<String> errorResponse = BaseResponse.<String>builder()
				.errors(errors)
				.message(HttpStatus.CONFLICT.name())
				.status(HttpStatus.CONFLICT.value())
				.build();
	    return Mono.just(ResponseEntity
	    		.status(HttpStatus.CONFLICT)
	    		.body(errorResponse));
	}
	
	@ExceptionHandler(BadRequestException.class)
	public Mono<ResponseEntity<Object>> handleBadRequestException(BadRequestException ex) {
		List<String> errors = Arrays.asList(ex.getMessage().split(","));
	    
		BaseResponse<String> errorResponse = BaseResponse.<String>builder()
				.errors(errors)
				.message(HttpStatus.BAD_REQUEST.name())
				.status(HttpStatus.BAD_REQUEST.value())
				.build();
	    return Mono.just(ResponseEntity
	    		.status(HttpStatus.BAD_REQUEST)
	    		.body(errorResponse));
	}

}
