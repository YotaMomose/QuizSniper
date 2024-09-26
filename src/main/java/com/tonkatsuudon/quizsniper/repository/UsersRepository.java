package com.tonkatsuudon.quizsniper.repository;


import com.tonkatsuudon.quizsniper.dao.UsersDao;
import com.tonkatsuudon.quizsniper.entity.Users;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsersRepository implements UsersDao{
    private final EntityManager entityManager;

    /**
     * ログイン時にのユーザー認証に使用
     * @param パスワード・idが設定されたUsersエンティティ
     * @return パスワード・idに紐づくuserテーブルのレコード
     * 
     */
    @Override
    public Users findUserByIdAndPassword(Users users) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Users> query = builder.createQuery(Users.class);
        Root<Users> usersRoot = query.from(Users.class);
        
        Predicate idCondition = builder.equal(usersRoot.get("id"), users.getId());
        Predicate passwordCondition = builder.equal(usersRoot.get("password"), users.getPassword());
        query.where(builder.and(idCondition, passwordCondition));
        try {
            Users result = entityManager.createQuery(query).getSingleResult();
            return result;
        } catch (Exception e) {
            return null;
        }

    }
    
    
} 
