package com.taskapp.dataaccess;

import java.util.List;

import com.taskapp.exception.AppException;
import com.taskapp.model.Task;
import com.taskapp.model.User;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.taskapp.model.*;

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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String taskData = task.getCode() + "," + task.getName() + "," + task.getStatus() + "," + task.getRepUser().getCode();
            writer.write(taskData);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    

    /**
     * コードを基にタスクデータを1件取得します。
     * @param code 取得するタスクのコード
     * @return 取得したタスク
     */
    public Task findByCode(int taskCode) {
        List<Task> tasks = loadTasks(); 
        for (Task task : tasks) {
            if (task.getCode() == taskCode) {
                return task; 
            }
        }
        return null; 
    }

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
    public void delete(int taskCode) throws AppException {
        List<Task> tasks = loadTasks();
        Task taskToDelete = null;
    
        for (Task task : tasks) {
            if (task.getTaskCode() == taskCode) {
                taskToDelete = task;
                break;
            }
        }
    
        if (taskToDelete == null) {
            throw new AppException("タスクが存在しません");
        }
    
        tasks.remove(taskToDelete);
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Task task : tasks) {
                writer.write(task.toCsvFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException("タスクの削除中にエラーが発生しました");
        }
    }
    
    /**
     * タスクデータをCSVに書き込むためのフォーマットを作成します。
     * @param task フォーマットを作成するタスク
     * @return CSVに書き込むためのフォーマット文字列
     */
    // private String createLine(Task task) {
    // }

    private List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                
                int taskCode = Integer.parseInt(fields[0].trim());
                String taskName = fields[1].trim();
                int status = Integer.parseInt(fields[2].trim());
                
                int repUserCode = Integer.parseInt(fields[3].trim());
   
                String repUserName = "担当者名";  
                String repUserEmail = "unknown@example.com";  
                String repUserPassword = "password";  
                
                User assignedUser = new User(repUserCode, repUserName, repUserEmail, repUserPassword);
                
                tasks.add(new Task(taskCode, taskName, status, assignedUser));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
}
}