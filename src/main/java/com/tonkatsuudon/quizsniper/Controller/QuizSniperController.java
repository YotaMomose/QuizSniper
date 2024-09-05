package com.tonkatsuudon.quizsniper.controller;


import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tonkatsuudon.quizsniper.entity.GenreContents;
import com.tonkatsuudon.quizsniper.entity.GenreTemplates;
import com.tonkatsuudon.quizsniper.entity.TargetContents;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;
import com.tonkatsuudon.quizsniper.entity.Users;
import com.tonkatsuudon.quizsniper.repository.GenreRepository;
import com.tonkatsuudon.quizsniper.repository.TargetRepository;
import com.tonkatsuudon.quizsniper.repository.UsersRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class QuizSniperController {
    private final UsersRepository usersRepository;

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

        List<TargetTemplates> targetTemplates = targetRepository.findTargetTemplates("tonkatsu");
        for (TargetTemplates template : targetTemplates) {
            System.out.println("Template Name: " + template.getName());
            List<TargetContents> targetContents = template.getTargetContents();
            for (TargetContents content : targetContents) {
                System.out.println("Content: " + content.getContent());
            }
        }

        List<GenreTemplates> genreTemplates = genreRepository.findGenreTemplates("tonkatsu");
        for (GenreTemplates template : genreTemplates) {
            System.out.println("Template Name: " + template.getName());
            List<GenreContents> genreContents = template.getGenreContents();
            for (GenreContents content : genreContents) {
                System.out.println("Content: " + content.getContent());
            }
        }


        

        mv.setViewName("index");
        List<Users> usersList = usersRepository.findAll();
        mv.addObject("usersList", usersList);
        return mv;
    }

    /* 設定画面遷移 */
    @GetMapping("/setting")
    public ModelAndView showSettingView(ModelAndView mv) {
        mv.setViewName("setting");
        return mv;
    }
    
}
