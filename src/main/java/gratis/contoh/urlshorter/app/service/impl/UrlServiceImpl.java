package gratis.contoh.urlshorter.app.service.impl;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gratis.contoh.mapper.ObjectMapper;
import gratis.contoh.urlshorter.app.exception.BadRequestException;
import gratis.contoh.urlshorter.app.model.MstUrl;
import gratis.contoh.urlshorter.app.model.Session;
import gratis.contoh.urlshorter.app.model.dto.UrlDto;
import gratis.contoh.urlshorter.app.model.request.PaginationRequest;
import gratis.contoh.urlshorter.app.model.response.PaginationResponse;
import gratis.contoh.urlshorter.app.model.response.UrlResponse;
import gratis.contoh.urlshorter.app.repository.CustomMstUrlRepository;
import gratis.contoh.urlshorter.app.repository.MstUrlRepository;
import gratis.contoh.urlshorter.app.repository.SessionRepository;
import gratis.contoh.urlshorter.app.service.UrlService;
import gratis.contoh.urlshorter.app.util.pagination.Pagination;
import gratis.contoh.urlshorter.app.util.querybuilder.Condition;
import gratis.contoh.urlshorter.app.util.querybuilder.Criteria;
import gratis.contoh.urlshorter.app.util.querybuilder.Operator;
import gratis.contoh.urlshorter.app.util.querybuilder.ValueType;
import reactor.core.publisher.Mono;

@Service
public class UrlServiceImpl implements UrlService {
	
	private static final int STRING_LENGTH = 7;
	
	@Autowired
	private ObjectMapper<UrlDto, MstUrl> urlDtoToMstUrl;
	
	@Autowired
	private ObjectMapper<MstUrl, UrlResponse> mstUrlToUrlResponse;
	
	@Autowired
	private MstUrlRepository urlRep;

	@Autowired
	private CustomMstUrlRepository customUrlRep;
	
	@Autowired
	private SessionRepository sessionRep;

	@Override
	public Mono<UrlResponse> generateLink(Mono<UrlDto> urlDto, String token) {
		LocalDateTime now = LocalDateTime.now();
		
		return urlDto.map(dto -> urlDtoToMstUrl.convert(dto))
				.zipWhen(dto -> {
					try {
			            URL url = new URL(dto.getRedirectTo());
			            return Mono.just(true);
			        } catch (Exception e) {
			            return Mono.just(false);
			        }
				})
				.map(tuple -> {
					if(!tuple.getT2()) throw new BadRequestException("URL not valid");
					
					MstUrl mstUrl = tuple.getT1();
					mstUrl.setUrlId(generateUrlId());
					mstUrl.setVisited(0);
					return mstUrl;
				})
				.zipWith(sessionRep.findBySessionIdAndExpiredAtGreaterThan(token, now).collectList())
				.map(tuple -> {
					if (tuple.getT2().size() == 0) return tuple.getT1();
					tuple.getT1().setAccountId(tuple.getT2().get(0).getAccountId());
					return tuple.getT1();
				})
				.flatMap(mstUrl -> urlRep.save(mstUrl))
				.map(mstUrl -> mstUrlToUrlResponse.convert(mstUrl));
	}

	@Override
	@Transactional
	public Mono<UrlResponse> getRedirect(String urlId) {
		return urlRep.findByUrlIdAndDeletedAtIsNull(urlId).singleOrEmpty()
				.flatMap(item -> {
					item.setVisited(item.getVisited() + 1);
					item.setLastVisited(LocalDateTime.now());
					return urlRep.save(item);
				})
				.map(item -> mstUrlToUrlResponse.convert(item));
	}
	
	private String generateUrlId() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[STRING_LENGTH];
        secureRandom.nextBytes(randomBytes);

        String hash = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(randomBytes);
            hash = bytesToHexString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash.substring(0, STRING_LENGTH);
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

	@Override
	public Mono<Boolean> delete(String urlId, String token) {
		LocalDateTime now = LocalDateTime.now();
		return sessionRep.findBySessionIdAndExpiredAtGreaterThan(token, now).singleOrEmpty()
				.flatMap(session 
						-> urlRep.findByUrlIdAndAccountIdAndDeletedAtIsNull(urlId, session.getAccountId()).singleOrEmpty())
				.flatMap(url -> {
					url.setDeletedAt(now);
					return urlRep.save(url);
				})
				.map(item -> true);
	}

	@Override
	public Mono<PaginationResponse<UrlResponse>> getList(Mono<PaginationRequest> paginationRequest, String token) {
		LocalDateTime now = LocalDateTime.now();
		return sessionRep.findBySessionIdAndExpiredAtGreaterThan(token, now).singleOrEmpty()
				.zipWhen(session -> {
					return paginationRequest.map(req -> {
						if (req.getSort() == null) {
							req.setSort("totalVisited desc");
						}
						
						return req;
					});
				})
				.zipWhen(tuple -> Mono.just(getOffset(tuple.getT2())))
				.zipWhen(tuple -> Mono.just(generateCriteria(tuple.getT1().getT2(), tuple.getT1().getT1())))
				.zipWhen(tuple -> customUrlRep
						.findAll(tuple.getT2(), tuple.getT1().getT1().getT2())
						.map(item -> mstUrlToUrlResponse.convert(item))
						.collectList())
				.zipWhen(tuple -> customUrlRep.countAll(tuple.getT1().getT2()))
				.map(tuple -> {
					int size = tuple.getT1().getT1().getT1().getT1().getT2().getSize();
					int offset = tuple.getT1().getT1().getT1().getT2();
					long totalData = tuple.getT2();
					
					return PaginationResponse.<UrlResponse>builder()
							.contents(tuple.getT1().getT2())
							.firstPage(Pagination.isFirstPage(offset))
                            .lastPage(Pagination.isLastPage(size, offset, totalData))
                            .totalPages(Pagination.getTotalPages(size, totalData))
							.totalData(totalData)
							.build();
				});
	}
	
	private int getOffset(PaginationRequest req) {
		return (req.getSize() * req.getPage()) - req.getSize();
	}
	
	private ArrayList<Criteria> generateCriteria(PaginationRequest req, Session session) {
		ArrayList<Criteria> criterias = new ArrayList<Criteria>();
		criterias.add(new Criteria(
				Operator.AND, 
				Condition.IS_NULL, 
				"deleted_at", 
				new String[]{""}, 
				ValueType.TIMESTAMP));
		
		criterias.add(new Criteria(
				Operator.AND, 
				Condition.EQUAL, 
				"account_id", 
				new String[]{session.getAccountId() + ""}, 
				ValueType.NUMBER));
		
		if (req.getSearch() != null && !req.getSearch().isEmpty() && !req.getSearch().isBlank()) {
			ArrayList<Criteria> criteriasTemp = new ArrayList<Criteria>();
			criteriasTemp.add(new Criteria(
					Operator.AND, 
					Condition.LIKE, 
					"LOWER(url)", 
					new String[]{req.getSearch().toLowerCase()}, 
					ValueType.TEXT));
			
			criteriasTemp.add(new Criteria(
					Operator.OR, 
					Condition.LIKE, 
					"LOWER(\"redirectTo\")", 
					new String[]{req.getSearch().toLowerCase()}, 
					ValueType.TEXT));
			
			criterias.add(new Criteria(Operator.AND, criteriasTemp));
		}
		
		return criterias;
	}


	@Override
	public Mono<UrlResponse> changeUrlId(String urlId, Mono<UrlDto> urlDto, String token) {
		LocalDateTime now = LocalDateTime.now();
		return sessionRep.findBySessionIdAndExpiredAtGreaterThan(token, now).singleOrEmpty()
				.flatMap(session 
						-> urlRep.findByUrlIdAndAccountIdAndDeletedAtIsNull(urlId, session.getAccountId())
						.singleOrEmpty())
				.zipWith(urlDto)
				.zipWhen(tuple -> {
					UrlDto dto = tuple.getT2();
					
					return urlRep.findByUrlIdAndDeletedAtIsNull(dto.getUrl()).count();
				})
				.flatMap(tuple -> {
					MstUrl urlItem = tuple.getT1().getT1();
					UrlDto dto = tuple.getT1().getT2();
					Long size = tuple.getT2();
					
					if (size > 0) return Mono.error(
							new DataIntegrityViolationException(dto.getUrl() + " already exist"));
					
					urlItem.setUrlId(dto.getUrl());
					return urlRep.save(urlItem);
				})
				.map(item -> mstUrlToUrlResponse.convert(item));
	}

}
