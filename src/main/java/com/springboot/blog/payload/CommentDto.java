package com.springboot.blog.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

	private Long id;

	@NotEmpty
	@Size(min = 5, message = "Name should be atleast 5 char long")
	private String name;

	@NotEmpty
	@Size(min = 5, message = "Email should be atleast 5 char long")
	@Email
	private String email;

	@NotEmpty
	@Size(min = 5, message = "Body should be atleast 5 char long")
	private String body;

	private Long postId;

}
