package com.example.administrator.test.lrc;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Create by zmm
 * Time 2019/5/30
 * PackageName com.example.administrator.test.lrc
 */
public class LrcEntity {
    public List<LrcLineEntity> lrcLines;
    public long totalLength;

    /**
     * 对多行歌词进行解析
     *
     * @param lrcContent 歌词内容
     * @return
     */
    @Nullable
    public static LrcEntity parse(@NonNull String lrcContent) {
        try {
            // 转换成流
            return parseStream(new ByteArrayInputStream(lrcContent.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据输入流进行解析
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    @NonNull
    public static LrcEntity parseStream(@NonNull InputStream inputStream) throws IOException {
        LrcEntity result = new LrcEntity();
        List<LrcLineEntity> lines = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.addAll(LrcLineEntity.parseLine(line));
        }

        reader.close();

        // 所有行解析后对让行根据时间进行排序
        Collections.sort(lines, new Comparator<LrcLineEntity>() {
            @Override
            public int compare(LrcLineEntity o1, LrcLineEntity o2) {
                return (int) (o1.startPosition - o2.startPosition);
            }
        });
        int lineSize = lines.size();

        // 记录歌词总长度
        result.totalLength = lines.get(lineSize - 1).startPosition;
        result.lrcLines = lines;

        return result;

    }

    public int lineCount() {
        return lrcLines.size();
    }

}
