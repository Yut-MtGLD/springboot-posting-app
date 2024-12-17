package com.example.postingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.postingapp.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
	// トークンの文字列からVerificationTokenエンティティを取得
	public VerificationToken findByToken(String token);
	
}
