package com.cdri.task.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Table(name = "category")
@Entity
public class Category {
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Column(nullable = false)
    private List<BookCategory> categoryList = new ArrayList<>();

    @Builder
    public Category(String name) {
        this.name = name;
    }
}
