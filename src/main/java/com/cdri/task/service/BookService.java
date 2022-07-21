package com.cdri.task.service;

import com.cdri.task.domain.Book;
import com.cdri.task.domain.BookCategory;
import com.cdri.task.domain.Category;
import com.cdri.task.dto.req.BookReqDto;
import com.cdri.task.dto.req.CategoryReqDto;
import com.cdri.task.dto.res.BookCategoryResDto;
import com.cdri.task.dto.res.BookResDto;
import com.cdri.task.dto.res.CommonResponse;
import com.cdri.task.repository.BookCategoryRepository;
import com.cdri.task.repository.BookRepository;
import com.cdri.task.repository.BookSpecification;
import com.cdri.task.repository.CategoryRepository;
import com.cdri.task.utils.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
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
        return CommonResponse.response(HttpStatus.OK.getStatus(), "도서 목록 조회", getBookListResDto(bookRepository.findAll()));
    }

    @Transactional(readOnly = true)
    public CommonResponse<List<BookResDto>> getBookListByWriter(String keyword) {
        Specification<Book> spec = Specification.where(BookSpecification.likeWriter(keyword));
        return CommonResponse.response(HttpStatus.OK.getStatus(), "도서 검색 결과", getBookListResDto(bookRepository.findAll(spec)));
    }

    @Transactional(readOnly = true)
    public CommonResponse<List<BookResDto>> getBookListByTitle(String keyword) {
        Specification<Book> spec = Specification.where(BookSpecification.likeTitle(keyword));
        return CommonResponse.response(HttpStatus.OK.getStatus(), "도서 검색 결과", getBookListResDto(bookRepository.findAll(spec)));
    }

    @Transactional(readOnly = true)
    public CommonResponse<List<BookResDto>> getBookListByCategory(String keyword) {
        Specification<Category> spec = Specification.where(BookSpecification.likeCategory(keyword));
        List<BookCategory> bookCategories = bookCategoryRepository.findAllByCategoryIn(categoryRepository.findAll(spec));
        return CommonResponse.response(HttpStatus.OK.getStatus(), "도서 검색 결과", getBookListResDto(bookRepository.findAllByCategoryListIn(bookCategories)));
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
        return CommonResponse.response(HttpStatus.OK.getStatus(), "도서 입력 완료");
    }

    @Transactional
    public CommonResponse<BookCategoryResDto> updateCategory(Long bookId, CategoryReqDto reqDto) {
        Book book = bookRepository.findById(bookId).orElse(null);
        List<BookCategory> newBookCategoryList = new ArrayList<>();
        for (String c : reqDto.getCategoryList()) {
            Category category = categoryRepository.findByName(c);
            if (category == null) {
                category = Category.builder().name(c).build();
                categoryRepository.save(category);
            }
            BookCategory bookCategory = BookCategory.builder().book(book).category(category).build();
            newBookCategoryList.add(bookCategory);
        }
        bookCategoryRepository.deleteAllByBookId(bookId);

        assert book != null;
        book.updateCategoryList(newBookCategoryList);

        BookCategoryResDto resDto = new BookCategoryResDto();
        resDto.setBookId(bookId);
        resDto.setCategoryList(newBookCategoryList.stream().map(n -> n.getCategory().getName()).collect(Collectors.toList()));

        return CommonResponse.response(HttpStatus.OK.getStatus(), "도서 카테고리 수정", resDto);
    }

    private List<BookResDto> getBookListResDto(List<Book> books) {
        List<BookResDto> bookList = new ArrayList<>();
        for (Book b : books) {
            BookResDto resDto = new BookResDto();
            resDto.setBookId(b.getId());
            resDto.setWriter(b.getWriter());
            resDto.setTitle(b.getTitle());
            resDto.setCategoryList(b.getCategoryList().stream().map(bc -> bc.getCategory().getName()).collect(Collectors.toList()));
            bookList.add(resDto);
        }
        return bookList;
    }
}
