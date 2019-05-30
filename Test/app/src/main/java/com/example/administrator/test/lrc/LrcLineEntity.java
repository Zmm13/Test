package com.example.administrator.test.lrc;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by zmm
 * Time 2019/5/30
 * PackageName com.example.administrator.test.lrc
 */
public class LrcLineEntity {
    // 提取时间正则
    private static final Pattern TIME_PATTERN = Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2})\\]");
    // 提取内容正则
    private static final Pattern CONTENT_PATTERN = Pattern.compile("\\[.*](.*)");

    // 该句歌词内容
    public String content;
    // 该句歌词开始播放的时间
    public long startPosition;

    public LrcLineEntity(String content, long startPosition) {
        this.content = content;
        this.startPosition = startPosition;
    }

    /**
     * 解析单行歌词
     *
     * @param lrcLine exam:[01:56.86][00:24.91]女：别离没有对错　要走也解释不多
     * @return
     */
    @NonNull
    public static List<LrcLineEntity> parseLine(@NonNull String lrcLine) {
        List<LrcLineEntity> result = new ArrayList<>();

        // 对内容进行提取
        Matcher contentMatcher = CONTENT_PATTERN.matcher(lrcLine);
        String content = "";
        if (contentMatcher.find()) {
            content = contentMatcher.group(1);
        }

        // 对多段时间进行提取
        Matcher timeMatcher = TIME_PATTERN.matcher(lrcLine);
        while (timeMatcher.find()) {
            String timeText = timeMatcher.group(1);
            result.add(new LrcLineEntity(content, parseTimeText(timeText)));
        }

        return result;
    }

    /**
     * 解析时间内容，转换为毫秒（相对时间）
     *
     * @param timeText exam:01:56.86-》(1*100*60*60+56*100*60+86)*10
     * @return
     */
    private static long parseTimeText(@NonNull String timeText) {
        String[] numbers = timeText.split("[^\\d]");
        long hour = 0;
        long minutes = 0;
        long seconds = 0;
        long ms = 0;

        try {
            // 从后往前解析，某些值不存在会出现越界错误，最后得到0不影响计算
            ms = Long.parseLong(numbers[numbers.length - 1]);
            seconds = Long.parseLong(numbers[numbers.length - 2]);
            minutes = Long.parseLong(numbers[numbers.length - 3]);
            hour = Long.parseLong(numbers[numbers.length - 4]);
        } catch (Exception ignored) {

        }

        return (hour * 100 * 60 * 60 + minutes * 100 * 60 + seconds * 100 + ms) * 10;
    }
}
