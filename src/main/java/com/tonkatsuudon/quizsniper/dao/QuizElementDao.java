package com.tonkatsuudon.quizsniper.dao;

import java.util.List;

import com.tonkatsuudon.quizsniper.entity.Templates;

public interface QuizElementDao {
    void delteSetContent(Integer id, Templates setTemplate);

    void bulkDeleteContents(List<Integer> deleteIdList, Templates Template);
}
