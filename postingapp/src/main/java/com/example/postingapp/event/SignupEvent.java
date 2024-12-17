package com.example.postingapp.event;

import org.springframework.context.ApplicationEvent;

import com.example.postingapp.entity.User;

import lombok.Getter;

@Getter
public class SignupEvent extends ApplicationEvent {
	private User user;
	private String requestUrl;
	
	/**
	 * 会員登録のイベント生成
	 * @param source イベントソース
	 * @param user 登録情報
	 * @param requestUrl 会員登録リクエストのURL
	 */
	public SignupEvent(Object source, User user, String requestUrl) {
		super(source);
		
		this.user = user; // 登録情報の設定
		this.requestUrl = requestUrl; // 会員登録リクエストのURL設定
	}
}
