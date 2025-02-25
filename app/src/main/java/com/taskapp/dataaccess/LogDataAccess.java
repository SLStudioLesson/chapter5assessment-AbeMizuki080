package com.taskapp.dataaccess;

import java.io.FileWriter;
import java.io.IOException;
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
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.append(log.getTaskCode() != 0 ? String.valueOf(log.getTaskCode()) : "N/A")  
              .append(",")
              .append(String.valueOf(log.getChangeUserCode()))
              .append(",")
              .append(String.valueOf(log.getStatus()))
              .append(",")
              .append(log.getChangeDate().toString())
              .append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * すべてのログを取得します。
     *
     * @return すべてのログのリスト
     */
    // public List<Log> findAll() {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    /**
     * 指定したタスクコードに該当するログを削除します。
     *
     * @see #findAll()
     * @param taskCode 削除するログのタスクコード
     */
    // public void deleteByTaskCode(int taskCode) {
    //     try () {

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * ログをCSVファイルに書き込むためのフォーマットを作成します。
     *
     * @param log フォーマットを作成するログ
     * @return CSVファイルに書き込むためのフォーマット
     */
    // private String createLine(Log log) {
    // }

}