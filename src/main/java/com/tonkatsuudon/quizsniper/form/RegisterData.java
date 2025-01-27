package com.tonkatsuudon.quizsniper.form;

import com.tonkatsuudon.quizsniper.entity.Users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterData {
    @NotBlank(message = "ユーザーIDを入力して下さい")
    @Pattern(
        regexp = "^[a-zA-Z0-9]{5,20}$",
        message = "IDは半角英数字で5～20文字にしてください。"
    )
    private String userId;

    @NotBlank(message = "パスワードを入力して下さい")
    @Pattern(
        regexp = "^[a-zA-Z0-9]{5,20}$",
        message = "パスワードは半角英数字で5～20文字にしてください。"
    )
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
