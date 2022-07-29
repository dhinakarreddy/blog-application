package com.springboot.blog.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	/*
	 * @Value("${constants.welcome}") private String welcomeText;
	 */

	private PostService postService;

	public PostController(PostService postService) {
		this.postService = postService;
	}

	// @Valid validates the request json with respect to the annotations on dto class
	@PostMapping
	public ResponseEntity<PostDto> create(@Valid @RequestBody PostDto postDto) {
		return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
	}

	// without pagination & sorting
	/*
	 * @GetMapping public ResponseEntity<List<PostDto>> getAll() { return new
	 * ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK); }
	 */

	// Get with pagination support and sort support
	@GetMapping
	public ResponseEntity<PostResponse> getAll(
			@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NO, required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
		return new ResponseEntity<>(postService.getAllPosts(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDto> getById(@PathVariable Long id) {
		return new ResponseEntity<>(postService.getById(id), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PostDto> update(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") Long id) {
		return new ResponseEntity<>(postService.updatePost(id, postDto), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable Long id) {
		return new ResponseEntity<>(postService.delete(id), HttpStatus.OK);
	}

}
