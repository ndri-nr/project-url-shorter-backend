package gratis.contoh.urlshorter.app.service;

import gratis.contoh.urlshorter.app.model.dto.AuthDto;
import gratis.contoh.urlshorter.app.model.response.AuthResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
	
	public Mono<AuthResponse> login(Mono<AuthDto> authDto);
	public Mono<Boolean> logout(String token);

}
