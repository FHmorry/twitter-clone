package com.example.twitterclone.controller;

import com.example.twitterclone.dto.UserRequestDTO;
import com.example.twitterclone.dto.UserResponseDTO;
import com.example.twitterclone.model.User;
import com.example.twitterclone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ユーザー登録を処理するコントローラークラス
 */
@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    /**
     * ユーザー登録のエンドポイント
     * @param userRequest 登録するユーザー情報
     * @return 登録結果のResponseEntity
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO userRequest) {
        try {
            // DTOからエンティティへの変換
            User user = new User();
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            user.setPassword(userRequest.getPassword());

            // ユーザー登録処理を実行
            User registeredUser = userService.registerUser(user);

            // DTOへの変換
            UserResponseDTO responseDTO = UserResponseDTO.fromEntity(registeredUser);

            // 登録成功時、DTOを返す
            return ResponseEntity.ok(responseDTO);
        } catch (DataIntegrityViolationException e) {
            // ユーザー名の重複などのデータ整合性違反が発生した場合、
            // コンフリクトステータスとエラーメッセージを返す
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
