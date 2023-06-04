package gratis.contoh.urlshorter.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gratis.contoh.urlshorter.app.model.response.PaginationResponse;
import gratis.contoh.urlshorter.app.model.dto.UrlDto;
import gratis.contoh.urlshorter.app.model.request.PaginationRequest;
import gratis.contoh.urlshorter.app.model.response.BaseResponse;
import gratis.contoh.urlshorter.app.model.response.UrlResponse;
import gratis.contoh.urlshorter.app.service.UrlService;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/url")
public class UrlController {
	
	@Autowired
	private UrlService urlService;
	
	@PostMapping("")
	public Mono<ResponseEntity<BaseResponse<UrlResponse>>> generate(
    		ServerHttpRequest request,
    		@RequestBody @Valid Mono<UrlDto> urlDto) {
		String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		
        return urlService.generateLink(urlDto, token)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<UrlResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()));
	}
	
	@GetMapping("")
	public Mono<ResponseEntity<BaseResponse<PaginationResponse<UrlResponse>>>> getList(
    		ServerHttpRequest request,
    		@Valid Mono<PaginationRequest> paginationRequest) {
		List<String> error = new ArrayList<String>();
		
		String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (token == null || token.isBlank() || token.isEmpty()) {
			error.add("please login to access this resource");
			return Mono.just(ResponseEntity
    				.status(HttpStatus.UNAUTHORIZED.value())
    				.body(BaseResponse.<PaginationResponse<UrlResponse>>builder()
    						.status(HttpStatus.UNAUTHORIZED.value())
    						.message(HttpStatus.UNAUTHORIZED.name())
    						.errors(error)
    						.build()));
		}
		error.add("you don't have access to this resource");
		
        return urlService.getList(paginationRequest, token)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<PaginationResponse<UrlResponse>>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.FORBIDDEN.value())
        				.body(BaseResponse.<PaginationResponse<UrlResponse>>builder()
        						.status(HttpStatus.FORBIDDEN.value())
        						.message(HttpStatus.FORBIDDEN.name())
        						.errors(error)
        						.build()));
	}
	
	@GetMapping("/{urlId}")
	public Mono<ResponseEntity<BaseResponse<UrlResponse>>> getRedirect(
    		ServerHttpRequest request,
    		@PathVariable @Valid String urlId) {
		
		List<String> error = new ArrayList<String>();
		error.add(urlId + " not found");
		
        return urlService.getRedirect(urlId)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<UrlResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.NOT_FOUND.value())
        				.body(BaseResponse.<UrlResponse>builder()
        						.status(HttpStatus.NOT_FOUND.value())
        						.message(HttpStatus.NOT_FOUND.name())
        						.errors(error)
        						.build()));
	}
	
	@DeleteMapping("/{urlId}")
	public Mono<ResponseEntity<BaseResponse<Boolean>>> delete(
    		ServerHttpRequest request,
    		@PathVariable @Valid String urlId) {
		
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
		error.add("url id or token mismatch");
		
        return urlService.delete(urlId, token)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<Boolean>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.BAD_REQUEST.value())
        				.body(BaseResponse.<Boolean>builder()
        						.status(HttpStatus.BAD_REQUEST.value())
        						.message(HttpStatus.BAD_REQUEST.name())
        						.errors(error)
        						.build()));
	}
	
	@PutMapping("/{urlId}")
	public Mono<ResponseEntity<BaseResponse<UrlResponse>>> changeUrlId(
    		ServerHttpRequest request,
    		@PathVariable @Valid String urlId,
    		@RequestBody @Valid Mono<UrlDto> urlDto) {
		List<String> error = new ArrayList<String>();
		
		String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (token == null || token.isBlank() || token.isEmpty()) {
			error.add("please login to access this resource");
			return Mono.just(ResponseEntity
    				.status(HttpStatus.UNAUTHORIZED.value())
    				.body(BaseResponse.<UrlResponse>builder()
    						.status(HttpStatus.UNAUTHORIZED.value())
    						.message(HttpStatus.UNAUTHORIZED.name())
    						.errors(error)
    						.build()));
		}
		error.add("you don't have access to this resource");
		
        return urlService.changeUrlId(urlId, urlDto, token)
        		.map(item -> ResponseEntity
        				.ok(BaseResponse.<UrlResponse>builder()
        						.status(HttpStatus.OK.value())
        						.message("success")
        						.data(item)
        						.build()))
        		.defaultIfEmpty(ResponseEntity
        				.status(HttpStatus.FORBIDDEN.value())
        				.body(BaseResponse.<UrlResponse>builder()
        						.status(HttpStatus.FORBIDDEN.value())
        						.message(HttpStatus.FORBIDDEN.name())
        						.errors(error)
        						.build()))
        		.doOnError(err -> ResponseEntity
        				.status(HttpStatus.CONFLICT.value())
        				.body(BaseResponse.<UrlResponse>builder()
        						.status(HttpStatus.CONFLICT.value())
        						.message(HttpStatus.CONFLICT.name())
        						.errors(Arrays.asList(err.getMessage().split(",")))
        						.build()));
	}

}
