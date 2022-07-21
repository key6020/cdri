package com.cdri.task.repository;

import com.cdri.task.domain.Book;
import com.cdri.task.domain.BookCategory;
import com.cdri.task.utils.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findAllByBookStatusAndCategoryListIn(BookStatus bookStatus, List<BookCategory> categoryList);

    Book findByIdAndBookStatus(Long id, BookStatus bookStatus);
}
