package gratis.contoh.urlshorter.app.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("session")
@Builder
public class Session implements Serializable {

	private static final long serialVersionUID = -8681032572007368209L;
	
	@Id
	@Column("id")
	private Long id;
	
	@Column("session_id")
	private String sessionId;
	
	@Column("account_id")
	private Long accountId;
	
	@Column("expired_at")
	private LocalDateTime expiredAt;

}
