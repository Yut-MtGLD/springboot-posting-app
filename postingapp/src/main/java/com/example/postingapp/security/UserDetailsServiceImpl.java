package com.example.postingapp.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.postingapp.entity.User;
import com.example.postingapp.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	// 依存性の注入
	private final UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {
			// メールアドレスからユーザー情報を取得
			User user = userRepository.findByEmail(email);
			// ユーザーのロール名を取得
			String userRoleName = user.getRole().getName();
			// リストの作成
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			// 取得したユーザーのロールをリストに追加
			authorities.add(new SimpleGrantedAuthority(userRoleName));
			
			// UserDetailsImplオブジェクトを生成して返す
			return new UserDetailsImpl(user, authorities);
		} catch(Exception e) {
			// ユーザーが見つからない場合は例外をスロー
			throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
		}
	}
}
