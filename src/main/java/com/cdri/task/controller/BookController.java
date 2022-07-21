package com.cdri.task.controller;

import com.cdri.task.dto.req.BookReqDto;
import com.cdri.task.dto.req.BookStatusReqDto;
import com.cdri.task.dto.req.CategoryReqDto;
import com.cdri.task.dto.res.BookCategoryResDto;
import com.cdri.task.dto.res.BookResDto;
import com.cdri.task.dto.res.BookStatusResDto;
import com.cdri.task.dto.res.CommonResponse;
import com.cdri.task.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/v1/books")
@RestController
public class BookController {
    private final BookService bookService;

    // getById
//    @GetMapping("/books/{bookId}")

    // 검색 조회 + 전체 도서 목록 조회
    @GetMapping()
    public ResponseEntity<CommonResponse<List<BookResDto>>> getBookListBySearch(@RequestParam(required = false) String writer,
                                                                                @RequestParam(required = false) String title,
                                                                                @RequestParam(required = false) String category) {

        if (writer != null) {
            return new ResponseEntity<>(bookService.getBookListByWriter(writer), HttpStatus.OK);
        }
        if (title != null) {
            return new ResponseEntity<>(bookService.getBookListByTitle(title), HttpStatus.OK);
        }
        if (category != null) {
            return new ResponseEntity<>(bookService.getBookListByCategory(category), HttpStatus.OK);
        }

        return new ResponseEntity<>(bookService.getBookList(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CommonResponse> saveBook(@Valid @RequestBody BookReqDto reqDto) {
        return new ResponseEntity<>(bookService.createBook(reqDto), HttpStatus.OK);
    }

    @PutMapping("/{bookId}/category")
    public ResponseEntity<CommonResponse<BookCategoryResDto>> updateBookCategory(@PathVariable("bookId") Long bookId, @Valid @RequestBody CategoryReqDto reqDto) {
        return new ResponseEntity<>(bookService.updateBookCategory(bookId, reqDto), HttpStatus.OK);
    }

    @PatchMapping("/{bookId}/status")
    public ResponseEntity<CommonResponse<BookStatusResDto>> updateBookStatus(@PathVariable("bookId") Long bookId, @Valid @RequestBody BookStatusReqDto reqDto) {
        return new ResponseEntity<>(bookService.updateBookStatus(bookId, reqDto), HttpStatus.OK);
    }
}
