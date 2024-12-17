package com.example.postingapp.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.example.postingapp.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignupEventPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;
	
	// 会員登録イベントの発行
	public void publishSignupEvent(User user, String requestUrl) {
		applicationEventPublisher.publishEvent(new SignupEvent(this, user, requestUrl));
	}
}