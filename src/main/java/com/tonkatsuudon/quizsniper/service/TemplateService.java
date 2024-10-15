package com.tonkatsuudon.quizsniper.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tonkatsuudon.quizsniper.dao.QuizElementDao;
import com.tonkatsuudon.quizsniper.entity.GenreContents;
import com.tonkatsuudon.quizsniper.entity.GenreTemplates;
import com.tonkatsuudon.quizsniper.entity.TargetContents;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;
import com.tonkatsuudon.quizsniper.entity.Templates;
import com.tonkatsuudon.quizsniper.repository.GenreRepository;
import com.tonkatsuudon.quizsniper.repository.TargetRepository;
import com.tonkatsuudon.quizsniper.type.ElementType;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateService {

    @PersistenceContext
    private EntityManager entityManager;
    private TargetRepository targetRepository;
    private GenreRepository genreRepository;
    private Map<ElementType, QuizElementDao> repositoies;

    @PostConstruct
    public void init() {
        targetRepository = new TargetRepository(entityManager);
        genreRepository = new GenreRepository(entityManager);
        repositoies = new HashMap<>();
        repositoies.put(ElementType.Genre, genreRepository);
        //repositoies.put(ElementType.Target, targetRepository);
    }
    //TODO インターフェースとかで下の二つのメソッドをいい感じにまとめられないか要検討
    /**
     * ユーザーがセットしているターゲットテンプレートの中身（contets）の一覧のリストを取得する。
     * @param ユーザーID
     * @return　セットしているターゲットテンプレートの中身のリスト
     */
    public List<String> getSetTargetContents(List<TargetTemplates> targetTemplates) {
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
    public List<String> getSetGenreContents(List<GenreTemplates> genreTemplates) {
        GenreTemplates setGenreTemplate = genreTemplates.get(0);

        List<String> setGenreContets = setGenreTemplate.getGenreContents().stream()
        .map(GenreContents::getContent)
        .collect(Collectors.toList());

        return setGenreContets;
    }
    
    /**
     * ユーザーがセットしているターゲットテンプレートのidを取得する処理
     */
    public Integer getSetTargetId(List<TargetTemplates> targetTemplates) {
        TargetTemplates setTargetTemplate = targetTemplates.get(0);
        System.out.println(setTargetTemplate.getId());
        return setTargetTemplate.getId();
    }

    /**
     * ユーザーがセットしているジャンルテンプレートのidを取得する処理
     */
    public Integer getSetGenreId(List<GenreTemplates> genreTemplates) {
        GenreTemplates setGenreTemplate = genreTemplates.get(0);
        System.out.println(setGenreTemplate.getId());
        return setGenreTemplate.getId();
    }

    /**
     * 現在セットされているジャンルテンプレートを解除し、新たにテンプレートをセットする。
     * @param currentSetId 現在セットされているテンプレートのID
     * @param newSetId　新たにセットするテンプレートのID
     */
    @Transactional
    public void switchSetGenreTemplate(Integer currentSetId, Integer newSetId) {
        genreRepository.unsetGenreTemplate(currentSetId);
        genreRepository.setGenreTemplate(newSetId);
    }

    /**
     * 現在セットされているターゲットテンプレートを解除し、新たにテンプレートをセットする。
     * @param currentSetId 現在セットされているテンプレートのID
     * @param newSetId　新たにセットするテンプレートのID
     */
    @Transactional
    public void switchSetTargetTemplate(Integer currentSetId, Integer newSetId) {
        targetRepository.unsetTargetTemplate(currentSetId);
        targetRepository.setTargetTemplate(newSetId);
    }

    /**
     * 現在セットされているジャンルテンプレートに新たにコンテンツを追加する
     * @param genreTemplates テンプレートのリスト
     * @param newGenre 追加するコンテンツ
     */
    @Transactional
    public void addNewGenreContent(List<GenreTemplates> genreTemplates, String newGenre) {
        GenreTemplates setTemplate = genreTemplates.get(0);

        genreRepository.addGenreContent(newGenre, setTemplate);
    }

    /**
     * 現在セットされているテンプレートにのコンテンツを削除する
     * @param genreTemplates テンプレートのリスト
     * @param deleteGenre 削除するコンテンツ
     */
    @Transactional
    public void deleteContent(List<Templates> templates, String deleteContent, ElementType type) {
        Templates setTemplate = templates.get(0);
        // QuizElementDao repository = repositoies.get(type);
        genreRepository.delteSetContent(deleteContent, setTemplate);
        
    }

    /**
     * 現在セットされているテンプレートにのコンテンツを削除する
     * @param genreTemplates テンプレートのリスト
     * @param deleteGenre 削除するコンテンツ
     */
    @Transactional
    public void deletetest(String id) {
        
        genreRepository.delteTest(id);;
        
    }

    /**
     * 現在セットされているターゲットテンプレートに新たにコンテンツを追加する
     * @param targetTemplates　
     * @param newGenre 追加するコンテンツ
     */
    @Transactional
    public void addNewTargetContent(List<TargetTemplates> targetTemplates, String newTarget) {
        TargetTemplates setTemplate = targetTemplates.get(0);

        targetRepository.addTargetContent(newTarget, setTemplate);
    }
}
