package com.cdri.task.repository;

import com.cdri.task.domain.Book;
import com.cdri.task.domain.Category;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> likeWriter(String writer) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("writer"), "%" + writer + "%");
    }

    public static Specification<Book> likeTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Category> likeCategory(String category) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + category + "%");
    }
}
