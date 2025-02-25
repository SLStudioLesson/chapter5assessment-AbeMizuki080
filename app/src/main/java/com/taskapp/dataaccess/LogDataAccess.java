package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.taskapp.exception.AppException;
import com.taskapp.model.Log;

public class LogDataAccess {
    private final String filePath;


    public LogDataAccess() {
        filePath = "app/src/main/resources/logs.csv";
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     */
    public LogDataAccess(String filePath) {
        this.filePath = filePath;
    }

    /**
     * ログをCSVファイルに保存します。
     *
     * @param log 保存するログ
     */
    public void save(Log log) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String logData = log.getTaskCode() + "," + log.getChangeUserCode() + "," + log.getStatus() + "," + log.getChangeDate();
            writer.write(logData);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


    /**
     * すべてのログを取得します。
     *
     * @return すべてのログのリスト
     */
   public List<Log> findAll() throws AppException {
    List<Log> logs = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length == 4) {
                int taskCode = Integer.parseInt(fields[0].trim());
                int changeUserCode = Integer.parseInt(fields[1].trim());
                int status = Integer.parseInt(fields[2].trim());
                LocalDate changeDate = LocalDate.parse(fields[3].trim());
                Log log = new Log(taskCode, changeUserCode, status, changeDate);
                logs.add(log);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        throw new AppException("ログ読み込み中にエラーが発生しました");
    }
    return logs;
}

    /**
     * 指定したタスクコードに該当するログを削除します。
     *
     * @see #findAll()
     * @param taskCode 削除するログのタスクコード
     */
public void deleteByTaskCode(int taskCode) throws AppException {
        List<Log> logs = findAll();
        logs.removeIf(log -> log.getTaskCode() == taskCode);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Log log : logs) {
                writer.write(log.toCsvFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException("ログ削除中にエラーが発生しました");
        }
    }

    /**
     * ログをCSVファイルに書き込むためのフォーマットを作成します。
     *
     * @param log フォーマットを作成するログ
     * @return CSVファイルに書き込むためのフォーマット
     */
    // private String createLine(Log log) {
    // }

}