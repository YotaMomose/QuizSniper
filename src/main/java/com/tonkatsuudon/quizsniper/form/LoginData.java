package com.tonkatsuudon.quizsniper.form;

import com.tonkatsuudon.quizsniper.entity.Users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginData {
    @NotBlank(message = "ユーザーIDを入力して下さい")
    private String userId;

    @NotBlank(message = "パスワードを入力して下さい")
    private String password;

    /**
     * 入力データからuserテーブルのエンティティを生成して返す
     */
    public Users toEntity() {
        Users users = new Users();
        users.setId(userId);
        users.setPassword(password);

        return users;
    }

}
