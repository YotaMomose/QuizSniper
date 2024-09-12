package com.tonkatsuudon.quizsniper.dao;

import java.util.List;

import com.tonkatsuudon.quizsniper.entity.TargetTemplates;




public interface TargetTemplateDao {
    
    List<TargetTemplates> findTargetTemplates(String userId);

} 
