package com.cdri.task.repository;

import com.cdri.task.domain.Book;
import com.cdri.task.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findAllByCategoryListIn(List<BookCategory> categoryList);
}
