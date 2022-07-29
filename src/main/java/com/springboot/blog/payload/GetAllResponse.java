package com.springboot.blog.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* Generic Response */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllResponse<T> {

	private List<T> content;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean last;
}
