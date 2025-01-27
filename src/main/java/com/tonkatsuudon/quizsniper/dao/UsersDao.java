package com.tonkatsuudon.quizsniper.dao;


import com.tonkatsuudon.quizsniper.entity.Users;

public interface UsersDao {
    Users findUserByIdAndPassword(Users users);

    public boolean existsId(String Id);

    public void registerUser(Users users);
}
