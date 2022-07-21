package com.cdri.task.domain;

import com.cdri.task.utils.BookStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "book")
@Entity
public class Book extends TimeEntity {
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private String writer;
    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<BookCategory> categoryList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    @Builder
    public Book(String writer, String title) {
        this.writer = writer;
        this.title = title;
        this.bookStatus = BookStatus.정상;
    }

    public void updateCategoryList(List<BookCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
