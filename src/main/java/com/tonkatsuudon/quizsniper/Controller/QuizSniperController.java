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

import com.tonkatsuudon.quizsniper.entity.GenreContents;
import com.tonkatsuudon.quizsniper.entity.GenreTemplates;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;
import com.tonkatsuudon.quizsniper.entity.Templates;
import com.tonkatsuudon.quizsniper.entity.Users;
import com.tonkatsuudon.quizsniper.form.LoginData;
import com.tonkatsuudon.quizsniper.repository.GenreRepository;
import com.tonkatsuudon.quizsniper.repository.TargetRepository;
import com.tonkatsuudon.quizsniper.service.LoginService;
import com.tonkatsuudon.quizsniper.service.TemplateService;
import com.tonkatsuudon.quizsniper.type.ElementType;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class QuizSniperController {
    @PersistenceContext
    private EntityManager entityManager;
    private TargetRepository targetRepository;
    private GenreRepository genreRepository;

    @PostConstruct
    public void init() {
        targetRepository = new TargetRepository(entityManager);
        genreRepository = new GenreRepository(entityManager);
    }

    private final TemplateService templateService;
    private final LoginService loginService;

    private final String DEFAULT_USER_ID = "default";

    /* 初期表示（メイン画面） */
    @GetMapping("/")
    public ModelAndView showMainView(ModelAndView mv, @ModelAttribute @Validated LoginData loginData,
            BindingResult result, HttpSession session) {

        // ログイン情報がない場合はテンプレートに初期値をセット
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) {
            List<GenreTemplates> genreTemplates = (List<GenreTemplates>) session.getAttribute("genreTemplates"); 
            // すでにセッション情報がある場合はセッションに初期値をセットしない
            if(genreTemplates != null) {
                mv.setViewName("index");
                return mv;
            }

            // ログインしない場合のターゲットテンプレートの一覧
            List<TargetTemplates> targetTemplates = targetRepository.findTargetTemplates(DEFAULT_USER_ID);
            session.setAttribute("targetTemplates", targetTemplates);

            // ログインしない場合のジャンルテンプレートの一覧
            genreTemplates = genreRepository.findGenreTemplates(DEFAULT_USER_ID);
            session.setAttribute("genreTemplates", genreTemplates);

            //セットされているターゲットのContentsのリスト
            session.setAttribute("setTargetContents", templateService.getsetTargetContents(targetTemplates));
            
            //セットされているジャンルのContentsのリスト
            session.setAttribute("setGenreContents", templateService.getsetGenreContents(genreTemplates));
            
            // セットされているターゲットのStirngのリスト
            session.setAttribute("setTargetStringList", templateService.getSetTargetStringList(targetTemplates));

            // セットされているジャンルStringのリスト
            session.setAttribute("setGenreStringList", templateService.getSetGenreStringList(genreTemplates));



        } else {
            //ログイン情報がある場合は再度DBにデータを取得しにいく

            String userId = loginUser.getId();
            // ユーザー情報をセッションに登録
            session.setAttribute("loginUser", loginUser);

            // ログインユーザーに紐づくターゲットテンプレートの一覧
            List<TargetTemplates> targetTemplates = targetRepository.findTargetTemplates(userId);
            session.setAttribute("targetTemplates", targetTemplates);

            // ログインユーザーに紐づくジャンルテンプレートの一覧
            List<GenreTemplates> genreTemplates = genreRepository.findGenreTemplates(userId);
            session.setAttribute("genreTemplates", genreTemplates);

            //セットされているターゲットのContentsのリスト
            session.setAttribute("setTargetContents", templateService.getsetTargetContents(targetTemplates));
            
            //セットされているジャンルのContentsのリスト
            session.setAttribute("setGenreContents", templateService.getsetGenreContents(genreTemplates));
            
            // セットされているターゲットのStirngのリスト
            session.setAttribute("setTargetStringList", templateService.getSetTargetStringList(targetTemplates));

            // セットされているジャンルStringのリスト
            session.setAttribute("setGenreStringList", templateService.getSetGenreStringList(genreTemplates));

            // セットされているターゲットのid
            session.setAttribute("setTargetId", templateService.getSetTargetId(targetTemplates));

            // セットされているジャンルのid
            session.setAttribute("setGenreId", templateService.getSetGenreId(genreTemplates));
            
            }
        mv.setViewName("index");
        return mv;
    }

    /* ログイン処理（メイン画面） */
    @PostMapping("/")
    public ModelAndView showMainViewFromLogin(ModelAndView mv, @ModelAttribute @Validated LoginData loginData,
            BindingResult result, HttpSession session) {

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

        // ユーザー情報をセッションに登録
        session.setAttribute("loginUser", loginUser);

        // ログインユーザーに紐づくターゲットテンプレートの一覧
        List<TargetTemplates> targetTemplates = targetRepository.findTargetTemplates(userId);
        session.setAttribute("targetTemplates", targetTemplates);

        // ログインユーザーに紐づくジャンルテンプレートの一覧
        List<GenreTemplates> genreTemplates = genreRepository.findGenreTemplates(userId);
        session.setAttribute("genreTemplates", genreTemplates);

        //セットされているターゲットのContentsのリスト
        session.setAttribute("setTargetContents", templateService.getsetTargetContents(targetTemplates));
        
        //セットされているジャンルのContentsのリスト
        session.setAttribute("setGenreContents", templateService.getsetGenreContents(genreTemplates));
        
        // セットされているターゲットのStirngのリスト
        session.setAttribute("setTargetStringList", templateService.getSetTargetStringList(targetTemplates));

        // セットされているジャンルStringのリスト
        session.setAttribute("setGenreStringList", templateService.getSetGenreStringList(genreTemplates));

        // セットされているターゲットのid
        session.setAttribute("setTargetId", templateService.getSetTargetId(targetTemplates));

        // セットされているジャンルのid
        session.setAttribute("setGenreId", templateService.getSetGenreId(genreTemplates));

        

        mv.setViewName("index");
        return mv;
    }

    /* ログイン画面遷移 */
    @GetMapping("/login")
    public ModelAndView showLoginView(ModelAndView mv, @ModelAttribute @Validated LoginData loginData,
            BindingResult result) {
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

    /* テンプレートセット(ジャンル) */
    @PostMapping("/setGenreTemplate/")
    public String setGenreTmp(@RequestParam("id") Integer id, HttpSession session) {
        Integer currentSetId = (Integer) session.getAttribute("setGenreId");
        //現在のテンプレートのセットを解除
        templateService.switchSetGenreTemplate(currentSetId,id);
        
        return "redirect:/";
    }

    /* テンプレートセット(ターゲット) */
    @PostMapping("/setTargetTemplate/")
    public String setTargetTmp(@RequestParam("id") Integer id, HttpSession session) {
        Integer currentSetId = (Integer) session.getAttribute("setTargetId");
        //現在のテンプレートのセットを解除
        templateService.switchSetTargetTemplate(currentSetId,id);
        
        return "redirect:/";
    }
    

    /* ジャンル追加処理 */
    @PostMapping("/addgenre")
    public String addGenre(@RequestParam("newGenre") String newGenre, HttpSession session) {
        List<GenreTemplates> genreTemplates = (List<GenreTemplates>) session.getAttribute("genreTemplates");
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) {
            // ログイン情報がない場合はテンプレートに追加
            List<GenreTemplates> newTemplates = templateService.addDefaultGenre(genreTemplates, newGenre);

            
            session.setAttribute("genreTemplates", newTemplates);
            session.setAttribute("setGenreContents", templateService.getsetGenreContents(newTemplates));
            session.setAttribute("setGenreStringList", templateService.getSetGenreStringList(newTemplates));
        } else {
            // ログイン情報がある場合はDBのデータを更新
            templateService.addNewGenreContent(genreTemplates, newGenre);
        }
        

        return "redirect:/";
    }

    /* ジャンル削除処理 */
    @PostMapping("/delgenre")
    public String deleteGenre(@RequestParam("delGenre") Integer deleteGenre, HttpSession session) {
        List<Templates> genreTemplates = (List<Templates>) session.getAttribute("genreTemplates");
        templateService.deleteContent(genreTemplates, deleteGenre, ElementType.Genre);
        return "redirect:/";
    }

    /* ターゲット追加処理 */
    @PostMapping("/addtarget")
    public String addTarget(@RequestParam("newTarget") String newTarget, HttpSession session) {
        List<TargetTemplates> targetTemplates = (List<TargetTemplates>) session.getAttribute("targetTemplates");
        templateService.addNewTargetContent(targetTemplates, newTarget);

        return "redirect:/";
    }

    /* ターゲット削除処理 */
    @PostMapping("/deltarget")
    public String deleteTarget(@RequestParam("delTarget") Integer deleteTarget, HttpSession session) {
        List<Templates> targetTemplates = (List<Templates>) session.getAttribute("targetTemplates");
        System.out.println("ジャンルのID：" + deleteTarget);
        templateService.deleteContent(targetTemplates, deleteTarget, ElementType.Target);
        return "redirect:/";
    }

    /* 設定画面遷移 */
    @GetMapping("/setting")
    public ModelAndView showSettingView(ModelAndView mv) {
        mv.setViewName("setting");
        return mv;
    }

}
