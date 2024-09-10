package com.tonkatsuudon.quizsniper.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tonkatsuudon.quizsniper.entity.GenreContents;
import com.tonkatsuudon.quizsniper.entity.GenreTemplates;
import com.tonkatsuudon.quizsniper.entity.TargetContents;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;
import com.tonkatsuudon.quizsniper.repository.GenreRepository;
import com.tonkatsuudon.quizsniper.repository.TargetRepository;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class QuizSniperController {

    @PersistenceContext
    private EntityManager entityManager;
    TargetRepository targetRepository;
    GenreRepository genreRepository;

    @PostConstruct
    public void init() {
        targetRepository = new TargetRepository(entityManager);
        genreRepository = new GenreRepository(entityManager);
    }
    /* 初期表示（メイン画面） */
    @GetMapping("/")
    public ModelAndView showMainView(ModelAndView mv) {

        // TODO　仮としてユーザーIDを「tonkatsu」の固定値で設定
        // ログイン処理を実装時にログインユーザーのIDで検索をするように変更

        //ターゲット
        List<TargetTemplates> targetTemplates = targetRepository.findTargetTemplates("tonkatsu");

        TargetTemplates setTargetTemplate = targetTemplates.get(0);

        List<String> setTargetContets = setTargetTemplate.getTargetContents().stream()
        .map(TargetContents::getContent)
        .collect(Collectors.toList());
        
        mv.addObject("setTargetContets", setTargetContets);
        mv.addObject("targetTemplates", targetTemplates);

        //ジャンル
        List<GenreTemplates> genreTemplates = genreRepository.findGenreTemplates("tonkatsu");

        GenreTemplates setGenreTemplate = genreTemplates.get(0);

        List<String> setGenreContets = setGenreTemplate.getGenreContents().stream()
        .map(GenreContents::getContent)
        .collect(Collectors.toList());
        
        mv.addObject("setGenreContets", setGenreContets);
        mv.addObject("genreTemplates", genreTemplates);



        

        mv.setViewName("index");
        return mv;
    }

    /* 設定画面遷移 */
    @GetMapping("/setting")
    public ModelAndView showSettingView(ModelAndView mv) {
        mv.setViewName("setting");
        return mv;
    }
    
}
