package com.cdri.task.domain;

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
    private List<Category> categoryList = new ArrayList<>();

    @Builder
    public Book(String writer, String title, List<Category> categoryList) {
        this.writer = writer;
        this.title = title;
        for (Category category : categoryList) {
            this.addCategory(category);
        }
    }

    public void addCategory(Category category) {
        categoryList.add(category);
        category.setBook(this);
    }
}
