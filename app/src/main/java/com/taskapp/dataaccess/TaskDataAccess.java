package com.taskapp.dataaccess;

import java.util.List;
import com.taskapp.model.Task;
import com.taskapp.model.User;
import com.taskapp.model.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TaskDataAccess {

    private final String filePath;
    private final UserDataAccess userDataAccess;

    public TaskDataAccess() {
        filePath = "app/src/main/resources/tasks.csv";
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     * @param userDataAccess
     */
    public TaskDataAccess(String filePath, UserDataAccess userDataAccess) {
        this.filePath = filePath;
        this.userDataAccess = userDataAccess;
    }

    /**
     * CSVから全てのタスクデータを取得します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @return タスクのリスト
     */
    public List<Task> findAll() {
    List<Task> tasks = new ArrayList<>();
    
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;

        br.readLine(); 
        
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            
            if (data.length == 4) {
                int taskCode = Integer.parseInt(data[0]);
                String taskName = data[1];
                int status = Integer.parseInt(data[2]);
                int repUserCode = Integer.parseInt(data[3]); 
                
                User repUser = userDataAccess.findByCode(repUserCode);
                
                if (repUser == null) {
                    repUser = new User(-1, "担当者不明", "unknown@example.com", "unknown");

                }

                tasks.add(new Task(taskCode, taskName, status, repUser));
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return tasks;
}

    /**
     * タスクをCSVに保存します。
     * @param task 保存するタスク
     */
    public void save(Task task) {
        try (FileWriter writer = new FileWriter("tasks.csv", true)) {
            
            writer.append(String.valueOf(task.getCode())) 
                  .append(",")
                  .append(task.getName())  
                  .append(",")
                  .append(String.valueOf(task.getStatus())) 
                  .append(",")
                  .append(String.valueOf(task.getRepUser().getCode())) 
                  .append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    

    /**
     * コードを基にタスクデータを1件取得します。
     * @param code 取得するタスクのコード
     * @return 取得したタスク
     */
    // public Task findByCode(int code) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    /**
     * タスクデータを更新します。
     * @param updateTask 更新するタスク
     */
    // public void update(Task updateTask) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * コードを基にタスクデータを削除します。
     * @param code 削除するタスクのコード
     */
    // public void delete(int code) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * タスクデータをCSVに書き込むためのフォーマットを作成します。
     * @param task フォーマットを作成するタスク
     * @return CSVに書き込むためのフォーマット文字列
     */
    // private String createLine(Task task) {
    // }
}