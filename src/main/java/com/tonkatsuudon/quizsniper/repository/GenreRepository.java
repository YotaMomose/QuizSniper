package com.tonkatsuudon.quizsniper.repository;

import java.util.List;

import com.tonkatsuudon.quizsniper.dao.GenreTemplateDao;
import com.tonkatsuudon.quizsniper.entity.GenreTemplates;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GenreRepository implements GenreTemplateDao {
    private final EntityManager entityManager;


    /**
     * ユーザーIDに紐づくジャンルテンプレートとその内容（GenreContets）を取得する
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
        List<GenreTemplates> results = entityManager.createQuery(query).getResultList();
        return results;
    }

}
