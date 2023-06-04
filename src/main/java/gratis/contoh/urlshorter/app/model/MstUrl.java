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
@Table("mst_url")
@Builder
public class MstUrl implements Serializable {

	private static final long serialVersionUID = -4049145006018540405L;

	@Id
	@Column("id")
	private Long id;
	
	@Column("url_id")
	private String urlId;

	@Column("account_id")
	private Long accountId;
	
	@Column("visited")
	private Integer visited;
	
	@Column("redirect_to")
	private String redirectTo;
	
	@Column("last_visited")
	private LocalDateTime lastVisited;
	
	@Column("deleted_at")
	private LocalDateTime deletedAt;

}
