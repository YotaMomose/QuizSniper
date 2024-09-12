package com.tonkatsuudon.quizsniper.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tonkatsuudon.quizsniper.entity.GenreContents;
import com.tonkatsuudon.quizsniper.entity.GenreTemplates;
import com.tonkatsuudon.quizsniper.entity.TargetContents;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;
import com.tonkatsuudon.quizsniper.repository.GenreRepository;
import com.tonkatsuudon.quizsniper.repository.TargetRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateService {
    @PersistenceContext
    private EntityManager entityManager;
    private TargetRepository targetRepository;
    private GenreRepository genreRepository;

    @PostConstruct
    public void init() {
        targetRepository = new TargetRepository(entityManager);
        genreRepository = new GenreRepository(entityManager);
    }

    //TODO インターフェースとかで下の二つのメソッドをいい感じにまとめられないか要検討
    /**
     * ユーザーがセットしているターゲットテンプレートの中身（contets）の一覧のリストを取得する。
     * @param ユーザーID
     * @return　セットしているターゲットテンプレートの中身のリスト
     */
    public List<String> getSetTargetContents(String userId) {
        List<TargetTemplates> targetTemplates = targetRepository.findTargetTemplates(userId);

        TargetTemplates setTargetTemplate = targetTemplates.get(0);

        List<String> setTargetContets = setTargetTemplate.getTargetContents().stream()
        .map(TargetContents::getContent)
        .collect(Collectors.toList());

        return setTargetContets;
    }

    /**
     * ユーザーがセットしているジャンルテンプレートの中身（contets）の一覧のリストを取得する。
     * @param ユーザーID
     * @return　セットしているジャンルテンプレートの中身のリスト
     */
    public List<String> getSetGenreContents(String userId) {
        List<GenreTemplates> genreTemplates = genreRepository.findGenreTemplates(userId);

        GenreTemplates setGenreTemplate = genreTemplates.get(0);

        List<String> setGenreContets = setGenreTemplate.getGenreContents().stream()
        .map(GenreContents::getContent)
        .collect(Collectors.toList());

        return setGenreContets;
    }
    
}
