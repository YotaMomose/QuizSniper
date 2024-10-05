package com.tonkatsuudon.quizsniper.dao;

import java.util.List;

import com.tonkatsuudon.quizsniper.entity.GenreTemplates;



public interface GenreTemplateDao {
    List<GenreTemplates> findGenreTemplates(String userId);

    void unsetGenreTemplate(Integer id);

    void setGenreTemplate(Integer id);
}
