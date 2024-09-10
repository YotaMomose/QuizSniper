package com.tonkatsuudon.quizsniper.repository;


import java.util.List;


import com.tonkatsuudon.quizsniper.dao.TargetTemplateDao;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TargetRepository implements TargetTemplateDao {
    private final EntityManager entityManager;

    
    /**
     * ユーザーIDに紐づくターゲットテンプレートとその内容（TargetContets）を取得する。
     * リストの1番最初の要素としてセットしているテンプレートを取得する。
     * 
     * @param ユーザーID
     * @return ターゲットテンプレートのリスト
     */
    @Override
    public List<TargetTemplates> findTargetTemplates(String userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TargetTemplates> query = builder.createQuery(TargetTemplates.class);
        Root<TargetTemplates> targetTemplatesRoot = query.from(TargetTemplates.class);
        
        Predicate userCondition = builder.equal(targetTemplatesRoot.get("userId"), userId);
        query.where(userCondition);
        query.orderBy(builder.desc(targetTemplatesRoot.get("isSet")));
        List<TargetTemplates> results = entityManager.createQuery(query).getResultList();
        return results;
        
    }


    
}
