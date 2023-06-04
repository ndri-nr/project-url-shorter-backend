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

import gratis.contoh.urlshorter.app.model.dto.RegisterDto;
import gratis.contoh.urlshorter.app.model.response.BaseResponse;
import gratis.contoh.urlshorter.app.model.response.ProfileResponse;
import gratis.contoh.urlshorter.app.model.response.RegisterResponse;
import gratis.contoh.urlshorter.app.service.AccountService;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/account")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@PostMapping("/register")
	public Mono<ResponseEntity<BaseResponse<RegisterResponse>>> register(
    		ServerHttpRequest request,
    		@RequestBody @Valid Mono<RegisterDto> registerDto) {
		
        return accountService.register(registerDto)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<RegisterResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()));
	}
	
	@GetMapping("/profile")
	public Mono<ResponseEntity<BaseResponse<ProfileResponse>>> profile(
    		ServerHttpRequest request) {
		
		List<String> error = new ArrayList<String>();
		
		String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (token == null || token.isBlank() || token.isEmpty()) {
			error.add("please login to access this resource");
			return Mono.just(ResponseEntity
    				.status(HttpStatus.UNAUTHORIZED.value())
    				.body(BaseResponse.<ProfileResponse>builder()
    						.status(HttpStatus.UNAUTHORIZED.value())
    						.message(HttpStatus.UNAUTHORIZED.name())
    						.errors(error)
    						.build()));
		}
		error.add("you don't have access to this resource");
		
        return accountService.profile(token)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<ProfileResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.FORBIDDEN.value())
        				.body(BaseResponse.<ProfileResponse>builder()
        						.status(HttpStatus.FORBIDDEN.value())
        						.message(HttpStatus.FORBIDDEN.name())
        						.errors(error)
        						.build()));
	}

}
