package com.cdri.task.service;

import com.cdri.task.domain.Book;
import com.cdri.task.domain.Category;
import com.cdri.task.dto.req.BookReqDto;
import com.cdri.task.dto.res.BookResDto;
import com.cdri.task.dto.res.CommonResponse;
import com.cdri.task.repository.BookRepository;
import com.cdri.task.utils.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public CommonResponse<List<BookResDto>> getBookList() {
        List<Book> books = bookRepository.findAll();
        List<BookResDto> bookList = new ArrayList<>();
        for (Book b : books) {
            BookResDto resDto = new BookResDto();
            resDto.setBookId(b.getId());
            resDto.setWriter(b.getWriter());
            resDto.setTitle(b.getTitle());
            resDto.setCategoryList(b.getCategoryList().stream().map(Category::getName).collect(Collectors.toList()));
            bookList.add(resDto);
        }

        return CommonResponse.response(HttpStatus.OK.getStatus(), "Book List Found.", bookList);
    }

    @Transactional
    public <T> CommonResponse<T> createBook(BookReqDto reqDto) {
        List<Category> categories = new ArrayList<>();
        for (String c : reqDto.getCategoryList()) {
            Category category = Category.builder()
                    .name(c)
                    .build();
            categories.add(category);
        }

        Book book = Book.builder()
                .title(reqDto.getTitle())
                .writer(reqDto.getWriter())
                .categoryList(categories)
                .build();
        bookRepository.save(book);

        return CommonResponse.response(HttpStatus.OK.getStatus(), "Book Created.");
    }


}
