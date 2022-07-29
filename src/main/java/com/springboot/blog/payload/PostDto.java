package com.springboot.blog.payload;

import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PostDto {

	private Long id;

	// title should not be null or empty
	// title should contain atleast min 2 chars
	@NotEmpty
	@Size(min = 2, message = "Post title should be atleast 2 char length")
	private String title;

	@NotEmpty
	@Size(min = 5, message = "Post description should be atleast 5 char length")
	private String description;

	@NotEmpty
	@Size(min = 5, message = "Post content should be atleast 5 char length")
	private String content;

	private Set<CommentDto> comments;
}
