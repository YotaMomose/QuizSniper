package com.tonkatsuudon.quizsniper.repository;


import java.util.List;


import com.tonkatsuudon.quizsniper.dao.QuizSniperDao;
import com.tonkatsuudon.quizsniper.entity.TargetContents;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TargetRepository implements QuizSniperDao {
    private final EntityManager entityManager;

    @Override
    public List<TargetContents> findTargetContent(TargetTemplates templates) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TargetContents> query = builder.createQuery(TargetContents.class);
        Root<TargetContents> targetContents = query.from(TargetContents.class);
        Predicate condition = builder.equal(targetContents.get("targetTemplates"), templates);
        query.select(targetContents);  
        query.where(condition);

        List<TargetContents> targets = entityManager.createQuery(query).getResultList();


        return targets;
    }
    
}
