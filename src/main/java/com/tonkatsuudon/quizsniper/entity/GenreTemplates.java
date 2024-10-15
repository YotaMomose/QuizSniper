package com.tonkatsuudon.quizsniper.entity;

import java.util.ArrayList;
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
public class GenreTemplates implements Templates {
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
    @OneToMany(mappedBy = "genreTemplates", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GenreContents> genreContents;


    /**
     * ログインしない場合のターゲットのデフォルトを返す
     * @param defaultGenre
     * @return
     */
    public List<GenreTemplates> getDefaultTmplate(List<String> defaultGenre) {
        
        List<GenreContents> genreContents = new ArrayList<>();
        for(String genre: defaultGenre) {
            GenreContents defaultContent = new GenreContents();
            defaultContent.setContent(genre);
            genreContents.add(defaultContent);
        }
        
        GenreTemplates genreTemplates = new GenreTemplates();
        genreTemplates.setGenreContents(genreContents);
        genreTemplates.setName("テンプレート1");

        List<GenreTemplates> defaultTmp = new ArrayList<>();
        defaultTmp.add(genreTemplates);

        return defaultTmp;

    }
}


