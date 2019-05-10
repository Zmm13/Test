package com.example.administrator.test.utils;

import java.io.File;

/**
 * Create by zmm
 * Time 2019/5/10
 * PackageName com.example.administrator.test.utils
 */
public class FileTool {
    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isFileExists(String path) {
        if (TextUtil.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
