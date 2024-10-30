package com.tonkatsuudon.quizsniper.repository;

import java.util.List;


import org.springframework.stereotype.Repository;
import com.tonkatsuudon.quizsniper.dao.GenreTemplateDao;
import com.tonkatsuudon.quizsniper.dao.QuizElementDao;
import com.tonkatsuudon.quizsniper.entity.GenreContents;
import com.tonkatsuudon.quizsniper.entity.GenreTemplates;
import com.tonkatsuudon.quizsniper.entity.Templates;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
public class GenreRepository implements GenreTemplateDao, QuizElementDao {
    private final EntityManager entityManager;


    /**
     * ユーザーIDに紐づくジャンルテンプレートとその内容（GenreContents）を取得する
     * リストの1番最初の要素としてセットしているテンプレートを取得する。
     * @param ユーザーID
     * @return ジャンルテンプレートのリスト
     */
    @Override
    public List<GenreTemplates> findGenreTemplates(String userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GenreTemplates> query = builder.createQuery(GenreTemplates.class);
        Root<GenreTemplates> genreTemplatesRoot = query.from(GenreTemplates.class);
        Predicate userCondition = builder.equal(genreTemplatesRoot.get("userId"), userId);
        query.where(userCondition);
        query.orderBy(builder.desc(genreTemplatesRoot.get("isSet")));
        List<GenreTemplates> results = entityManager.createQuery(query).getResultList();
        return results;
    }

    /**
     * 引数で受け取ったidのGenreTemplatesのisSetをtrueに更新する
     * @param id
     */
    @Override
    public void setGenreTemplate(Integer id) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<GenreTemplates> query = cb.createQuery(GenreTemplates.class);
            Root<GenreTemplates> root = query.from(GenreTemplates.class);
            
            query.select(root).where(cb.equal(root.get("id"), id));

            GenreTemplates genreTemplate = entityManager.createQuery(query).getSingleResult();
            
            if (genreTemplate != null) {
                genreTemplate.setSet(true);
                entityManager.persist(genreTemplate); 
            }
        } catch (Exception e) {
            // TODO 結果が見つからない場合の処理（例: ログ出力など）
            System.out.println("GenreTemplate with ID " + id + " not found.");
            System.out.println(e);
        }

    }


    /**
     * 引数で受け取ったidのGenreTemplatesのisSetをfalseに更新する
     * @param id
     */
    @Override
    public void unsetGenreTemplate(Integer id) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<GenreTemplates> query = cb.createQuery(GenreTemplates.class);
            Root<GenreTemplates> root = query.from(GenreTemplates.class);
            
            query.select(root).where(cb.equal(root.get("id"), id));

            GenreTemplates genreTemplate = entityManager.createQuery(query).getSingleResult();
            
            if (genreTemplate != null) {
                genreTemplate.setSet(false);
                entityManager.persist(genreTemplate); 
            }
        } catch (Exception e) {
            // TODO 結果が見つからない場合の処理（例: ログ出力など）
            System.out.println("GenreTemplate with ID " + id + " not found.");
            System.out.println(e);
        }

    }

    /**
     * 引数で受け取ったcontentとtemplateidを持つGenreContentを追加する
     * @param content
     * @param templateId
     */
    @Override
    public void addGenreContent(String content, GenreTemplates setTemplate) {
        try {
            GenreContents genreContent = new GenreContents();
            genreContent.setContent(content);
            genreContent.setGenreTemplates(setTemplate);
            entityManager.persist(genreContent);
        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            
            System.out.println(e);
        }
    }

    @Override
    public void delteSetContent(Integer id, Templates setTemplate) {
        try {
            
            // 検索結果を取得
            GenreTemplates genreTemplates = (GenreTemplates)setTemplate;
            List<GenreContents> genreContents = genreTemplates.getGenreContents();
            GenreContents deleteContents = genreContents.stream()
                .filter(content -> content.getId().equals(id))
                .findFirst()
                .orElse(null);
            
            // 検索したエンティティを削除
            if (deleteContents != null) {
                genreTemplates.getGenreContents().remove(deleteContents);
                entityManager.merge(genreTemplates);
            }
            
        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            
            System.out.println(e);
        }
        
    }
        
    

    



}
