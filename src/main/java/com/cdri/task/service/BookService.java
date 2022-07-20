package com.cdri.task.service;

import com.cdri.task.domain.Book;
import com.cdri.task.domain.BookCategory;
import com.cdri.task.domain.Category;
import com.cdri.task.dto.req.BookReqDto;
import com.cdri.task.dto.res.BookResDto;
import com.cdri.task.dto.res.CommonResponse;
import com.cdri.task.repository.BookCategoryRepository;
import com.cdri.task.repository.BookRepository;
import com.cdri.task.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Transactional(readOnly = true)
    public CommonResponse<List<BookResDto>> getBookList() {
        List<Book> books = bookRepository.findAll();
        List<BookResDto> bookList = new ArrayList<>();
        for (Book b : books) {
            BookResDto resDto = new BookResDto();
            resDto.setBookId(b.getId());
            resDto.setWriter(b.getWriter());
            resDto.setTitle(b.getTitle());
            resDto.setCategoryList(b.getCategoryList().stream().map(bc -> bc.getCategory().getName()).collect(Collectors.toList()));
            bookList.add(resDto);
        }

        return CommonResponse.response(HttpStatus.OK.getStatus(), "Book List Found.", bookList);
    }

    @Transactional
    public <T> CommonResponse<T> createBook(BookReqDto reqDto) {
        List<Category> categories = new ArrayList<>();
        for (String c : reqDto.getCategoryList()) {
            Category category = categoryRepository.findByName(c);
            if (category == null) {
                Category newCategory = Category.builder()
                        .name(c)
                        .build();
                categoryRepository.save(newCategory);
                categories.add(newCategory);
            } else {
                categories.add(category);
            }
        }

        Book book = Book.builder()
                .title(reqDto.getTitle())
                .writer(reqDto.getWriter())
                .build();
        bookRepository.save(book);

        for (Category c : categories) {
            bookCategoryRepository.save(BookCategory.builder().book(book).category(c).build());
        }
        return CommonResponse.response(HttpStatus.OK.getStatus(), "Book Created.");
    }
}
