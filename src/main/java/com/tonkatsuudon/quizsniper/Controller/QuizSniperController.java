package com.tonkatsuudon.quizsniper.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

    /* 初期表示（メイン画面） */
    @GetMapping("/")
    public ModelAndView showMainView(ModelAndView mv, @ModelAttribute @Validated LoginData loginData,
            BindingResult result, HttpSession session) {

        // ログイン情報がない場合はテンプレートに初期値をセット
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) {
            List<String> setTargetContets = List.of("佐藤", "鈴木", "高橋", "田中", "伊藤");
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
                    "小学校時代に関すること");

            //ターゲットのデフォルト一覧
            TargetTemplates targetTemplates = new TargetTemplates();
            session.setAttribute("targetTemplates", targetTemplates.getDefaultTmplate(setTargetContets));

            GenreTemplates genreTemplates = new GenreTemplates();
            session.setAttribute("genreTemplates", genreTemplates.getDefaultTmplate(setGenreContets));

            // ターゲット
            session.setAttribute("setTargetContets", setTargetContets);

            // ジャンル
            session.setAttribute("setGenreContets", setGenreContets);
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

            // セットされているターゲット
            session.setAttribute("setTargetContets", templateService.getSetTargetContents(targetTemplates));

            // セットされているジャンル
            session.setAttribute("setGenreContets", templateService.getSetGenreContents(genreTemplates));

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

        // セットされているターゲット
        session.setAttribute("setTargetContets", templateService.getSetTargetContents(targetTemplates));

        // セットされているジャンル
        session.setAttribute("setGenreContets", templateService.getSetGenreContents(genreTemplates));

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
        templateService.addNewGenreContent(genreTemplates, newGenre);

        return "redirect:/";
    }

    /* ジャンル削除処理 */
    @PostMapping("/delgenre")
    public String deleteGenre(@RequestParam("delGenre") String deleteGenre, HttpSession session) {
        List<Templates> genreTemplates = (List<Templates>) session.getAttribute("genreTemplates");
        templateService.deleteContent(genreTemplates, deleteGenre, ElementType.Genre);
        //templateService.deletetest("test");
        return "redirect:/";
    }

    /* ターゲット追加処理 */
    @PostMapping("/addtarget")
    public String addTarget(@RequestParam("newTarget") String newTarget, HttpSession session) {
        List<TargetTemplates> targetTemplates = (List<TargetTemplates>) session.getAttribute("targetTemplates");
        templateService.addNewTargetContent(targetTemplates, newTarget);

        return "redirect:/";
    }

    /* 設定画面遷移 */
    @GetMapping("/setting")
    public ModelAndView showSettingView(ModelAndView mv) {
        mv.setViewName("setting");
        return mv;
    }

}
