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

    private List<String> defaultGenre = List.of(
        "漢字問題",
        "一般常識問題",
        "スポーツのこと",
        "自分に関すること",
        "５教科の問題",
        "ものの名前",
        "動物に関すること",
        "乗り物に関すること",
        "食べ物に関すること",
        "オールジャンル",
        "有名人・芸能人に関すること",
        "音楽に関すること",
        "生き物に関すること",
        "アニメ・漫画・ゲームに関すること",
        "エンタメに関すること",
        "地理・場所に関すること",
        "ファッションに関すること",
        "中学校時代に関すること",
        "高校時代に関すること",
        "小学校時代に関すること"
    );

    /**
     * ログインしない場合のターゲットのデフォルトを返す
     * @param defaultGenre
     * @return
     */
    public List<GenreTemplates> getDefaultTmplate() {
        
        List<GenreContents> genreContents = new ArrayList<>();
        Integer contentId = 1;
        for(String genre: defaultGenre) {
            GenreContents defaultContent = new GenreContents();
            defaultContent.setContent(genre);
            defaultContent.setId(contentId);
            contentId++;
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


