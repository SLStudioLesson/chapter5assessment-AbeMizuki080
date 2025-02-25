package com.taskapp.dataaccess;

import com.taskapp.model.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class UserDataAccess {
    private final String filePath;

    public UserDataAccess() {
        filePath = "app/src/main/resources/users.csv";
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     */
    public UserDataAccess(String filePath) {
        this.filePath = filePath;
    }

    /**
     * メールアドレスとパスワードを基にユーザーデータを探します。
     * @param email メールアドレス
     * @param password パスワード
     * @return 見つかったユーザー
     */
    public User findByEmailAndPassword(String email, String password) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;

        while ((line = br.readLine()) != null) {
            
            String[] data = line.split(",");
            if (data.length == 4) {
                String userEmail = data[2];  
                String userPassword = data[3];  
                String userName = data[1];  

                if (userEmail.equals(email) && userPassword.equals(password)) {
                    return new User(Integer.parseInt(data[0]), userName, userEmail, userPassword);
                }
            }
        }
    } catch (IOException e) {
        System.err.println("ファイル読み込み中にエラーが発生しました: " + filePath);
        e.printStackTrace();
    } catch (NumberFormatException e) {
        System.err.println("ユーザーコードの形式が不正です。データの形式を確認してください。");
        e.printStackTrace();
    }
    return null;
}

    /**
     * コードを基にユーザーデータを取得します。
     * @param code 取得するユーザーのコード
     * @return 見つかったユーザー
     */
    public User findByCode(int code) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            br.readLine(); 

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    try {
                        int userCode = Integer.parseInt(data[0]);
                        if (userCode == code) {
                            String userName = data[1];
                            String userEmail = data[2];
                            String userPassword = data[3];
                            return new User(userCode, userName, userEmail, userPassword);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("ユーザーコードの形式が不正です。コード: " + data[0]);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("ファイル読み込み中にエラーが発生しました: " + filePath);
            e.printStackTrace();
        }
        return null;
    }
}
