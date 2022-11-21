package com.project.gryllo.post.entity;

import com.project.gryllo.user.entity.User;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name="comment")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String content;

	@ManyToOne
	@JoinColumn(name="imageId")
	private Image image;
	

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

	@CreationTimestamp
	private Timestamp createDate;
	
	@Transient
	private boolean commentHost;
}



