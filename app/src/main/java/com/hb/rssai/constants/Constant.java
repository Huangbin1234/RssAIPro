package com.hb.rssai.constants;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/4/25.
 */

public class Constant {
    public static final String KEY_JSON_PARAMS = "jsonParams";
    public static final String DATE_LONG_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SHORT_PATTERN = "yyyy-MM-dd";
    public static final String TIPS_SYSTEM = "系统提示";
    public static final String TIPS_FILTER = "筛选分类";
    public static final String TIPS_NICK_NAME = "修改昵称";
    public static final String KEY_DATA_FROM = "dataFrom";//0sys1me

    public static final String FLAG_RSS_SOURCE = "rss_source|";
    public static final String FLAG_COLLECTION_SOURCE = "collection_source|";
    public static final String FLAG_URL_SOURCE = "url_source|";

    public static final String FLAG_PRESS_RSS_SOURCE = "press_rss_source|";
    public static final String FLAG_PRESS_COLLECTION_SOURCE = "press_collection_source|";
    public static final String FLAG_PRESS_URL_SOURCE = "press_url_source|";

    public static final String TIPS_FILTER_SORT = "筛选排序";
    public static final String TOKEN = "token";
    public static final String SP_LOGIN_USER_NAME = "sp_login_user_name";
    public static final String SP_LOGIN_PSD = "sp_login_password";
    public static final String KEY_IS_LOAD_IMAGE = "isLoadImage";//是否加载图片

    public static final String MSG_NETWORK_ERROR = "网络慢或连接失败，请稍后重试。";
    public static final String FAILED_NETWORK = "网络连接不可用，请稍后重试。";

    public static final int PAGE_SIZE = 20;    //页码尺寸
    public static final int RECOMMEND_PAGE_SIZE = 6;//推荐
    public static final int SUBSCRIBE_PAGE_SIZE = 6;//订阅
    public static final String USER_ID = "user_id";
    public static final int DATA_TYPE_ALL = 0;
    public static final String SAVE_IS_UPDATE = "isupdate";

    public static final String KEY_SYS_NIGHT_MODE = "sys_night_mode";
    public static final String KEY_SYS_NIGHT_MODE_TIME = "sys_night_mode_time";

    public static final String LAST_UPDATE_TIME = "last_update_time";
    public static final String TIPS_IMAGE_SELECTOR = "选择封面图片";
    public static final String KEY_MESSAGE_COUNT = "message_count";
    public static final String KEY_GUIDE = "key_guide";
    public static final String TYPE_EMAIL = "email";
    public static final String TYPE_REPRESENTATION = "representation";
    public static final String TIPS_EMAIL = "修改邮箱";


    public static String START_TIME = "1990-01-01 00:00:00";
    public static String MSG_NO_LOGIN = "尚未登录，请登录后操作！";

    public static String ACTION_BD_KEY = "bd_key";
    public static String ACTION_INPUT_LINK = "input_link";
    public static String ACTION_OPEN_OPML = "open_opml";

    public static SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_LONG_PATTERN);
}
