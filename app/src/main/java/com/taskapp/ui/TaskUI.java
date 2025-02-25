package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.taskapp.exception.AppException;
import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.model.User;

public class TaskUI {
    private final BufferedReader reader;
    private final UserLogic userLogic;
    private final TaskLogic taskLogic;
    private User loginUser;

    public TaskUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        userLogic = new UserLogic();
        taskLogic = new TaskLogic();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param reader
     * @param userLogic
     * @param taskLogic
     */
    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) {
        this.reader = reader;
        this.userLogic = userLogic;
        this.taskLogic = taskLogic;
    }

    /**
     * メニューを表示し、ユーザーの入力に基づいてアクションを実行します。
     *
     * @see #inputLogin()
     * @see com.taskapp.logic.TaskLogic#showAll(User)
     * @see #selectSubMenu()
     * @see #inputNewInformation()
     */
    public void displayMenu() {
        System.out.println("タスク管理アプリケーションにようこそ!!");

        inputLogin();

        // メインメニュー
        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                System.out.println();

                switch (selectMenu) {
                    case "1":
                        taskLogic.showAll(loginUser);
                        break;
                    case "2":
                        inputNewInformation();
                        break;
                    case "3":
                        System.out.println("ログアウトしました。");
                        flg = false;
                        break;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのログイン情報を受け取り、ログイン処理を行います。
     *設問1 設問2
     * @see com.taskapp.logic.UserLogic#login(String, String)
     */
    public void inputLogin() {
        boolean flg = false;
        while (!flg) {
            try {
                System.out.print("メールアドレスを入力してください：");
                String email = reader.readLine();

                System.out.print("パスワードを入力してください：");
                String password = reader.readLine();

                loginUser = userLogic.login(email, password);

                flg = true;
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * ユーザーからの新規タスク情報を受け取り、新規タスクを登録します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#save(int, String, int, User)
     */
    public void inputNewInformation() {
    try {
        int taskCode = -1;
        String taskName = "";
        int userCode = -1;

        while (true) {
            System.out.print("タスクコードを入力してください：");
            String inputCode = reader.readLine();
            if (isNumeric(inputCode)) {
                taskCode = Integer.parseInt(inputCode);
                break;
            } else {
                System.out.println("コードは半角の数字で入力してください");
            }
        }

        while (true) {
            System.out.print("タスク名を入力してください：");
            taskName = reader.readLine();
            if (taskName.length() <= 10) {
                break;
            } else {
                System.out.println("タスク名は10文字以内で入力してください");
            }
        }

        while (true) {
            System.out.print("担当するユーザーのコードを選択してください：");
            String inputUserCode = reader.readLine();
            if (isNumeric(inputUserCode)) {
                userCode = Integer.parseInt(inputUserCode);
                
                boolean userExists = false;
                BufferedReader userReader = new BufferedReader(new FileReader("users.csv"));
                String line;

                while ((line = userReader.readLine()) != null) {
                    String[] userFields = line.split(",");
                    int code = Integer.parseInt(userFields[0]);
                    if (code == userCode) {
                        userExists = true;
                        break;
                    }
                }
                userReader.close();

                if (!userExists) {
                    System.out.println("存在するユーザーコードを入力してください");
                } else {
                    break;  
                }
            } else {
                System.out.println("ユーザーのコードは半角の数字で入力してください");
            }
        }

        taskLogic.save(taskCode, taskName, userCode, loginUser);

        System.out.println(taskName + "の登録が完了しました。");

    } catch (IOException | AppException e) {
        e.printStackTrace();
    }
}


    /**
     * タスクのステータス変更または削除を選択するサブメニューを表示します。
     *
     * @see #inputChangeInformation()
     * @see #inputDeleteInformation()
     */
    // public void selectSubMenu() {
    // }

    /**
     * ユーザーからのタスクステータス変更情報を受け取り、タスクのステータスを変更します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#changeStatus(int, int, User)
     */
    // public void inputChangeInformation() {
    // }

    /**
     * ユーザーからのタスク削除情報を受け取り、タスクを削除します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#delete(int)
     */
    // public void inputDeleteInformation() {
    // }

    /**
     * 指定された文字列が数値であるかどうかを判定します。
     * 負の数は判定対象外とする。
     *
     * @param inputText 判定する文字列
     * @return 数値であればtrue、そうでなければfalse
     */
    public boolean isNumeric(String inputText) {
        try {
            Integer.parseInt(inputText);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}