package com.example.postingapp.event;

import java.util.UUID;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.postingapp.entity.User;
import com.example.postingapp.service.VerificationTokenService;

@Component
public class SignupEventListener {
	
	// 依存性の注入
	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;
	
	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService = verificationTokenService;
		this.javaMailSender = mailSender;
	}
	
	@EventListener
	private void onSignupEvent(SignupEvent signupEvent) {
		
		// SignupEventクラスから通知を受け取ったときに実行される処理
		
		// 新規会員の取得
		User user = signupEvent.getUser();
		// 認証トークンの生成
		String token = UUID.randomUUID().toString();
		// 認証トークンをDBへ保存
		verificationTokenService.create(user, token);
		
		// 送信元メールアドレス
		String senderAddress = "springboot.postingapp@example.com";
		// 宛先
		String recipientAddress = user.getEmail();
		// メール件名
		String subject = "メール認証";
		// 認証用URL
		String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
		// メール本文
		String message = "以下のリンクをクリックして会員登録を完了してください。";
		
		// メール送信処理
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(senderAddress);
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		
		// メール送信
		javaMailSender.send(mailMessage);
	}
}