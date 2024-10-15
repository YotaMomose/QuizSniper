package com.tonkatsuudon.quizsniper.repository;

import java.util.List;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tonkatsuudon.quizsniper.dao.GenreTemplateDao;
import com.tonkatsuudon.quizsniper.dao.QuizElementDao;
import com.tonkatsuudon.quizsniper.entity.GenreContents;
import com.tonkatsuudon.quizsniper.entity.GenreTemplates;
import com.tonkatsuudon.quizsniper.entity.Templates;
import com.tonkatsuudon.quizsniper.entity.Users;

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
     * ユーザーIDに紐づくジャンルテンプレートとその内容（GenreContets）を取得する
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
    public void delteSetContent(String content, Templates setTemplate) {
        //try {
            
             // 既存のエンティティを検索
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<GenreContents> query = cb.createQuery(GenreContents.class);
            Root<GenreContents> root = query.from(GenreContents.class);
            
            // content と setTemplate に基づいて削除するエンティティを検索
            query.select(root).where(
                cb.equal(root.get("id"), 15)
            );
            // 検索結果を取得
            GenreContents genreContent = entityManager.createQuery(query).getSingleResult();
            
            System.out.println("GenreContent content: " + genreContent.getContent());
            System.out.println("GenreContent content: " + genreContent.getId());
            
            // 検索したエンティティを削除
            if (genreContent != null) {
                GenreTemplates genreTemplates = (GenreTemplates)setTemplate;
                genreTemplates.getGenreContents().remove(genreContent);
                entityManager.merge(genreTemplates);
                entityManager.flush(); 
            }
            genreContent = entityManager.createQuery(query).getSingleResult();
            System.out.println(genreContent.getContent());
            System.out.println(genreContent.getId());
            System.out.println(genreContent.getGenreTemplates().getId());
       // } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            
         //   System.out.println(e);
       // }
        
    }

    @Override
    public void delteTest(String id) {
        try {
             // 既存のエンティティを検索
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Users> query = cb.createQuery(Users.class);
            Root<Users> root = query.from(Users.class);
            
            // content と setTemplate に基づいて削除するエンティティを検索
            query.select(root).where(
                cb.equal(root.get("id"), id)
            );
            // 検索結果を取得
            Users user = entityManager.createQuery(query).getSingleResult();
            System.out.println("GenreContent content: " + user.getId());
            System.out.println("GenreContent content: " + user.getName());
            
            // 検索したエンティティを削除
            if (user != null) {
                entityManager.remove(user);
            }
        } catch (Exception e) {
            // TODO: エラーハンドリング（例: ログ出力など）
            
            System.out.println(e);
        }
        
    }

    



}
