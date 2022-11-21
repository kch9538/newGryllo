package com.project.gryllo.post.entity;

import com.project.gryllo.user.entity.User;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="chatting")
public class Chatting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String message;

	@ManyToOne
	@JoinColumn(name = "fromUserId")
	private User fromUser;

	@ManyToOne
	@JoinColumn(name = "toUserId")
	private User toUser;

	@CreationTimestamp
	private Date createDate;

}
