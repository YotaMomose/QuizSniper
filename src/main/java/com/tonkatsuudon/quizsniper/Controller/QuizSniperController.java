package com.tonkatsuudon.quizsniper.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tonkatsuudon.quizsniper.entity.Users;
import com.tonkatsuudon.quizsniper.repository.UsersRepository;

import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class QuizSniperController {
    private final UsersRepository usersRepository;
    /* 初期表示（メイン画面） */
    @GetMapping("/")
    public ModelAndView showMainView(ModelAndView mv) {
        

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
