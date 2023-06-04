package gratis.contoh.urlshorter.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import gratis.contoh.auth.annotation.Authorize;
import gratis.contoh.urlshorter.app.model.dto.AuthDto;
import gratis.contoh.urlshorter.app.model.response.AuthResponse;
import gratis.contoh.urlshorter.app.model.response.BaseResponse;
import gratis.contoh.urlshorter.app.service.AuthService;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/login")
	public Mono<ResponseEntity<BaseResponse<AuthResponse>>> login(
    		ServerHttpRequest request,
    		@RequestBody @Valid Mono<AuthDto> authDto) {
		List<String> error = new ArrayList<String>();
		error.add("username or password incorrect");
		
        return authService.login(authDto)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<AuthResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.UNAUTHORIZED.value())
        				.body(BaseResponse.<AuthResponse>builder()
        						.status(HttpStatus.UNAUTHORIZED.value())
        						.message(HttpStatus.UNAUTHORIZED.name())
        						.errors(error)
        						.build()));
	}
	
	@GetMapping("/logout")
	public Mono<ResponseEntity<BaseResponse<Boolean>>> logout(
    		ServerHttpRequest request) {
		
		List<String> error = new ArrayList<String>();
		
		String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (token == null || token.isBlank() || token.isEmpty()) {
			error.add("please login to access this resource");
			return Mono.just(ResponseEntity
    				.status(HttpStatus.UNAUTHORIZED.value())
    				.body(BaseResponse.<Boolean>builder()
    						.status(HttpStatus.UNAUTHORIZED.value())
    						.message(HttpStatus.UNAUTHORIZED.name())
    						.errors(error)
    						.build()));
		}
		error.add("you don't have access this resource");
		
        return authService.logout(token)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<Boolean>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.FORBIDDEN.value())
        				.body(BaseResponse.<Boolean>builder()
        						.status(HttpStatus.FORBIDDEN.value())
        						.message(HttpStatus.FORBIDDEN.name())
        						.errors(error)
        						.build()));
	}

}
