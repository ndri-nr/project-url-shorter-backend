package gratis.contoh.urlshorter.app.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gratis.contoh.urlshorter.app.model.MstAccount;
import gratis.contoh.urlshorter.app.model.Session;
import gratis.contoh.urlshorter.app.model.dto.AuthDto;
import gratis.contoh.urlshorter.app.model.response.AuthResponse;
import gratis.contoh.urlshorter.app.repository.MstAccountRepository;
import gratis.contoh.urlshorter.app.repository.SessionRepository;
import gratis.contoh.urlshorter.app.service.AuthService;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceImpl implements AuthService {
	
	private final Integer SEVEN_NUMBER = 7;
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 50;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MstAccountRepository mstAccountRep;
	
	@Autowired
	private SessionRepository sessionRep;

	@Override
	public Mono<AuthResponse> login(Mono<AuthDto> authDto) {
		LocalDateTime now = LocalDateTime.now();
		
		return authDto
				.zipWhen(dto -> {
					return mstAccountRep.findByEmail(dto.getEmail()).singleOrEmpty();
				})
				.flatMap(tuple -> {
					AuthDto dto = tuple.getT1();
					MstAccount user = tuple.getT2();
					
					if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
						return Mono.justOrEmpty(null);
					}
					
					String token = generateUniqueToken();
					LocalDateTime sevenDays = now.plusDays(SEVEN_NUMBER);
					return sessionRep.findByAccountIdAndExpiredAtGreaterThan(user.getId(), now).collectList()
						.flatMap(list -> {
							if (list.size() > 0) {
								Session session = list.get(0);
								session.setExpiredAt(sevenDays);
								return sessionRep.save(session);
							} else {
								return sessionRep.save(Session.builder()
										.sessionId(token)
										.accountId(user.getId())
										.expiredAt(sevenDays)
										.build());
							}
						})
						.map(session -> new AuthResponse(session.getSessionId()));
				});
	}
	
	private static String generateUniqueToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder tokenBuilder = new StringBuilder(TOKEN_LENGTH);
        Set<String> usedTokens = new HashSet<>();

        while (true) {
            // Generate random token
            for (int i = 0; i < TOKEN_LENGTH; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                tokenBuilder.append(randomChar);
            }
            String token = tokenBuilder.toString();

            // Check uniqueness
            if (!usedTokens.contains(token)) {
                usedTokens.add(token);
                return token;
            }

            // Clear token builder for next iteration
            tokenBuilder.setLength(0);
        }
    }

	@Override
	public Mono<Boolean> logout(String token) {
		LocalDateTime now = LocalDateTime.now();
		
		return Mono.just(true)
				.zipWith(sessionRep.findBySessionIdAndExpiredAtGreaterThan(token, now).singleOrEmpty()
						.flatMap(session -> {
							session.setExpiredAt(now);
							return sessionRep.save(session);
						}))
				.map(tuple -> tuple.getT1());
	}

}
