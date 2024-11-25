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
import com.tonkatsuudon.quizsniper.form.RegisterData;
import com.tonkatsuudon.quizsniper.repository.GenreRepository;
import com.tonkatsuudon.quizsniper.repository.TargetRepository;
import com.tonkatsuudon.quizsniper.service.LoginService;
import com.tonkatsuudon.quizsniper.service.RegisterService;
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
    private final RegisterService registerService;


    private final String TYPE_GENRE = "genre";
    private final String TYPE_TARGET = "target";

    /* 初期表示（メイン画面） */
    @GetMapping("/")
    public ModelAndView showMainView(ModelAndView mv, @ModelAttribute @Validated LoginData loginData,
            BindingResult result, HttpSession session) {

        // ログイン情報がない場合はテンプレートに初期値をセット
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) {
            List<GenreTemplates> genreTemplates = (List<GenreTemplates>) session.getAttribute("genreTemplates");
            // すでにセッション情報がある場合はセッションに初期値をセットしない
            if (genreTemplates != null) {
                mv.setViewName("index");
                return mv;
            }
            GenreTemplates gTemplates = new GenreTemplates();
            TargetTemplates tTemplates = new TargetTemplates();
            // ログインしない場合のターゲットテンプレートの一覧
            List<TargetTemplates> targetTemplates = tTemplates.getDefaultTmplate();
            session.setAttribute("targetTemplates", targetTemplates);

            // ログインしない場合のジャンルテンプレートの一覧
            genreTemplates = gTemplates.getDefaultTmplate();
            session.setAttribute("genreTemplates", genreTemplates);

            // セットされているターゲットのContentsのリスト
            session.setAttribute("setTargetContents", templateService.getsetTargetContents(targetTemplates));

            // セットされているジャンルのContentsのリスト
            session.setAttribute("setGenreContents", templateService.getsetGenreContents(genreTemplates));

            // セットされているターゲットのStirngのリスト
            session.setAttribute("setTargetStringList", templateService.getSetTargetStringList(targetTemplates));

            // セットされているジャンルStringのリスト
            session.setAttribute("setGenreStringList", templateService.getSetGenreStringList(genreTemplates));

        } else {
            // ログイン情報がある場合は再度DBにデータを取得しにいく

            String userId = loginUser.getId();
            // ユーザー情報をセッションに登録
            session.setAttribute("loginUser", loginUser);

            // ログインユーザーに紐づくターゲットテンプレートの一覧
            List<TargetTemplates> targetTemplates = targetRepository.findTargetTemplates(userId);
            session.setAttribute("targetTemplates", targetTemplates);

            // ログインユーザーに紐づくジャンルテンプレートの一覧
            List<GenreTemplates> genreTemplates = genreRepository.findGenreTemplates(userId);
            session.setAttribute("genreTemplates", genreTemplates);

            // セットされているターゲットのContentsのリスト
            session.setAttribute("setTargetContents", templateService.getsetTargetContents(targetTemplates));

            // セットされているジャンルのContentsのリスト
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

        // セットされているターゲットのContentsのリスト
        session.setAttribute("setTargetContents", templateService.getsetTargetContents(targetTemplates));

        // セットされているジャンルのContentsのリスト
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
 // ↓新規登録画面----------------------------------------------------------------------------------------------------------------------------
    
    /* 新規登録画面遷移 */
    @GetMapping("/register")
    public ModelAndView showRegisterView(ModelAndView mv) {
        mv.setViewName("register");
        mv.addObject("registerData", new RegisterData());
        return mv;
    }

    /* 新規登録処理（メイン画面） */
    @PostMapping("/register")
    public ModelAndView newRegistration(ModelAndView mv, @ModelAttribute @Validated RegisterData registerData,
            BindingResult result, HttpSession session) {

        // エラー時は新規登録画面へ遷移
        if (result.hasErrors()) {
            mv.setViewName("register");
            return mv;
        }

        
        String inputId = registerData.getUserId();
        if (registerService.checkDuplicateId(inputId)) {
            result.reject("error.duplicate", "入力したIDは既に使用されています。別のIDを入力してください。");
            mv.setViewName("register");
            return mv;
        }

        //ユーザーテーブルに登録
        Users InputData = registerData.toEntity();
        String userId = registerData.getUserId();

        List<GenreTemplates> newGenreTemplates = (List<GenreTemplates>) session.getAttribute("genreTemplates");
        List<TargetTemplates> newTargetTemplates = (List<TargetTemplates>) session.getAttribute("targetTemplates");
        GenreTemplates newGenreTemplate = newGenreTemplates.get(0);
        TargetTemplates newTargetTemplate = newTargetTemplates.get(0);

        registerService.newRegister(InputData, newGenreTemplate, newTargetTemplate, userId);
        
        // // ユーザー情報をセッションに登録
        // session.setAttribute("loginUser", loginUser);

        mv.setViewName("registered");
        return mv;
    }

    /* テンプレートセット(ジャンル) */
    @PostMapping("/setGenreTemplate/")
    public String setGenreTmp(@RequestParam("id") Integer id, HttpSession session) {
        Integer currentSetId = (Integer) session.getAttribute("setGenreId");
        // 現在のテンプレートのセットを解除
        templateService.switchSetGenreTemplate(currentSetId, id);

        return "redirect:/";
    }

    /* テンプレートセット(ターゲット) */
    @PostMapping("/setTargetTemplate/")
    public String setTargetTmp(@RequestParam("id") Integer id, HttpSession session) {
        Integer currentSetId = (Integer) session.getAttribute("setTargetId");
        // 現在のテンプレートのセットを解除
        templateService.switchSetTargetTemplate(currentSetId, id);

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

        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) {
            // ログイン情報がない場合はテンプレートから削除
            // TODO 宣言がログイン時と非ログイン時で重複してるから改善したい。
            List<GenreTemplates> genreTemplates = (List<GenreTemplates>) session.getAttribute("genreTemplates");
            List<GenreTemplates> newTemplates = templateService.deleteDefaultGenreContent(genreTemplates, deleteGenre);

            session.setAttribute("genreTemplates", newTemplates);
            session.setAttribute("setGenreContents", templateService.getsetGenreContents(newTemplates));
            session.setAttribute("setGenreStringList", templateService.getSetGenreStringList(newTemplates));
        } else {
            List<Templates> genreTemplates = (List<Templates>) session.getAttribute("genreTemplates");
            templateService.deleteContent(genreTemplates, deleteGenre, ElementType.Genre);
        }
        return "redirect:/";
    }

    /* ターゲット追加処理 */
    @PostMapping("/addtarget")
    public String addTarget(@RequestParam("newTarget") String newTarget, HttpSession session) {
        List<TargetTemplates> targetTemplates = (List<TargetTemplates>) session.getAttribute("targetTemplates");
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) {
            // ログイン情報がない場合はテンプレートに追加
            List<TargetTemplates> newTemplates = templateService.addDefaultTarget(targetTemplates, newTarget);

            session.setAttribute("targetTemplates", newTemplates);
            session.setAttribute("setTargetContents", templateService.getsetTargetContents(newTemplates));
            session.setAttribute("setTargetStringList", templateService.getSetTargetStringList(newTemplates));
        } else {
            templateService.addNewTargetContent(targetTemplates, newTarget);
        }

        return "redirect:/";
    }

    /* ターゲット削除処理 */
    @PostMapping("/deltarget")
    public String deleteTarget(@RequestParam("delTarget") Integer deleteTarget, HttpSession session) {
        Users loginUser = (Users) session.getAttribute("loginUser");
        if (loginUser == null) {
            // ログイン情報がない場合はテンプレートから削除
            // TODO 宣言がログイン時と非ログイン時で重複してるから改善したい。
            List<TargetTemplates> targetTemplates = (List<TargetTemplates>) session.getAttribute("targetTemplates");
            List<TargetTemplates> newTemplates = templateService.deleteDefaultTargetContent(targetTemplates,
                    deleteTarget);

            session.setAttribute("targetTemplates", newTemplates);
            session.setAttribute("setTargetContents", templateService.getsetTargetContents(newTemplates));
            session.setAttribute("setTargetStringList", templateService.getSetTargetStringList(newTemplates));
        } else {
            List<Templates> targetTemplates = (List<Templates>) session.getAttribute("targetTemplates");
            templateService.deleteContent(targetTemplates, deleteTarget, ElementType.Target);
        }
        return "redirect:/";
    }

    /* テンプレート削除処理（ターゲット） */
    @PostMapping("/deleteTargetTemplate")
    public String deletetTargetTempate(@RequestParam("id") Integer id,
            HttpSession session) {
        // テンプレートを削除
        templateService.deleteTemplate(id, ElementType.Target);

        return "redirect:/";
    }

    /* テンプレート削除処理（ターゲット） */
    @PostMapping("/deleteGenreTemplate")
    public String deletetGenreTempate(@RequestParam("id") Integer id,
            HttpSession session) {
        // テンプレートを削除
        templateService.deleteTemplate(id, ElementType.Genre);

        return "redirect:/";
    }

    /* テンプレート編集画面遷移（ターゲット） */
    @PostMapping("/editTargetTemplate")
    public ModelAndView showeditTargetTempateView(ModelAndView mv, @RequestParam("id") Integer id,
            HttpSession session) {
        // 編集するテンプレートを特定
        TargetTemplates editTemplate = (TargetTemplates) templateService.findTemplateById(id, ElementType.Target);

        if (editTemplate == null) {
            System.out.println("null");
            // エラー画面に遷移
        }

        session.setAttribute("editTargetTemplateId", id);
        mv.addObject("editTemplate", editTemplate);
        mv.addObject("type", TYPE_TARGET);
        mv.setViewName("templateEdit");
        return mv;
    }

    /* テンプレート編集画面遷移（ジャンル） */
    @PostMapping("/editGenreTemplate")
    public ModelAndView showeditGenreTempateView(ModelAndView mv, @RequestParam("id") Integer id, HttpSession session) {
        // 編集するテンプレートを特定
        GenreTemplates editTemplate = (GenreTemplates) templateService.findTemplateById(id, ElementType.Genre);
        if (editTemplate == null) {
            System.out.println("null");
            // エラー画面に遷移
        }

        session.setAttribute("editGenreTemplateId", id);
        mv.addObject("editTemplate", editTemplate);
        mv.addObject("type", TYPE_GENRE);
        mv.setViewName("templateEdit");
        return mv;
    }

    /* テンプレート編集画面リダイレクト遷移（ターゲット） */
    @GetMapping("/editTargetTemplate")
    public ModelAndView showEditTargetTempateViewRedirect(ModelAndView mv, HttpSession session) {
        // 編集するテンプレートを特定
        Integer id = (Integer) session.getAttribute("editTargetTemplateId");
        TargetTemplates editTemplate = (TargetTemplates) templateService.findTemplateById(id, ElementType.Target);

        if (editTemplate == null) {
            System.out.println("null");
            // エラー画面に遷移
        }

        session.setAttribute("editTargetTemplateId", id);
        mv.addObject("editTemplate", editTemplate);
        mv.addObject("type", TYPE_TARGET);
        mv.setViewName("templateEdit");
        return mv;
    }

    /* テンプレート編集画面リダイレクト遷移（ジャンル） */
    @GetMapping("/editGenreTemplate")
    public ModelAndView showEditGenreTempateViewRedirect(ModelAndView mv, HttpSession session) {
        // 編集するテンプレートを特定
        Integer id = (Integer) session.getAttribute("editGenreTemplateId");
        GenreTemplates editTemplate = (GenreTemplates) templateService.findTemplateById(id, ElementType.Genre);

        if (editTemplate == null) {
            System.out.println("null");
            // エラー画面に遷移
        }

        session.setAttribute("editGenreTemplateId", id);
        mv.addObject("editTemplate", editTemplate);
        mv.addObject("type", TYPE_GENRE);
        mv.setViewName("templateEdit");
        return mv;
    }

    /* 設定画面遷移 */
    @GetMapping("/setting")
    public ModelAndView showSettingView(ModelAndView mv) {
        mv.setViewName("setting");
        return mv;
    }

    // ↓編集画面----------------------------------------------------------------------------------------------------------------------------

    /* ターゲットコンテンツ一括削除処理 */
    @PostMapping("/deleteTargetContents")
    public String deleteTargetContents(@RequestParam("deleteContentId") List<Integer> deleteIdList,
            @RequestParam("editTemplateId") Integer editTemplateId, HttpSession session) {
        List<Templates> targetTemplates = (List<Templates>) session.getAttribute("targetTemplates");

        templateService.bulkDeleteContents(targetTemplates, editTemplateId, deleteIdList, ElementType.Target);

        return "redirect:/editTargetTemplate";
    }

    /* ジャンルコンテンツ一括削除処理 */
    @PostMapping("/deleteGenreContents")
    public String deleteGenreContents(@RequestParam("deleteContentId") List<Integer> deleteIdList,
            @RequestParam("editTemplateId") Integer editTemplateId, HttpSession session) {
        List<Templates> genreTemplates = (List<Templates>) session.getAttribute("genreTemplates");

        templateService.bulkDeleteContents(genreTemplates, editTemplateId, deleteIdList, ElementType.Genre);

        return "redirect:/editGenreTemplate";
    }

    /* ターゲット追加処理 (編集画面) */
    @PostMapping("/addtarget-edit")
    public String addTargetFromEditView(@RequestParam("newTarget") String newTarget,
            @RequestParam("editTemplateId") Integer editTemplateId, HttpSession session) {
        List<Templates> targetTemplates = (List<Templates>) session.getAttribute("targetTemplates");
        Templates editTemplate = templateService.getTemplateById(targetTemplates, editTemplateId);
        templateService.addNewContent(editTemplate, newTarget, ElementType.Target);

        return "redirect:/editTargetTemplate";
    }

    /* ジャンル追加処理 (編集画面) */
    @PostMapping("/addgenre-edit")
    public String addGenreFromEditView(@RequestParam("newGenre") String newGenre,
            @RequestParam("editTemplateId") Integer editTemplateId, HttpSession session) {
        List<Templates> genreTemplates = (List<Templates>) session.getAttribute("genreTemplates");
        Templates editTemplate = templateService.getTemplateById(genreTemplates, editTemplateId);
        templateService.addNewContent(editTemplate, newGenre, ElementType.Genre);

        return "redirect:/editGenreTemplate";
    }

    // ↓テンプレート追加画面----------------------------------------------------------------------------------------------------------------------------
    @PostMapping("/saveTemplate")
    public String saveTemplate(
            @RequestParam("templateName") String templateName,
            @RequestParam("templateContent") List<String> templateContents,
            @RequestParam("templateType") String templateType,
            HttpSession session) {
        
        Users loginUser = (Users) session.getAttribute("loginUser");
        System.out.println(templateType);
        if (TYPE_GENRE.equals(templateType)) {
            templateService.addNewTemplate(templateName, templateContents, loginUser.getId(), ElementType.Genre);
        } else if (TYPE_TARGET.equals(templateType)) {
            templateService.addNewTemplate(templateName, templateContents, loginUser.getId(), ElementType.Target);
        } else {
            System.out.println("エラーです");
        }
        
        return "redirect:/setting";
    }
}
