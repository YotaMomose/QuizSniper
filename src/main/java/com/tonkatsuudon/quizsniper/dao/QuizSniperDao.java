package com.tonkatsuudon.quizsniper.dao;

import java.util.List;

import com.tonkatsuudon.quizsniper.entity.TargetContents;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;



public interface QuizSniperDao {
    /**
     * ユーザーがセットしているターゲットの一覧を取得する
     * @param ターゲットテンプレートID
     * @return
     */
    List<TargetContents> findTargetContent(TargetTemplates templates);
} 
