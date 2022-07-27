package com.springboot.blog.service;

import java.util.List;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

public interface PostService {

	public PostDto createPost(PostDto postDto);

	public List<PostDto> getAllPosts();

	public PostDto getById(Long id);

	public PostDto updatePost(Long id, PostDto postDto);

	public String delete(Long id);

	public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
}
