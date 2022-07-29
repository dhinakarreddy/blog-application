package com.springboot.blog.service;

import java.util.List;

import com.springboot.blog.payload.CommentDto;

public interface CommentService {

	public CommentDto createComment(Long postId, CommentDto comment);

	public List<CommentDto> getCommentsByPostId(Long postId);

	public CommentDto getCommentById(Long postId, Long commentId);

	public CommentDto updateCommentById(Long postId, Long commentId, CommentDto comment);

	public String deleteCommentById(Long postId, Long commentId);
}
