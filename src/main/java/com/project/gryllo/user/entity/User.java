package com.project.gryllo.user.entity;

import com.project.gryllo.user.dto.FollowRespDto;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@SqlResultSetMapping(
		name = "FollowRespDtoMapping",
		classes = @ConstructorResult(
				targetClass = FollowRespDto.class,
				columns = {
						@ColumnResult(name="id", type = Integer.class),
						@ColumnResult(name="username", type = String.class),
						@ColumnResult(name="name", type = String.class),
						@ColumnResult(name="profileImage", type = String.class),
						@ColumnResult(name="followState", type = Boolean.class),
						@ColumnResult(name="equalUserState", type = Boolean.class),
				}
		)
)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique = true)
	private String username;
	private String password;
	private String email;
	private String name;
	private String bio;
	private String profileImage;
	@Enumerated(EnumType.STRING)
	private UserRole role;
	@CreationTimestamp
	private Timestamp createDate;

}





