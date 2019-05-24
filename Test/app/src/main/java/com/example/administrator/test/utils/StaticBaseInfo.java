package com.example.administrator.test.utils;

import android.content.Context;
import android.graphics.Color;

import com.example.administrator.test.R;

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
    public static int color_divider_light = Color.parseColor("#66ffffff");
    public static int color_item_light = Color.parseColor("#08000000");
    public static int color_item_dark = Color.parseColor("#12ffffff");
    public static int box_light = Color.parseColor("#12ffffff");
    public static int box_dark = Color.parseColor("#08000000");

    /**
     * 获取主页item字体普通大小
     * @param context
     * @return
     */
    public static int getItemTextSizeSimple(Context context){
        return (int) Res.getDimen(R.dimen.x10,context);
    }
    /**
     * 获取主页item字体选中大小
     * @param context
     * @return
     */
    public static int getItemTextSizeSelect(Context context){
        return (int) Res.getDimen(R.dimen.x13,context);
    }

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
    public static final String SONG_INFO_LIST = "songInfoList";
    //qq音乐新歌top100
    public static final String QQ_NEW_MUSIC_TOP_100_BASEURL = "https://u.y.qq.com/";
    public static final String QQ_NEW_MUSIC_TOP_100_IMAGE_REPLCAE_TAG = "&albumMid&";
    public static final String QQ_NEW_MUSIC_TOP_100_IMAGE_PATH = "https://y.gtimg.cn/music/photo_new/T002R300x300M000&albumMid&.jpg";
    public static final String QQ_NEW_MUSIC_NEW = "cgi-bin/musicu.fcg?-=getUCGI6156166971860477&g_tk=859231619&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B%22detail%22%3A%7B%22module%22%3A%22musicToplist.ToplistInfoServer%22%2C%22method%22%3A%22GetDetail%22%2C%22param%22%3A%7B%22topId%22%3A27%2C%22offset%22%3A0%2C%22num%22%3A20%2C%22period%22%3A%222019-05-21%22%7D%7D%2C%22comm%22%3A%7B%22ct%22%3A24%2C%22cv%22%3A0%7D%7D";
    public static final String QQ_NEW_MUSIC_COOL = "cgi-bin/musicu.fcg?-=getUCGI7062918289209454&g_tk=1019472139&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B%22detail%22%3A%7B%22module%22%3A%22musicToplist.ToplistInfoServer%22%2C%22method%22%3A%22GetDetail%22%2C%22param%22%3A%7B%22topId%22%3A4%2C%22offset%22%3A0%2C%22num%22%3A20%2C%22period%22%3A%222019-05-23%22%7D%7D%2C%22comm%22%3A%7B%22ct%22%3A24%2C%22cv%22%3A0%7D%7D";
    public static final String QQ_NEW_MUSIC_HOT = "cgi-bin/musicu.fcg?-=getUCGI27588192397517175&g_tk=1019472139&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B\"detail\"%3A%7B\"module\"%3A\"musicToplist.ToplistInfoServer\"%2C\"method\"%3A\"GetDetail\"%2C\"param\"%3A%7B\"topId\"%3A26%2C\"offset\"%3A0%2C\"num\"%3A20%2C\"period\"%3A\"2019_20\"%7D%7D%2C\"comm\"%3A%7B\"ct\"%3A24%2C\"cv\"%3A0%7D%7D";
    public static final String QQ_NEW_MUSIC_NEIDI = "cgi-bin/musicu.fcg?-=getUCGI8720694674101619&g_tk=1019472139&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B\"detail\"%3A%7B\"module\"%3A\"musicToplist.ToplistInfoServer\"%2C\"method\"%3A\"GetDetail\"%2C\"param\"%3A%7B\"topId\"%3A5%2C\"offset\"%3A0%2C\"num\"%3A20%2C\"period\"%3A\"2019_20\"%7D%7D%2C\"comm\"%3A%7B\"ct\"%3A24%2C\"cv\"%3A0%7D%7D";
    public static final String QQ_NEW_MUSIC_OUMEI = "cgi-bin/musicu.fcg?-=getUCGI9932813829465561&g_tk=1019472139&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B\"detail\"%3A%7B\"module\"%3A\"musicToplist.ToplistInfoServer\"%2C\"method\"%3A\"GetDetail\"%2C\"param\"%3A%7B\"topId\"%3A3%2C\"offset\"%3A0%2C\"num\"%3A20%2C\"period\"%3A\"2019_20\"%7D%7D%2C\"comm\"%3A%7B\"ct\"%3A24%2C\"cv\"%3A0%7D%7D";

    public static final String QQ_MUSIC_GE_DAN_SELECT_BY_ITEM_ID="cgi-bin/musicu.fcg?-=recom5243062888560728&g_tk=1019472139&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0";
    public static final String QQ_MUSIC_GE_DAN_SELECT_BY_ITEM_ID_REPLACE="$ITEM_ID$";
    public static final String QQ_MUSIC_GE_DAN_SELECT_BY_ITEM_ID_DATA="%7B%22comm%22%3A%7B%22ct%22%3A24%7D%2C%22playlist%22%3A%7B%22method%22%3A%22get_playlist_by_category%22%2C%22param%22%3A%7B%22id%22%3A$ITEM_ID$%2C%22curPage%22%3A1%2C%22size%22%3A40%2C%22order%22%3A5%2C%22titleid%22%3A3317%7D%2C%22module%22%3A%22playlist.PlayListPlazaServer%22%7D%7D";

    public static final String QQ_NEW_MUSIC_RIBEN = "cgi-bin/musicu.fcg?-=getUCGI21181586901080007&g_tk=1019472139&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B\"detail\"%3A%7B\"module\"%3A\"musicToplist.ToplistInfoServer\"%2C\"method\"%3A\"GetDetail\"%2C\"param\"%3A%7B\"topId\"%3A17%2C\"offset\"%3A0%2C\"num\"%3A20%2C\"period\"%3A\"2019_20\"%7D%7D%2C\"comm\"%3A%7B\"ct\"%3A24%2C\"cv\"%3A0%7D%7D";
//    public static final String QQ_NEW_MUSIC_TOP_100_GET_KEY_ADD = "cgi-bin/musicu.fcg?-=getplaysongvkey7687284379381651&g_tk=859231619&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%229733784294%22%2C%22songmid%22%3A%5B%22&SONGMIDID&%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221359163679%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A1359163679%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D";
    public static final String QQ_NEW_MUSIC_TOP_100_GET_KEY_ADD = "cgi-bin/musicu.fcg?getplaysongvkey";
//    public static final String QQ_NEW_MUSIC_TOP_100_GET_KEY_DATA = "\"req_0\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"9733784294\",\"songmid\":[\"&SONGMIDID&\"],\"songtype\":[0],\"uin\":\"1359163679\",\"loginflag\":1,\"platform\":\"20\"}},\"comm\":{\"uin\":0,\"format\":\"json\",\"ct\":24,\"cv\":0}}\n";
    public static final String QQ_NEW_MUSIC_TOP_100_GET_KEY_DATA = "%7B\"req_0\"%3A%7B\"module\"%3A\"vkey.GetVkeyServer\"%2C\"method\"%3A\"CgiGetVkey\"%2C\"param\"%3A%7B\"guid\"%3A\"9733784294\"%2C\"songmid\"%3A%5B\"&SONGMIDID&\"%5D%2C\"songtype\"%3A%5B0%5D%2C\"uin\"%3A\"1359163679\"%2C\"loginflag\"%3A1%2C\"platform\"%3A\"20\"%7D%7D%2C\"comm\"%3A%7B\"uin\"%3A1359163679%2C\"format\"%3A\"json\"%2C\"ct\"%3A24%2C\"cv\"%3A0%7D%7D";
    public static final String QQ_NEW_MUSIC_TOP_100_GET_KEY_REPLACE_TAG="&SONGMIDID&";
    public static final String QQ_NEW_MUSIC_TOP_100_PLAY="http://dl.stream.qqmusic.qq.com/";
    public static final String QQ_NEW_MUSIC_TOP_100_PLAY_ADD="C400&SONGMIDID&.m4a?guid=9733784294&vkey=&KEY&&uin=4383&fromtag=66";
    public static final String QQ_NEW_MUSIC_TOP_100_PLAY_REPLACE_TAG="&KEY&";

    public static final String QQ_MUSIC_SHOUYE_BASE_URL="https://u.y.qq.com/";
    public static final String QQ_MUSIC_SHOUYE_ADD_URL="cgi-bin/musicu.fcg?-=recom40897571598403193&g_tk=1019472139&loginUin=1359163679&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data=%7B%22comm%22%3A%7B%22ct%22%3A24%7D%2C%22category%22%3A%7B%22method%22%3A%22get_hot_category%22%2C%22param%22%3A%7B%22qq%22%3A%22%22%7D%2C%22module%22%3A%22music.web_category_svr%22%7D%2C%22recomPlaylist%22%3A%7B%22method%22%3A%22get_hot_recommend%22%2C%22param%22%3A%7B%22async%22%3A1%2C%22cmd%22%3A2%7D%2C%22module%22%3A%22playlist.HotRecommendServer%22%7D%2C%22playlist%22%3A%7B%22method%22%3A%22get_playlist_by_category%22%2C%22param%22%3A%7B%22id%22%3A8%2C%22curPage%22%3A1%2C%22size%22%3A40%2C%22order%22%3A5%2C%22titleid%22%3A8%7D%2C%22module%22%3A%22playlist.PlayListPlazaServer%22%7D%2C%22new_song%22%3A%7B%22module%22%3A%22newsong.NewSongServer%22%2C%22method%22%3A%22get_new_song_info%22%2C%22param%22%3A%7B%22type%22%3A5%7D%7D%2C%22new_album%22%3A%7B%22module%22%3A%22newalbum.NewAlbumServer%22%2C%22method%22%3A%22get_new_album_info%22%2C%22param%22%3A%7B%22area%22%3A1%2C%22sin%22%3A0%2C%22num%22%3A10%7D%7D%2C%22new_album_tag%22%3A%7B%22module%22%3A%22newalbum.NewAlbumServer%22%2C%22method%22%3A%22get_new_album_area%22%2C%22param%22%3A%7B%7D%7D%2C%22toplist%22%3A%7B%22module%22%3A%22musicToplist.ToplistInfoServer%22%2C%22method%22%3A%22GetAll%22%2C%22param%22%3A%7B%7D%7D%2C%22focus%22%3A%7B%22module%22%3A%22QQMusic.MusichallServer%22%2C%22method%22%3A%22GetFocus%22%2C%22param%22%3A%7B%7D%7D%7D";
}


