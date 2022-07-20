package com.cdri.task;

import com.cdri.task.domain.Book;
import com.cdri.task.domain.BookCategory;
import com.cdri.task.domain.Category;
import com.cdri.task.repository.BookCategoryRepository;
import com.cdri.task.repository.BookRepository;
import com.cdri.task.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class TaskApplicationTests {
    Logger log = LoggerFactory.getLogger(TaskApplicationTests.class);
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BookCategoryRepository bookCategoryRepository;
    @Autowired
    private MockMvc mockMvc;

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

    @Transactional(readOnly = true)
    @Test
    void searchBooksByWriter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("writer", "권태영")
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Transactional(readOnly = true)
    @Test
    void searchBooksByTitle() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "리더")
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Transactional(readOnly = true)
    @Test
    void searchBooksByCategory() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("category", "문학")
                ).andExpect(status().isOk())
                .andDo(print());
    }
}
