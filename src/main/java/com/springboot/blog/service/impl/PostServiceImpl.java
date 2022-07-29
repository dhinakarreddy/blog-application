package com.springboot.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	private PostRepository repo;
	private ModelMapper mapper;

	// @Autowired
	public PostServiceImpl(PostRepository repo, ModelMapper mapper) {
		this.repo = repo;
		this.mapper = mapper;
	}

	private PostDto mapToDto(Post post) {
		/* PostDto postDto = new PostDto();
		postDto.setId(post.getId());
		postDto.setTitle(post.getTitle());
		postDto.setDescription(post.getDescription());
		postDto.setContent(post.getContent());

		return postDto; */
		
		return mapper.map(post, PostDto.class);
	}

	private Post mapToEntity(PostDto dto) {
		/* Post post = new Post();
		if (post.getId() != null)
			post.setId(dto.getId());
		post.setTitle(dto.getTitle());
		post.setDescription(dto.getDescription());
		post.setContent(dto.getContent());

		return post; */
		return mapper.map(dto, Post.class);
	}

	@Override
	public PostDto createPost(PostDto payload) {
		Post post = mapToEntity(payload);
		Post newPost = repo.save(post);
		PostDto response = mapToDto(newPost);

		return response;
	}

	@Override
	public List<PostDto> getAllPosts() {
		List<Post> posts = repo.findAll();
		return posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
	}

	@Override
	public PostDto getById(Long id) {
		Post post = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		return mapToDto(post);
	}

	@Override
	public PostDto updatePost(Long id, PostDto postDto) {
		Post post = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

		post.setTitle(postDto.getTitle());
		post.setDescription(postDto.getDescription());
		post.setContent(postDto.getContent());

		Post updatedPost = repo.save(post);
		PostDto response = mapToDto(updatedPost);

		return response;

	}

	@Override
	public String delete(Long id) {
		repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
		repo.deleteById(id);

		return "Post deleted successfully";
	}

	@Override
	public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Direction.ASC.name()) ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		Pageable page = PageRequest.of(pageNo, pageSize, sort);
		Page<Post> posts = repo.findAll(page);
		List<Post> filtered = posts.getContent();

		List<PostDto> content = filtered.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

		PostResponse response = new PostResponse(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(),
				posts.getTotalPages(), posts.isLast());

		return response;
	}
}
