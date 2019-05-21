package com.example.administrator.test.utils;

import android.content.Context;
import android.graphics.Color;

import retrofit2.http.PUT;

/**
 * Create by zmm
 * Time 2019/5/5
 * PackageName com.example.administrator.test.utils
 */
public class StaticBaseInfo {
    public static int baseColor_light = Color.parseColor("#ffffff");
    public static int baseColor_dark = Color.parseColor("#000000");
    public static int color_divider_dark = Color.parseColor("#cacaca");
    public static int color_divider_light = Color.parseColor("#33ffffff");
    public static int color_item_light = Color.parseColor("#09000000");
    public static int color_item_dark = Color.parseColor("#11ffffff");

    public static boolean isLight(Context context) {
        return SpTool.getBoolean(context, SP_NAME, IS_LIGHT);
    }

    public static void fanIsLight(Context context) {
        SpTool.saveBoolean(context, SP_NAME, IS_LIGHT, !isLight(context));
    }

    public static final int MAINACTIVITY_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    public static final int MAINACTIVITY_REQUEST_CODE_READ_EXTERNAL_STORAGE = 2;

    public static final String SP_NAME = "SP_NAME";
    public static final String SP_INIT_MUSIC = "SP_INIT_MUSIC";
    public static final String SP_PLAY_SONG = "SP_PLAY_SONG";
    public static final String IS_LIGHT = "IS_LIGHT";


    public static int baseColor = Color.parseColor("#000000");

    public static int getBaseColor2() {
        return baseColor;
    }

    public static void setBaseColor(int baseColor) {
        StaticBaseInfo.baseColor = baseColor;
    }

    //    网络请求标识
    public static final String CODE_SUCCESS_NAME = "code";
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FAILD = 1;
    public static final String SONG_LIST_NAME = "song";
    public static final String SONG_LIST_DATA_NAME = "data";
    public static final String SONG_LIST_SINGER_NAME = "singer";
    public static final String SONG_LIST_SINGER = "name";
    public static final String SONG_DETAIL = "detail";
    //qq音乐新歌top100
    public static final String QQ_NEW_MUSIC_TOP_100_BASEURL = "https://u.y.qq.com/";
    public static final String QQ_NEW_MUSIC_TOP_100_IMAGE_REPLCAE_TAG = "&albumMid&";
    public static final String QQ_NEW_MUSIC_TOP_100_IMAGE_PATH = "https://y.gtimg.cn/music/photo_new/T002R300x300M000&albumMid&.jpg";
    public static final String QQ_NEW_MUSIC_TOP_100_GET_ADD = "cgi-bin/musicu.fcg?-=getUCGI6156166971860477&g_tk=859231619&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B%22detail%22%3A%7B%22module%22%3A%22musicToplist.ToplistInfoServer%22%2C%22method%22%3A%22GetDetail%22%2C%22param%22%3A%7B%22topId%22%3A27%2C%22offset%22%3A0%2C%22num%22%3A20%2C%22period%22%3A%222019-05-21%22%7D%7D%2C%22comm%22%3A%7B%22ct%22%3A24%2C%22cv%22%3A0%7D%7D";
//    public static final String QQ_NEW_MUSIC_TOP_100_GET_KEY_ADD = "cgi-bin/musicu.fcg?-=getplaysongvkey7687284379381651&g_tk=859231619&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%229733784294%22%2C%22songmid%22%3A%5B%22&SONGMIDID&%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221359163679%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A1359163679%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D";
    public static final String QQ_NEW_MUSIC_TOP_100_GET_KEY_ADD = "cgi-bin/musicu.fcg?";
    public static final String QQ_NEW_MUSIC_TOP_100_GET_KEY_DATA = "%7B%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%229733784294%22%2C%22songmid%22%3A%5B%22&SONGMIDID&%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221359163679%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A1359163679%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D";
    public static final String QQ_NEW_MUSIC_TOP_100_GET_KEY_REPLACE_TAG="&SONGMIDID&";
    public static final String QQ_NEW_MUSIC_TOP_100_PLAY="http://dl.stream.qqmusic.qq.com/";
    public static final String QQ_NEW_MUSIC_TOP_100_PLAY_ADD="C400&SONGMIDID&.m4a?guid=9733784294&vkey=&KEY&&uin=4383&fromtag=66";
    public static final String QQ_NEW_MUSIC_TOP_100_PLAY_REPLACE_TAG="&KEY&";
}


