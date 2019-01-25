package fr.univtln.bruno.i311.simplers.generic.dao;

import java.util.List;

/**
 * Created by bruno on 25/03/15.
 */
public class Page<T> {
    public final int PAGE_NUMBER;
    public final int PAGE_SIZE;
    public final int TOTAL_ITEMS;
    public final int TOTAL_PAGES;
    public final List<T> content;

    public Page(int pageNumber, int pageSize, int totalItems, int totalPages, List<T> content) {
        this.PAGE_NUMBER = pageNumber;
        this.PAGE_SIZE = pageSize;
        this.TOTAL_ITEMS = totalItems;
        this.TOTAL_PAGES = totalPages;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Page{" +
                "PAGE_NUMBER=" + PAGE_NUMBER +
                ", PAGE_SIZE=" + PAGE_SIZE +
                ", TOTAL_ITEMS=" + TOTAL_ITEMS +
                ", content=" + content +
                '}';
    }
}
