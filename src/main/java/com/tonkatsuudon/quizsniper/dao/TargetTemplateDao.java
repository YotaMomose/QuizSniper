package com.tonkatsuudon.quizsniper.dao;

import java.util.List;

import com.tonkatsuudon.quizsniper.entity.TargetTemplates;




public interface TargetTemplateDao {   
    List<TargetTemplates> findTargetTemplates(String userId);

    void unsetTargetTemplate(Integer id);

    void setTargetTemplate(Integer id);

    public void addTargetContent(String content, TargetTemplates setTemplate);
} 
