package com.cdri.task.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "book_category")
@Entity
public class BookCategory extends TimeEntity {
    @Column(name = "book_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder
    public BookCategory(Book book, Category category) {
        this.book = book;
        this.category = category;
        book.getCategoryList().add(this);
        category.getCategoryList().add(this);
    }
}
