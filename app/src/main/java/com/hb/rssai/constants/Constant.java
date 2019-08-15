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
    public static final String FLAG_PRESS_SUB_RSS_SOURCE = "press_sub_rss_source|";//订阅
    public static final String FLAG_PRESS_COLLECTION_SOURCE = "press_collection_source|";
    public static final String FLAG_PRESS_URL_SOURCE = "press_url_source|";

    public static final String TIPS_FILTER_SORT = "筛选排序";
    public static final String TOKEN = "token";
    public static final String SP_LOGIN_USER_NAME = "sp_login_user_name";
    public static final String SP_LOGIN_PSD = "sp_login_password";
    public static final String KEY_IS_NO_IMAGE_MODE = "isLoadImage";//是否加载图片
    public static final String KEY_IS_COLD_REBOOT_MODE ="isColdReboot" ;//是否是冷启动模式

    public static final String MSG_NETWORK_ERROR = "网络慢或连接失败，请稍后重试。";
    public static final String FAILED_NETWORK = "网络连接不可用，请稍后重试。";

    public static final int PAGE_SIZE = 20;    //页码尺寸
    public static final int RECOMMEND_PAGE_SIZE = 9;//推荐
    public static final int SUBSCRIBE_PAGE_SIZE = 9;//订阅
    public static final String USER_ID = "user_id";
    public static final int DATA_TYPE_ALL = 0;
    public static final String SAVE_IS_UPDATE = "isupdate";

    public static final String KEY_SYS_NIGHT_MODE = "sys_night_mode";
    public static final String KEY_SYS_NIGHT_MODE_TIME = "sys_night_mode_time";
    public static final String KEY_IS_OFFLINE_MODE = "key_is_offline_mode";//脱机模式

    public static final String LAST_UPDATE_TIME = "last_update_time";
    public static final String TIPS_IMAGE_SELECTOR = "选择封面图片";
    public static final String KEY_MESSAGE_COUNT = "message_count";
    public static final String KEY_GUIDE = "key_guide";
    public static final String TYPE_EMAIL = "email";
    public static final String TYPE_REPRESENTATION = "representation";
    public static final String TIPS_EMAIL = "修改邮箱";
    //上一次的消息总数 当消息列表加载的时候 用于更新 我的界面消息总数显示
    public static final String KEY_MESSAGE_TOTAL_COUNT = "message_total_count";
    public static final String KEY_IS_SHOW_POP = "isShowPop";//是否显示过pop弹窗
    public static final String KEY_THEME = "key_theme";//设置主题保存
    public static final String KEY_REMARK = "key_ad_remark";//一句话
    public static final String KEY_DATA_GROUP = "key_data_group";//本地缓存分类
    public static final String KEY_DATA_GROUP_TIME = "key_data_group_time";//本地缓存分类时间 ms
    public static final String KEY_CACHE_AVATAR = "key_avatar";//头像
    public static final String KEY_CACHE_AVATAR_NAME = "key_avatar_name";//头像名
    public static final String KEY_JAMES = "james";

    public static final long DEFAULT_COLLECTION_TIME =60 * 1000 ;//
    public static final String KEY_IS_OLD_REC_MODE = "key_is_old_rec_mode";

    public static final String KEY_WORD_FILTER ="E789B9E8ADA6,E8ADA6E5AF9F,E4B9A6E8AEB0,E694BFE5BA9C,E5859A,E69D8EE5858BE5BCBA,E694BFE6B2BB,E4B9A0E8BF91E5B9B3,E5B882E995BF,E79C81E995BF,E5B180E995BF,E58EBFE995BF,E69D91E995BF,E5B180E995BF,E7A791E995BF,E4B8BBE4BBBB,E585B1E4BAA7E5859A,E8A7A3E694BEE5869B,E983A8E9989F,E5B88CE789B9E58B92,E5B1A0E69D80,E680A7E788B1,73657879,78766964656F73,706F726E687562,E889B2E68385,E88D89E6A6B4,E5908CE5BF97,E585A5E88289,E680A7E4BAA4,E68FB4E4BAA4,E588B6E69C8D,E4B89DE8A29C,000000,E4BABAE5A6BB,E5B7A8E4B9B3,E9B28D,E585B1E99D92E59BA2,E9BB91E9ACBC,E4B9B1E4BCA6,E7B2BEE6B6B2,E58685E5B084,E6B395E8BDAEE58A9F,706f726e,78766964656f73,736578,73657879,6675636b,6d616b65206c6f7665,6675636b6572,746f6b796f,617373,627261,6e6967676572,6469636b,766167696e61,70656e6973,676179,e88d89e6a6b4,e5908ce5bf97,e998b4e88c8e,e998b4e98193,e5819ae788b1,e68f92e585a5,e6938d,42,e9babbe797b9,e9b28d,e680a7,e585a5e88289,e78b82e5b9b2,e5a5b6,e9aa86e9a9bc,e5908ce680a7e6818b,e5a5b8,e5b084,e4b89ce4baace783ad,e78886e6938d,e9bb91e9acbc,e5b181e882a1,e88780,e5a6b9e5ad90,e889b2" ;
    public static final String TIPS_DELETE_ALL = "确定要删除全部？";


    public static String START_TIME = "1990-01-01 00:00:00";
    public static String MSG_NO_LOGIN = "尚未登录，请登录后操作！";

    public static String ACTION_BD_KEY = "bd_key";
    public static String ACTION_INPUT_LINK = "input_link";
    public static String ACTION_OPEN_OPML = "open_opml";

    public static SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_LONG_PATTERN);
    public static String AlipaysUrl="alipay_url";

    //头像
    public static final String AVATAR_SAVE_DIR = "/GeneralUpdateLib/";
//    public static final String AVATAR_SAVE_NAME = "zr_avatar_img.png";

}
