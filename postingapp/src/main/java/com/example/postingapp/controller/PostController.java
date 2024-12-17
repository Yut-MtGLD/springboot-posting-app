package com.example.postingapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.postingapp.entity.Post;
import com.example.postingapp.entity.User;
import com.example.postingapp.form.PostEditForm;
import com.example.postingapp.form.PostRegisterForm;
import com.example.postingapp.security.UserDetailsImpl;
import com.example.postingapp.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {
	// 依存性の注入
	private final PostService postService;
	
	public PostController(PostService postService) {
		this.postService = postService;
	}
	
	// 投稿一覧の表示
	@GetMapping 
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		// 認証済みユーザーを取得
		User user = userDetailsImpl.getUser();
		
		// ユーザーの投稿を作成日の降順で取得
		// List<Post> posts = postService.findByUserOrderByCreatedAtDesc(user);
		
		// ユーザーの投稿を更新日の昇順で取得
		List<Post> posts = postService.findByUserOrderByUpdatedAtAsc(user);
		
		// 取得した投稿をモデルに追加
		model.addAttribute("posts", posts);
		
		// 投稿一覧を表示
		return "posts/index";
	}
	
	// 投稿詳細の表示
	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
		// IDで投稿を取得
		Optional<Post> optionalPost = postService.findPostById(id);
		
		// 投稿が存在しない場合に行う
		if(optionalPost.isEmpty()) {
			// エラーメッセージの設定
			redirectAttributes.addFlashAttribute("errorMessage", "投稿が存在しません。");
			
			// 投稿一覧ページにリダイレクト
			return "redirect:/posts";
		}
		
		// 投稿を取得
		Post post = optionalPost.get();
		
		// 取得した投稿を格納
		model.addAttribute("post", post);
		
		// 投稿詳細の表示
		return "posts/show";
	}
	
	// 投稿作成画面の表示
	@GetMapping("/register")
	public String register(Model model) {
		// 空のフォームオブジェクトにモデルを追加
		model.addAttribute("postRegisterForm", new PostRegisterForm());
		
		//投稿作成画面を表示
		return "posts/register";
	}
	
	// 投稿登録処理
	@PostMapping("/create")
	public String create(@ModelAttribute @Validated PostRegisterForm postRegisterForm,
		                 BindingResult bindingResult,
		                 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
		                 RedirectAttributes redirectAttributes,
		                 Model model) 
	{
		// バリデーションエラーがある場合
		if (bindingResult.hasErrors()) {
			// フォームオブジェクトをモデルに追加
			model.addAttribute("postRegister", postRegisterForm);
			
			// 投稿画面へどもる
			return "posts/register";
		}
		
		// 認証済みユーザーを取得
		User user = userDetailsImpl.getUser();
		
		// 投稿を作成
		postService.createPost(postRegisterForm, user);
		
		// 成功メッセージの設定
		redirectAttributes.addFlashAttribute("successMessage", "投稿が完了しました。");
		
		// 投稿一覧ページにリダイレクト
		return "redirect:/posts";
	}
	
	// 編集画面の表示
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id,
		               @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
		               RedirectAttributes redirectAttributes,
		               Model model)
	{
		// IDで投稿を取得
		Optional<Post> optionalPost = postService.findPostById(id);
		
		// 認証済みユーザーを取得
		User user = userDetailsImpl.getUser();
		
		// 投稿が存在しない、またはユーザーが異なる場合
		if (optionalPost.isEmpty() || !optionalPost.get().getUser().equals(user)) {
			// エラーメッセージの設定
			redirectAttributes.addFlashAttribute("errorMessage", "不正なアクセスです。");
			
			// 投稿一覧ページにリダイレクト
			return "redirect:/posts";
		}
		
		// 投稿を取得
		Post post = optionalPost.get();
		
		// 取得した投稿をモデルに追加
		model.addAttribute("post", post);
		
		// 編集用フォームオブジェクトを作成してモデルに追加
		model.addAttribute("postEditForm", new PostEditForm(post.getTitle(), post.getContent()));
		
		// 編集画面を表示
		return "posts/edit";
	}
	
	// 更新画面の表示
	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated PostEditForm postEditForm,
		                 BindingResult bindingResult,
		                 @PathVariable(name = "id") Integer id,
		                 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
		                 RedirectAttributes redirectAttributes,
		                 Model model)
	{
		// IDで投稿を取得
		Optional<Post> optionalPost = postService.findPostById(id);
		// 認証済みユーザーを取得
		User user = userDetailsImpl.getUser();
		
		// 投稿が存在しない、またはユーザーが異なる場合
		if (optionalPost.isEmpty() || !optionalPost.get().getUser().equals(user)) {
			// エラーメッセージを設定
			redirectAttributes.addFlashAttribute("errorMessage", "不正なアクセスです。");
			
			// 投稿一覧ページにリダイレクト
			return "redirect:/posts";
		}
		
		// 投稿を取得
		Post post = optionalPost.get();
		
		// バリデーションエラーがある場合
		if (bindingResult.hasErrors()) {
			// 投稿とフォームオブジェクトをモデルに追加
			model.addAttribute("post", post);
			model.addAttribute("postEditForm", postEditForm);
			
			// 編集画面に戻る
			return "posts/edit";
		}
		
		// 投稿を更新
		postService.updatePost(postEditForm, post);
		// 成功メッセージを設定
		redirectAttributes.addFlashAttribute("successMessage", "投稿を編集しました。");
		
		// 元の投稿詳細ページにリダイレクト
		return "redirect:/posts/" + id;
	}
	
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable(name = "id") Integer id,
		                 @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
		                 RedirectAttributes redirectAttributes,
		                 Model model)
	{
		Optional<Post> optionalPost = postService.findPostById(id);
		User user = userDetailsImpl.getUser();
		
		if (optionalPost.isEmpty() || !optionalPost.get().getUser().equals(user)) {
			redirectAttributes.addFlashAttribute("errorMessage", "不正なアクセスです。");
			
			return "redirect:/posts";
		}
		
		Post post = optionalPost.get();
		postService.deletePost(post);
		redirectAttributes.addFlashAttribute("successMessage", "投稿を削除しました。");
		
		return "redirect:/posts";
	}
}
