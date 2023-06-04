package gratis.contoh.urlshorter.app.service;

import gratis.contoh.urlshorter.app.model.dto.UrlDto;
import gratis.contoh.urlshorter.app.model.request.PaginationRequest;
import gratis.contoh.urlshorter.app.model.response.PaginationResponse;
import gratis.contoh.urlshorter.app.model.response.UrlResponse;
import reactor.core.publisher.Mono;

public interface UrlService {

	public Mono<UrlResponse> generateLink(Mono<UrlDto> urlDto, String token);
	public Mono<UrlResponse> getRedirect(String urlId);
	public Mono<Boolean> delete(String urlId, String token);
	public Mono<PaginationResponse<UrlResponse>> getList(Mono<PaginationRequest> paginationRequest, String token);
	public Mono<UrlResponse> changeUrlId(String urlId, Mono<UrlDto> urlDto, String token);
	
}
