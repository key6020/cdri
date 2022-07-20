package com.cdri.task;

import com.cdri.task.domain.Book;
import com.cdri.task.domain.BookCategory;
import com.cdri.task.domain.Category;
import com.cdri.task.repository.BookCategoryRepository;
import com.cdri.task.repository.BookRepository;
import com.cdri.task.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootTest
class TaskApplicationTests {
    Logger log = LoggerFactory.getLogger(TaskApplicationTests.class);
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BookCategoryRepository bookCategoryRepository;

    @Rollback(value = false)
    @Transactional
    @Test
    void insertBookData() throws FileNotFoundException {
        FileInputStream input = new FileInputStream("doc/book_data.txt");
        InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(reader);
        while (scanner.hasNext()) {
            String str = scanner.nextLine().trim();
            String[] temp1 = str.replace("[", "").split("]");
            Category category = categoryRepository.findByName(temp1[0]);
            if (category == null) {
                category = Category.builder().name(temp1[0]).build();
                categoryRepository.save(category);
            }
            String[] temp2 = temp1[1].split(",");
            Book book = Book.builder()
                    .writer(temp2[1])
                    .title(temp2[0])
                    .build();
            bookRepository.save(book);
            bookCategoryRepository.save(BookCategory.builder().book(book).category(category).build());
        }
    }

    @Transactional(readOnly = true)
    @Test
    void getBookCategoryList() {
        List<Book> books = bookRepository.findAll();
        for (Book b : books) {
            log.info("title : {}, writer : {}, category : {}", b.getTitle(), b.getWriter(), b.getCategoryList().stream().map(c -> c.getCategory().getName()).collect(Collectors.toList()));
        }
    }
}
