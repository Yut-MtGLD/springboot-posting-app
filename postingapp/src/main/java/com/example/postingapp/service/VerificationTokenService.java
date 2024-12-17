package com.example.postingapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.postingapp.entity.User;
import com.example.postingapp.entity.VerificationToken;
import com.example.postingapp.repository.VerificationTokenRepository;

@Service
public class VerificationTokenService {
	private final VerificationTokenRepository verificationTokenRepository;
	
	// 依存性の注入
	public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
		this.verificationTokenRepository = verificationTokenRepository;
	}
	
	/**
	 * ユーザーとトークンを受け取り、認証トークンをデータベースへ保存
	 * @param user トークンを関連付けるユーザー
	 * @param token 認証トークンの文字列
	 */
	@Transactional
	public void create(User user, String token) {
		VerificationToken verificationToken = new VerificationToken();
		
		verificationToken.setUser(user);
		verificationToken.setToken(token);
		
		verificationTokenRepository.save(verificationToken);
	}
	
	/**
	 * トークンの文字列で検索した結果を返す
	 * @param token 検索対象のトークン文字列
	 * @return トークンに一致するVerificationTokenオブジェクト、存在しない場合はnullを返す
	 */
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}
}
