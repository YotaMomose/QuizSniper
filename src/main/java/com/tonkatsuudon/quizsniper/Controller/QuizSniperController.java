package com.tonkatsuudon.quizsniper.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tonkatsuudon.quizsniper.entity.TargetContents;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;
import com.tonkatsuudon.quizsniper.entity.Users;
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

    @PostConstruct
    public void init() {
        targetRepository = new TargetRepository(entityManager);
    }
    /* 初期表示（メイン画面） */
    @GetMapping("/")
    public ModelAndView showMainView(ModelAndView mv) {

        //TODO　仮としてセットしているターゲットテンプレートを１固定として取得
        TargetTemplates targetTemplates = new TargetTemplates();
        targetTemplates.setId(1);
        List<TargetContents> targetContents = targetRepository.findTargetContent(targetTemplates);

        for(TargetContents content : targetContents) {
            System.out.println(content.getContent());
            System.out.println(content.getTargetTemplates().getName());
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
