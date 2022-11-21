package com.project.gryllo.notification.entity;

import com.project.gryllo.user.entity.User;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="noti")
public class Noti {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Enumerated(EnumType.STRING)
	private NotiType notiType;
	
	@ManyToOne
	@JoinColumn(name="fromUserId")
	private User fromUser;
	
	@ManyToOne
	@JoinColumn(name="toUserId")
	private User toUser;

	@CreationTimestamp
	private Timestamp createDate;
}
