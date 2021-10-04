package com.bolsadeideas.springboot.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

public class PageRender<T> {

	@Getter
	private String url;

	@Getter
	private int pagesQuantity;

	@Getter
	private int actualPage;

	@Getter
	private List<PageItem> pages;

	private Page<T> page;

	private int elementsForPage;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<>();

		this.elementsForPage = page.getSize();
		this.pagesQuantity = page.getTotalPages();
		this.actualPage = page.getNumber() + 1;

		int start, end;
		if (this.pagesQuantity <= this.elementsForPage) {
			start = 1;
			end = this.pagesQuantity;
		} else {
			if (this.actualPage <= this.elementsForPage / 2) {
				start = 1;
			} else if (this.actualPage >= this.pagesQuantity - this.elementsForPage / 2) {
				start = this.pagesQuantity - this.elementsForPage + 1;
			} else {
				start = this.actualPage - this.elementsForPage / 2;
			}
			end = this.elementsForPage;
		}
		
		for (int i = 0; i < end; i++) {
			this.pages.add(new PageItem(start + i, this.actualPage == start + i));
		}
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

}
