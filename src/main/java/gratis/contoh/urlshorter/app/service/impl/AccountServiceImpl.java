package gratis.contoh.urlshorter.app.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gratis.contoh.urlshorter.app.exception.BadRequestException;
import gratis.contoh.mapper.ObjectMapper;
import gratis.contoh.urlshorter.app.model.MstAccount;
import gratis.contoh.urlshorter.app.model.dto.RegisterDto;
import gratis.contoh.urlshorter.app.model.response.ProfileResponse;
import gratis.contoh.urlshorter.app.model.response.RegisterResponse;
import gratis.contoh.urlshorter.app.repository.MstAccountRepository;
import gratis.contoh.urlshorter.app.repository.SessionRepository;
import gratis.contoh.urlshorter.app.service.AccountService;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ObjectMapper<RegisterDto, MstAccount> registerDtoToMstAccount;
	
	@Autowired
	private ObjectMapper<MstAccount, RegisterResponse> mstAccountToRegisterResponse;
	
	@Autowired
	private ObjectMapper<MstAccount, ProfileResponse> mstAccountToProfileResponse;

	@Autowired
	private MstAccountRepository mstAccountRep;
	
	@Autowired
	private SessionRepository sessionRep;

	@Override
	public Mono<RegisterResponse> register(Mono<RegisterDto> registerDto) {
		return registerDto
				.zipWhen(dto -> {
					dto.setPassword(passwordEncoder.encode(dto.getPassword()));
					return mstAccountRep.findByEmail(dto.getEmail()).count();
				})
				.flatMap(tuple -> {
					if (tuple.getT2() > 0) {
						return Mono.error(
								new BadRequestException(
										"Email " + tuple.getT1().getEmail() + " already exist"));
					}
						
					return mstAccountRep.save(registerDtoToMstAccount.convert(tuple.getT1()));
				})
				.map(user -> mstAccountToRegisterResponse.convert(user));
	}

	@Override
	public Mono<ProfileResponse> profile(String token) {
		LocalDateTime now = LocalDateTime.now();
		return sessionRep.findBySessionIdAndExpiredAtGreaterThan(token, now).singleOrEmpty()
				.flatMap(session -> mstAccountRep.findById(session.getAccountId()))
				.map(account -> mstAccountToProfileResponse.convert(account));
	}

}
