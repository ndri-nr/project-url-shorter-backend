package gratis.contoh.urlshorter.app.model;

import java.io.Serializable;

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
@Table("mst_account")
@Builder
public class MstAccount implements Serializable {
	
	private static final long serialVersionUID = -1920155609408875685L;
	
	@Id
	@Column("id")
	private Long id;

	@Column("email")
	private String email;
	
	@Column("password")
	private String password;
	
	@Column("full_name")
	private String fullName;

}
