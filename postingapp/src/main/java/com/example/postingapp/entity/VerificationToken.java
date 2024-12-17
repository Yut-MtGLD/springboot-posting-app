package com.example.postingapp.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "verification_tokens")
@Data
public class VerificationToken {
	// ID(主キー)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	// ユーザーID(外部キー)
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	// トークン
	@Column(name = "token")
	private String token;
	
	// 作成日時
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAT;
	
	// 更新日時
	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;
}
