package com.car.account.web.common.utils;
public class Constants {

    /**
     * 国家
     */
    public static final String COUNTRY="1";

    //图片限制大小2M
    public static final Long PERSON_IMAGE_SIZE_LIMIT = 10 * 1024 * 1024L;

    /**
     * 验证码(邮箱/短信)
     */
    public static final String LOGIN_VERIFICATION_CODE_CACHE_KEY = "yanxin:waic:portal:login:verificationCode:%s";

    /**
     * 校验验证码次数 key 值 accountName_code
     */
    public static final String VERIFICATION_CODE_TIMES_CACHE_KEY = "yanxin:waic:portal:login:verificationCodeTimes:%s_%s";
    /**
     * 用户数据序列号
     */
    public static final String MOBILE_SERIAL_CACHE_KEY = "yanxin:waic:portal:mobile:serial:%s";

    /**
     * 用户数据序列号总数
     */
    public static final String MOBILE_SERIAL_TOTAL_CACHE_KEY = "yanxin:waic:portal:mobile:serialTotal";

    /**
     * 公众号缓存token对象
     */
    public static final String WECHAT_ACCESS_TOKEN_CACHE_KEY = "yanxin:waic:portal:weChat:token";

    /**
     * 公众号缓存jsTicket对象
     */
    public static final String WECHAT_JS_TICKET_CACHE_KEY = "yanxin:waic:portal:weChat:jsTicket";

    /**
     * 注册验证码(邮箱/短信)
     */
    public static final String REGISTER_VERIFICATION_CODE_CACHE_KEY = "yanxin:waic:portal:register:verificationCode:%s";

    /**
     * 验证码有效期时间10分钟
     */
    public static final Integer LOGIN_VERIFICATION_CODE_TIME = 5;

    /**
     * 微信获取openid地址
     */
    public static final String WE_CHAT_GET_OPENID = "https://api.weixin.qq.com/sns/jscode2session?appid=";

    /**
     * 用户注册金额
     */
    public static final String REGISTER_MONEY_CODE = "register_money";

    /**
     * 嘉宾注册订单名称
     */
    public static final String REGISTER_ORDER_NAME = "2020WAIC 注册";

    /**
     * 短链接无效提示
     */
    public static final String SHORT_LINK_INVALID = "无效的短链接地址";

    /**
     * 个人注册数据sts= 1的判断注册是否重复的时效性
     */
    public static final Integer PERSONAL_PRESCRIPTION = 120;

    /**
     * 特色活动
     */
    public static final String CHARACTERISTIC_ACTIVITY="42";

    /**
     * 主题论坛/行业论坛/特色活动嘉宾数据ID
     */
    public static final String unifyGuestTypeUuid = "64";

    /**
     * 评论redis的key
     */
    public static final String LEAVE_MSG_PERSON_CODE_CACHE_KEY = "yanxin:waic:portal:leaveMsg:personUuid:%s";

    /**
     * 华为敏感词校验缓存token
     */
    public static final String COMMENT_HW_TOKEN_CODE="yanxin:waic:portal:comment:hw:token";

    /**
     * 敏感词token超时时间
     */
    public static final long COMMENT_HW_TOKEN_TIME= 60 * 60 * 60 * 24;

    /**
     * 留资redis的key
     */
    public static final String COMMENT_PERSON_CODE_CACHE_KEY = "yanxin:waic:portal:comment:personUuid:%s";
    /**
     * 企业票据
     */
    public static final String COMPANY_TICKET_KEY = "yanxin:waic:portal:ticket:%s";
    /**
     * 企业令牌
     */
    public static final String COMPANY_TOKEN_KEY = "yanxin:waic:portal:token:%s";


    /**
     * 点赞key %s_%s personUUid_dataUuid
     */
    public static final String PUSH_LIKE_KEY = "yanxin:waic:portal:push_like:%s_%s";

    /**
     * 评论
     */
    public static final String PUSH_COMMENT_KEY = "yanxin:waic:portal:push_comment:%s_%s";

    /**
     * 问卷 key personUUid_businessId
     */
    public static final String QUESTION_KEY = "yanxin:waic:portal:question:%s_%s";

    /**
     * 镇馆之宝key %s_%s personUUid_dataUuid
     */
    public static final String PUSH_TOWN_KEY = "yanxin:waic:portal:push_town:%s_%s";


    public static final String COMMENT_KEY = "yanxin:waic:portal:comment:%s";

    /**
     * 获取企业点赞排行 type_virtual
     */
    public static final String STATIS_COMPANY_RANKING_LIST = "yanxin:waic:portal:ranking_list:%s_%s";


    /**
     * 到处excel 临时key
     */
    public static final String REDIS_EXPORT_TMP_KEY = "yanxin:waic:portal:dict:export_excel_tmp_key:%s";

    /**
     * 地区
     */
    public static final String REDIS_EXPORT_TMP_ADDR_KEY = "yanxin:waic:portal:dict:export_excel_addr_key:%s_%s";

    /**
     * 能量组维度
     */
    public static final String ENERGY_DIMENSION = "energy_dimension";

    /**
     * 视频订单订单名称
     */
    public static final String VIDEO_ORDER_NAME = "WAIC2020观看票";


    /**
     * 能量订单订单名称
     */
    public static final String ENERGY_ORDER_NAME = "WAIC商品购买";

    /**
     * 签到
     */
    public static final String SING_IN_PERSON_CODE_CACHE_KEY = "yanxin:waic:portal:signIn:personUuid:%s";

    /**
     * 活动开始时间
     */
    public static final String ACTIVITY_BEGIN_TIME_UUID = "251";

    /**
     * 镇馆之宝数据ID
     */
    public static final String ZhenGuangZhiBao_UUID = "252";

    /**
     * 短链接前缀url数据ID
     */
    public static final String short_link_dict_uuid = "286";

    /**
     * 分割字符串
     */
    public static final String SPLIT_CONTENT = ",";

    /**
     * 字符前缀
     */
    public static final String WAIC = "waic";

    /**
     * 能量字符前缀
     */
    public static final String energy_order_qz = "NL";

    /**
     * 调查问卷配置文件固定编码
     */
    public static final String questionnaire_code = "questionnaire";

    /**
     * 问卷调查MD5key
     */
    public static final String questionnaire_md5_key = "waic_2020_questionnaire";

    /**
     * redis 内容前缀 method_key
     */
    public static final String contents = "yanxin:waic:portal:content:%s:%s";

    /**
     * setting 检索
     */
    public static final String REDIS_SETTING_KEY = "yanxin:waic:portal:setting:setting_key:%s_%s";

    /**
     * 授权码
     */
    public static final String authorization_code = "authorization_code";

}
