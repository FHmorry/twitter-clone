package com.example.twitterclone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.twitterclone.model.User;
import com.example.twitterclone.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * ユーザー登録を処理するコントローラークラス
 */
@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    /**
     * ユーザー登録のエンドポイント
     * @param user 登録するユーザー情報
     * @return 登録結果のResponseEntity
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // ユーザー登録処理を実行
            User registeredUser = userService.registerUser(user);
            // 登録成功時、登録されたユーザー情報を返す
            return ResponseEntity.ok(registeredUser);
        } catch (DataIntegrityViolationException e) {
            // ユーザー名の重複などのデータ整合性違反が発生した場合、
            // コンフリクトステータスとエラーメッセージを返す
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
