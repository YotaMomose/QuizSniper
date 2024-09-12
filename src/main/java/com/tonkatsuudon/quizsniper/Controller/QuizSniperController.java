package com.tonkatsuudon.quizsniper.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tonkatsuudon.quizsniper.service.TemplateService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class QuizSniperController {
    private final TemplateService targetService;

    /* 初期表示（メイン画面） */
    @GetMapping("/")
    public ModelAndView showMainView(ModelAndView mv) {

        // TODO　仮としてユーザーIDを「tonkatsu」の固定値で設定
        // ログイン処理を実装時にログインユーザーのIDで検索をするように変更


        //ターゲット
        mv.addObject("setTargetContets", targetService.getSetTargetContents("tonkatsu"));

        //ジャンル
        mv.addObject("setGenreContets", targetService.getSetGenreContents("tonkatsu"));



        

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
