package gratis.contoh.urlshorter.app.service;

import gratis.contoh.urlshorter.app.model.dto.RegisterDto;
import gratis.contoh.urlshorter.app.model.response.ProfileResponse;
import gratis.contoh.urlshorter.app.model.response.RegisterResponse;
import reactor.core.publisher.Mono;

public interface AccountService {
	
	public Mono<RegisterResponse> register(Mono<RegisterDto> registerDto);
	public Mono<ProfileResponse> profile(String token);

}
