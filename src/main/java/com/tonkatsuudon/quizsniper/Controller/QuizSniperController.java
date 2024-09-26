package com.tonkatsuudon.quizsniper.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.tonkatsuudon.quizsniper.entity.Users;
import com.tonkatsuudon.quizsniper.form.LoginData;
import com.tonkatsuudon.quizsniper.service.LoginService;
import com.tonkatsuudon.quizsniper.service.TemplateService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class QuizSniperController {
    private final TemplateService templateService;
    private final LoginService loginService;

    /* 初期表示（メイン画面） */
    @GetMapping("/")
    public ModelAndView showMainView(ModelAndView mv, @ModelAttribute @Validated LoginData loginData, BindingResult result, HttpSession session) {

        //ログイン情報がない場合はテンプレートに初期値をセット
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) {
            List<String> setTargetContets = List.of("佐藤","鈴木","高橋","田中","伊藤");
            List<String> setGenreContets = List.of(
                "漢字問題",
                "一般常識問題",
                "スポーツのこと",
                "自分に関すること",
                "５教科の問題",
                "ものの名前",
                "動物に関すること",
                "乗り物に関すること",
                "食べ物に関すること",
                "オールジャンル",
                "有名人・芸能人に関すること",
                "音楽に関すること",
                "生き物に関すること",
                "アニメ・漫画・ゲームに関すること",
                "エンタメに関すること",
                "地理・場所に関すること",
                "ファッションに関すること",
                "中学校時代に関すること",
                "高校時代に関すること",
                "小学校時代に関すること"
            );
            //ターゲット
            session.setAttribute("setTargetContets", setTargetContets);

            //ジャンル
            session.setAttribute("setGenreContets", setGenreContets);
        }


        mv.setViewName("index");
        return mv;
    }

    /* 初期表示（メイン画面） */
    @PostMapping("/")
    public ModelAndView showMainViewFromLogin(ModelAndView mv, @ModelAttribute @Validated LoginData loginData, BindingResult result, HttpSession session) {

        // エラー時はログイン画面へ遷移
        if (result.hasErrors()) {
            mv.setViewName("login");
            return mv;
        }

        Users loginInfo = loginData.toEntity();
        Users loginUser = loginService.fetchLoginUserData(loginInfo);

        if (loginUser == null) {
            result.reject("error.login", "ユーザーIDまたはパスワードが間違っています。");
            mv.setViewName("login");
            return mv;
        }

        String userId = loginUser.getId();

        //ユーザー情報をセッションに登録
        session.setAttribute("loginUser", loginUser);

        //ターゲット
        session.setAttribute("setTargetContets", templateService.getSetTargetContents(userId));

        //ジャンル
        session.setAttribute("setGenreContets", templateService.getSetGenreContents(userId));

        mv.setViewName("index");
        return mv;
    }

    /* ログイン画面遷移 */
    @GetMapping("/login")
    public ModelAndView showLoginView(ModelAndView mv,  @ModelAttribute @Validated LoginData loginData, BindingResult result) {
        mv.setViewName("login");
        mv.addObject("loginData", new LoginData());
        return mv;
    }

    /* ログアウト */
    @GetMapping("/logout")
    public String loout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    /* ジャンル追加処理 */
    @PostMapping("/addgenre")
    public String addGenre(@RequestParam("newGenre") String newGenre, HttpSession session) {
        List<String> setTargetContets = (List<String>) session.getAttribute("setTargetContets");
        List<String> newTargetContents = new ArrayList<>(setTargetContets);
        newTargetContents.add(newGenre);
        
        System.out.println(newGenre);
        session.setAttribute("setTargetContets", newTargetContents);
        return "redirect:/";
    }

    /* 設定画面遷移 */
    @GetMapping("/setting")
    public ModelAndView showSettingView(ModelAndView mv) {
        mv.setViewName("setting");
        return mv;
    }
    
}
