package com.springboot.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	private PostRepository postRepo;
	private CommentRepository commentRepo;

	public CommentServiceImpl(PostRepository postRepo, CommentRepository commentRepo) {
		this.postRepo = postRepo;
		this.commentRepo = commentRepo;
	}

	@Override
	public CommentDto createComment(Long postId, CommentDto commentDto) {
		Comment comment = mapToComment(commentDto);
		// Post post = postRepo.findById(postId).orElseThrow(() -> new
		// ResourceNotFoundException("posts", "id", postId));
		Post post = this.getPostInfo(postId);
		comment.setPost(post);

		Comment inserted = commentRepo.save(comment);
		CommentDto response = mapToCommentDto(inserted);

		return response;
	}

	public Comment mapToComment(CommentDto dto) {
		Comment comment = new Comment(dto.getId(), dto.getName(), dto.getEmail(), dto.getBody(), null);
		return comment;
	}

	public CommentDto mapToCommentDto(Comment comment) {
		CommentDto dto = new CommentDto(comment.getId(), comment.getName(), comment.getEmail(), comment.getBody(),
				comment.getPost().getId());
		return dto;
	}

	@Override
	public List<CommentDto> getCommentsByPostId(Long postId) {
		List<Comment> comments = commentRepo.findByPostId(postId);
		return comments.stream().map(comment -> mapToCommentDto(comment)).collect(Collectors.toList());
	}

	@Override
	public CommentDto getCommentById(Long postId, Long commentId) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
		/*
		 * Comment comment = commentRepo.findById(commentId) .orElseThrow(() -> new
		 * ResourceNotFoundException("Comment", "id", commentId));
		 */
		Comment comment = this.getCommentInfo(commentId);

		if (!comment.getPost().getId().equals(post.getId()))
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");

		return mapToCommentDto(comment);
	}

	@Override
	public CommentDto updateCommentById(Long postId, Long commentId, CommentDto update) {
		Post post = this.getPostInfo(postId);
		Comment comment = this.getCommentInfo(commentId);
		if (!comment.getPost().getId().equals(post.getId()))
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");

		if (update.getEmail() != null)
			comment.setEmail(update.getEmail());
		if (update.getName() != null)
			comment.setName(update.getName());
		if (update.getBody() != null)
			comment.setBody(update.getBody());

		Comment response = commentRepo.save(comment);
		return mapToCommentDto(response);
	}

	private Post getPostInfo(Long postId) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("posts", "id", postId));
		return post;
	}

	private Comment getCommentInfo(Long commentId) {
		Comment comment = commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
		return comment;
	}

	@Override
	public String deleteCommentById(Long postId, Long commentId) {
		Post post = this.getPostInfo(postId);
		Comment comment = this.getCommentInfo(commentId);
		if (!comment.getPost().getId().equals(post.getId()))
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");

		commentRepo.deleteById(commentId);
		return "Comment deleted successfully";
	}

}
