package com.tonkatsuudon.quizsniper.dao;


import com.tonkatsuudon.quizsniper.entity.Users;

public interface UsersDao {
    Users findUserByIdAndPassword(Users users);
}
