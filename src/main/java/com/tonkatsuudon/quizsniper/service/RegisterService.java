package com.tonkatsuudon.quizsniper.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tonkatsuudon.quizsniper.dao.QuizElementDao;
import com.tonkatsuudon.quizsniper.entity.GenreTemplates;
import com.tonkatsuudon.quizsniper.entity.TargetTemplates;
import com.tonkatsuudon.quizsniper.entity.Templates;
import com.tonkatsuudon.quizsniper.entity.Users;
import com.tonkatsuudon.quizsniper.repository.GenreRepository;
import com.tonkatsuudon.quizsniper.repository.TargetRepository;
import com.tonkatsuudon.quizsniper.repository.UsersRepository;
import com.tonkatsuudon.quizsniper.type.ElementType;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterService {
    @PersistenceContext
    private EntityManager entityManager;
    private UsersRepository usersRepository;
    private TargetRepository targetRepository;
    private GenreRepository genreRepository;
    private Map<ElementType, QuizElementDao> repositoies;

    @PostConstruct
    public void init() {
        targetRepository = new TargetRepository(entityManager);
        genreRepository = new GenreRepository(entityManager);
        repositoies = new HashMap<>();
        repositoies.put(ElementType.Genre, genreRepository);
        repositoies.put(ElementType.Target, targetRepository);
        usersRepository = new UsersRepository(entityManager);
    }


    /**
     * 既存データとのIDの重複チェック
     * @param Id
     * @return
     */
    public boolean checkDuplicateId(String Id) {
        boolean result = usersRepository.existsId(Id);
        return result;
    }

    @Transactional
    /**
     * ユーザーテーブル・ジャンルテンプレートテーブル・ターゲットテンプレートテーブルに新規登録のデータを登録する
     * @param users 登録するユーザー
     * @param template 登録するテンプレート
     * @param userId 登録するユーザーのID    
     * @param type ジャンルorターゲット
     */
    public void newRegister(Users users, GenreTemplates gTemplates, TargetTemplates tTemplates,String userId) {
        registerUser(users);
        templateInitialSetup(gTemplates, userId, ElementType.Genre);
        templateInitialSetup(tTemplates, userId, ElementType.Target);
    }

    /**
     * ユーザーテーブルに新規会員登録をする
     * @param users 登録するユーザー
     */
    private void registerUser(Users users) {
        usersRepository.registerUser(users);

    }

    /**
     * 新規登録時の初期テンプレート登録
     * @param template 登録するテンプレート
     * @param userId 登録するユーザーのID    
     * @param type ジャンルorターゲット
     * */
    private void templateInitialSetup(Templates templates, String userId, ElementType type) {
        
        QuizElementDao repository = repositoies.get(type);
        repository.templateInitialSetup(templates, userId);
        
    }
}
