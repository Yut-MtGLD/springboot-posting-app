package com.example.postingapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.postingapp.entity.Post;
import com.example.postingapp.entity.User;

public interface PostRepository extends JpaRepository<Post, Integer> {
	// 作成日時の降順で作成
	public List<Post> findByUserOrderByCreatedAtDesc(User user);
	public Post findFirstByOrderByIdDesc();
	
	// 更新日時の昇順で作成
	public List<Post> findByUserOrderByUpdatedAtAsc(User user);
	public Post findFirstByOrderByIdAsc();
}
