package com.tonkatsuudon.quizsniper.service;

import org.springframework.stereotype.Service;

import com.tonkatsuudon.quizsniper.entity.Users;
import com.tonkatsuudon.quizsniper.repository.UsersRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
    @PersistenceContext
    private EntityManager entityManager;
    private UsersRepository usersRepository;
    
    @PostConstruct
    public void init() {
        usersRepository = new UsersRepository(entityManager);
    }

    public Users fetchLoginUserData(Users users) {
        Users loginUser = usersRepository.findUserByIdAndPassword(users);
        return loginUser;
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

    /**
     * ユーザーテーブルに新規会員登録をする
     * @param users 登録するユーザー
     */
    @Transactional
    public void registerUser(Users users) {
        usersRepository.registerUser(users);

    }
}
