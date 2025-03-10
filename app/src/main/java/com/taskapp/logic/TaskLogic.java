package com.taskapp.logic;

import com.taskapp.dataaccess.LogDataAccess;
import com.taskapp.dataaccess.TaskDataAccess;
import com.taskapp.dataaccess.UserDataAccess;
import com.taskapp.exception.AppException;
import com.taskapp.model.Log;
import com.taskapp.model.Task;
import com.taskapp.model.User;
import com.taskapp.logic.*;

import java.time.LocalDate;
import java.util.List;

public class TaskLogic {
    private final TaskDataAccess taskDataAccess;
    private final LogDataAccess logDataAccess;
    private final UserDataAccess userDataAccess;


    public TaskLogic() {
        taskDataAccess = new TaskDataAccess();
        logDataAccess = new LogDataAccess();
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param taskDataAccess
     * @param logDataAccess
     * @param userDataAccess
     */
    public TaskLogic(TaskDataAccess taskDataAccess, LogDataAccess logDataAccess, UserDataAccess userDataAccess) {
        this.taskDataAccess = taskDataAccess;
        this.logDataAccess = logDataAccess;
        this.userDataAccess = userDataAccess;
    }

    /**
     * 全てのタスクを表示します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findAll()
     * @param loginUser ログインユーザー
     */
    // 設問2
    public void showAll(User loginUser) {
        List<Task> tasks = taskDataAccess.findAll();

        int index = 1;
        for(Task task :tasks){
            String status = "";
            if (task.getStatus() == 0) {
                status = "未着手";
            }else if (task.getStatus() == 1) {
                status = "着手中";
            }else if (task.getStatus() == 2) {
                status = "完了";
            }
            
            User assignedUser = task.getRepUser();
            String assignedUserName = (assignedUser != null) ? assignedUser.getName() : "担当者不明";

            String assignedTo = (loginUser.getCode() == (assignedUser != null ? assignedUser.getCode() : -1)) ?
            "あなたが担当しています" : assignedUserName + "が担当しています";

            System.out.println(index++ + ". タスク名：" + task.getName() + ", 担当者名：" + assignedTo + ", ステータス：" + status);
    
        }
    }

    /**
     * 新しいタスクを保存します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#save(com.taskapp.model.Task)
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.model.Log)
     * @param code タスクコード
     * @param name タスク名
     * @param repUserCode 担当ユーザーコード
     * @param loginUser ログインユーザー
     * @throws AppException ユーザーコードが存在しない場合にスローされます
     */
    
     public void save(int code, String name, int repUserCode, User loginUser) throws AppException {
       
        User assignedUser = userDataAccess.findByCode(repUserCode);
        if (assignedUser == null) {
            throw new AppException("存在するユーザーコードを入力してください");
        }

        Task task = new Task(code, name, 0, assignedUser);
        taskDataAccess.save(task);  

        LocalDate changeDate = LocalDate.now();
        Log log = new Log(code, loginUser.getCode(), 0, changeDate);
        logDataAccess.save(log);  
    }
    

    /**
     * タスクのステータスを変更します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#update(com.taskapp.model.Task)
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.model.Log)
     * @param code タスクコード
     * @param status 新しいステータス
     * @param loginUser ログインユーザー
     * @throws AppException タスクコードが存在しない、またはステータスが前のステータスより1つ先でない場合にスローされます
     */
    // public void changeStatus(int code, int status,
    //                         User loginUser) throws AppException {
    // }

    /**
     * タスクを削除します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#delete(int)
     * @see com.taskapp.dataaccess.LogDataAccess#deleteByTaskCode(int)
     * @param code タスクコード
     * @throws AppException タスクコードが存在しない、またはタスクのステータスが完了でない場合にスローされます
     */
    public void delete(int taskCode) throws AppException {
        Task task = taskDataAccess.findByCode(taskCode);
        if (task == null) {
            throw new AppException("存在するタスクコードを入力してください");
        }

        taskDataAccess.delete(taskCode);

        logDataAccess.deleteByTaskCode(taskCode);
    }
}