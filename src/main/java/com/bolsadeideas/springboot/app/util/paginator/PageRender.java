package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

public class PageRender<T> {

	@Getter
	private String url;

	@Getter
	private int totalPages;

	@Getter
	private int actualPage;

	@Getter
	private List<PageItem> pages;

	private Page<T> page;

	private int size;

	public static <T> PageRender<T> of(String url, Page<T> page) {
		PageRender<T> pageRender = new PageRender<>();

		pageRender.initPageRender(url, page);

		return pageRender;
	}

	public boolean isFirst() {
		return this.page.isFirst();
	}

	public boolean isLast() {
		return this.page.isLast();
	}

	public boolean hasNext() {
		return this.page.hasNext();
	}

	public boolean hasPrevious() {
		return this.page.hasPrevious();
	}

	private void initPageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<>();
		
		this.size = page.getSize();
		this.totalPages = page.getTotalPages();
		this.actualPage = page.getNumber() + 1;

		int start, end;
		if (this.totalPages <= this.size) {
			start = 1;
			end = this.totalPages;
		} else {
			if (this.actualPage <= this.size / 2) {
				start = 1;
			} else if (this.actualPage >= this.totalPages - this.size / 2) {
				start = this.totalPages - this.size + 1;
			} else {
				start = this.actualPage - this.size / 2;
			}
			end = this.size;
		}

		for (int i = 0; i < end; i++) {
			this.pages.add(new PageItem(start + i, this.actualPage == start + i));
		}
	}

}
