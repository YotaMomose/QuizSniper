package com.tonkatsuudon.quizsniper.dao;

import java.util.List;

import com.tonkatsuudon.quizsniper.entity.Templates;

public interface QuizElementDao {
    void delteSetContent(Integer id, Templates setTemplate);

    void bulkDeleteContents(List<Integer> deleteIdList, Templates Template);

    void addContent(String newContent, Templates ediTemplate);

    Templates findTemplateById(Integer id);

    void addNewTemplate(String templateName,List<String> templateContents,String userId);

    void deleteTemplate(Integer id);

    void templateInitialSetup(Templates templates, String userId);
}
