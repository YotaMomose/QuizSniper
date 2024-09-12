package com.tonkatsuudon.quizsniper.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "genre_templates")
@Data
public class GenreTemplates {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String name;

    @Column(length = 20)
    private String userId;

    @Column
    private boolean isSet;

    //fetch = FetchType.EAGERとすることで一度のアクセスでtargetContentsまで取得する。デフォルトはFetchType.LAZY
    @OneToMany(mappedBy = "genreTemplates", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<GenreContents> genreContents;
}


