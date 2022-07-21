package com.cdri.task;

import com.cdri.task.domain.Book;
import com.cdri.task.domain.BookCategory;
import com.cdri.task.domain.Category;
import com.cdri.task.dto.req.BookStatusReqDto;
import com.cdri.task.dto.req.CategoryReqDto;
import com.cdri.task.repository.BookCategoryRepository;
import com.cdri.task.repository.BookRepository;
import com.cdri.task.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

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
    @Autowired
    private ObjectMapper mapper;

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
            Category category = categoryRepository.findByName(temp1[0].trim());
            if (category == null) {
                category = Category.builder().name(temp1[0].trim()).build();
                categoryRepository.save(category);
            }
            String[] temp2 = temp1[1].split(",");
            Book book = Book.builder()
                    .writer(temp2[1].trim())
                    .title(temp2[0].trim())
                    .build();
            bookRepository.save(book);
            bookCategoryRepository.save(BookCategory.builder().book(book).category(category).build());
        }
    }

    @Transactional(readOnly = true)
    @Test
    void getBookList() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andDo(print());
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(Matchers.containsInAnyOrder("단순하게 배부르게", "게으른 사랑")))
                .andDo(print());
    }


    @Transactional(readOnly = true)
    @Test
    void searchBooksByWriterOrTitle() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/books/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("keyword", "검색")
                ).andExpect(status().isOk())
                .andDo(print());
    }

    @Rollback(value = false)
    @Transactional
    @Test
    void updateCategory() throws Exception {
        CategoryReqDto reqDto = new CategoryReqDto(Arrays.asList("새로운카테고리1", "새로운카테고리2"));

        this.mockMvc.perform(MockMvcRequestBuilders.put("/v1/books/{bookId}/category", 16)
                        .content(mapper.writeValueAsBytes(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categoryList").value(Matchers.containsInAnyOrder("새로운카테고리1", "새로운카테고리2")))
                .andDo(print());
    }

    @Rollback(value = false)
    @Transactional
    @Test
    void updateBookStatus() throws Exception {
        BookStatusReqDto reqDto = new BookStatusReqDto();
        reqDto.setStatus("분실");
        this.mockMvc.perform(MockMvcRequestBuilders.patch("/v1/books/{bookId}/status", 16)
                        .content(mapper.writeValueAsBytes(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value("분실"))
                .andDo(print());
    }

    @Transactional(readOnly = true)
    @Test
    void getBookById() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/books/{bookId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("너에게 해주지 못한 말들"))
                .andDo(print());
    }
}
