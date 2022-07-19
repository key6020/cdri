package com.cdri.task.controller;

import com.cdri.task.dto.req.BookReqDto;
import com.cdri.task.dto.res.BookResDto;
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

    // list
    @GetMapping
    public ResponseEntity<CommonResponse<List<BookResDto>>> getBookList() {
        return new ResponseEntity<>(bookService.getBookList(), HttpStatus.OK);
    }

    // getById
//    @GetMapping("/books/{bookId}")

    // Search

    // saveBook
    @PostMapping()
    public ResponseEntity<CommonResponse> saveBook(@Valid @RequestBody BookReqDto reqDto) {
        return new ResponseEntity<>(bookService.createBook(reqDto), HttpStatus.OK);
    }
    // updateCategory
//    @PutMapping("/books/{bookId}/category")

    // updateBookStatus
//    @PutMapping("/books/{bookId}/status")
}
