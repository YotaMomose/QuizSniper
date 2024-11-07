package com.tonkatsuudon.quizsniper.service;

import java.util.ArrayList;
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
        repositoies.put(ElementType.Target, targetRepository);
    }
    /**
     * ユーザーがセットしているターゲットテンプレートの中身（Contents）の内容をリストとして取得する。
     * @param ユーザーID
     * @return　セットしているターゲットテンプレートの中身のリスト
     */
    public List<TargetContents> getsetTargetContents(List<TargetTemplates> targetTemplates) {
        TargetTemplates setTargetTemplate = targetTemplates.get(0);

        List<TargetContents> setTargetStringList = setTargetTemplate.getTargetContents();

        return setTargetStringList;
    }

    /**
     * ユーザーがセットしているジャンルテンプレートの中身（Contents）の内容をリストとして取得する。
     * @param ユーザーID
     * @return　セットしているジャンルテンプレートの中身のリスト
     */
    public List<GenreContents> getsetGenreContents(List<GenreTemplates> genreTemplates) {
        GenreTemplates setGenreTemplate = genreTemplates.get(0);

        List<GenreContents> setGenreStringList = setGenreTemplate.getGenreContents();

        return setGenreStringList;
    }

    //TODO インターフェースとかで下の二つのメソッドをいい感じにまとめられないか要検討
    /**
     * ユーザーがセットしているターゲットテンプレートの中身（Contents）の内容をStringリストとして取得する。
     * @param ユーザーID
     * @return　セットしているターゲットテンプレートの中身のStringのリスト
     */
    public List<String> getSetTargetStringList(List<TargetTemplates> targetTemplates) {
        TargetTemplates setTargetTemplate = targetTemplates.get(0);

        List<String> setTargetStringList = setTargetTemplate.getTargetContents().stream()
        .map(TargetContents::getContent)
        .collect(Collectors.toList());

        return setTargetStringList;
    }

    /**
     * ユーザーがセットしているジャンルテンプレートの中身（Contents）の一覧のリストを取得する。
     * @param ユーザーID
     * @return　セットしているジャンルテンプレートの中身のStringのリスト
     */
    public List<String> getSetGenreStringList(List<GenreTemplates> genreTemplates) {
        GenreTemplates setGenreTemplate = genreTemplates.get(0);

        List<String> setGenreStringList = setGenreTemplate.getGenreContents().stream()
        .map(GenreContents::getContent)
        .collect(Collectors.toList());

        return setGenreStringList;
    }
    
    /**
     * ユーザーがセットしているターゲットテンプレートのidを取得する処理
     */
    public Integer getSetTargetId(List<TargetTemplates> targetTemplates) {
        TargetTemplates setTargetTemplate = targetTemplates.get(0);
        return setTargetTemplate.getId();
    }

    /**
     * ユーザーがセットしているジャンルテンプレートのidを取得する処理
     */
    public Integer getSetGenreId(List<GenreTemplates> genreTemplates) {
        GenreTemplates setGenreTemplate = genreTemplates.get(0);
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
     * 現在セットされているテンプレートのコンテンツを削除する
     * @param genreTemplates テンプレートのリスト
     * @param deleteGenre 削除するコンテンツ
     */
    @Transactional
    public void deleteContent(List<Templates> templates, Integer id, ElementType type) {
        Templates setTemplate = templates.get(0);
        QuizElementDao repository = repositoies.get(type);
        repository.delteSetContent(id, setTemplate);
        
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

    /**
     * デフォルトのジャンルテンプレートにcontetsを追加する
     * @param genreTemplates 現在のデフォルトテンプレート
     * @return newTemplates 新たに追加したデフォルトテンプレート
     */
    public List<GenreTemplates> addDefaultGenre(List<GenreTemplates> genreTemplates, String newGenre) {
        GenreTemplates newTemplate = genreTemplates.get(0);
        GenreContents newContents = new GenreContents();
        newContents.setContent(newGenre);
        newTemplate.getGenreContents().add(newContents);
        
        List<GenreTemplates> newTemplates = new ArrayList<GenreTemplates>();
        newTemplates.add(newTemplate);

        return newTemplates;
    }

    /**
     * デフォルトのターゲットテンプレートにcontetsを追加する
     * @param targetTemplates 現在のデフォルトテンプレート
     * @return newTemplates 新たに追加したデフォルトテンプレート
     */
    public List<TargetTemplates> addDefaultTarget(List<TargetTemplates> targetTemplates, String newTarget) {
        TargetTemplates newTemplate = targetTemplates.get(0);
        TargetContents newContents = new TargetContents();
        newContents.setContent(newTarget);
        newTemplate.getTargetContents().add(newContents);
        
        List<TargetTemplates> newTemplates = new ArrayList<TargetTemplates>();
        newTemplates.add(newTemplate);

        return newTemplates;
    }

     /**
     * デフォルトのジャンルテンプレートからcontetsを削除する
     * @param genreTemplates 現在のデフォルトテンプレート
     * @param delGenre 削除するcontents
     * @return newTemplates contentsを削除したデフォルトテンプレート
     */
    public List<GenreTemplates> deleteDefaultGenreContent(List<GenreTemplates> genreTemplates, Integer delGenre) {
        GenreTemplates newTemplate = genreTemplates.get(0);
        
        GenreContents deleteContents = newTemplate.getGenreContents().stream()
                .filter(content -> content.getId().equals(delGenre))
                .findFirst()
                .orElse(null);
        newTemplate.getGenreContents().remove(deleteContents);
        List<GenreTemplates> newTemplates = new ArrayList<GenreTemplates>();
        newTemplates.add(newTemplate);

        return newTemplates;
    }

    /**
     * デフォルトのターゲットプレートからcontetsを削除する
     * @param targetTemplates 現在のデフォルトテンプレート
     * @param delTarget 削除するcontents
     * @return newTemplates contentsを削除したデフォルトテンプレート
     */
    public List<TargetTemplates> deleteDefaultTargetContent(List<TargetTemplates> targetTemplates, Integer delTarget) {
        TargetTemplates newTemplate = targetTemplates.get(0);
        
        TargetContents deleteContents = newTemplate.getTargetContents().stream()
                .filter(content -> content.getId().equals(delTarget))
                .findFirst()
                .orElse(null);
        newTemplate.getTargetContents().remove(deleteContents);
        List<TargetTemplates> newTemplates = new ArrayList<TargetTemplates>();
        newTemplates.add(newTemplate);

        return newTemplates;
    }

    /**
     * テンプレートのリストからidのテンプレートを抽出する
     * @param List<Templates> テンプレートのリスト
     * @param id テンプレートID
     * @return Templates idに該当するテンプレート
     */
    public Templates getTemplateById(List<Templates> templates, Integer id) {
        Templates editTemplate = templates.stream()
        .filter(template -> template.getId().equals(id))
        .findFirst()
        .orElse(null);

        return editTemplate;
    } 

    /**
     * テンプレートのコンテンツを一括削除する
     * @param genreTemplates テンプレートのリスト
     * @param tempid 削除する対象のテンプレートのID
     * @param deleteContentList 削除するコンテンツのIDのリスト
     * @param type ジャンルorターゲット
     */
    @Transactional
    public void bulkDeleteContents(List<Templates> templates, Integer tempId , List<Integer> deleteIdList, ElementType type) {
        Templates Template = getTemplateById(templates, tempId);
        
        QuizElementDao repository = repositoies.get(type);
        repository.bulkDeleteContents(deleteIdList, Template);
        
    }
}
