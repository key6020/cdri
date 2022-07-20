package com.cdri.task.repository;

import com.cdri.task.domain.BookCategory;
import com.cdri.task.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    List<BookCategory> findAllByCategoryIn(List<Category> categoryList);
}
