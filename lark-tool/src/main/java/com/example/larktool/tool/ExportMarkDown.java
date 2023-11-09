package com.example.larktool.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.larktool.config.ConfigReader;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Real
 * @since 2023/11/01 14:33
 */
public class ExportMarkDown {

    /**
     * 语雀获取markdown模板
     */
    private static final String TEMPLATE = "https://www.yuque.com/%s/%s/%s/markdown?plain=true&linebreak=false&anchor=false";

    /**
     * 当前知识库路径
     */
    private static String SAVE_PATH;

    /**
     * 默认上次更新时间
     */
    private static final String DEFAULT_TIME = "1970-01-01T00_00_00_000";

    /**
     * 先创建一个Random对象
     */
    private static final Random RANDOM = new Random();
    /**
     * 目录名称中不允许出现这些字符
     */
    private static final String[] INVALID_CHARS = {"\"", "/", "\\", ":", "*", "?", "<", ">", "|"};
    private static final String REPLACEMENT_CHAR = "_";

    /**
     * 这个表达式用来找知识库地址响应的html数据中的json数据
     * 报文：{"me":{"PERMISSION":{"create_member":true,"create_book":true,"create_book_collaborator":false,"modify_book_setting":false,"share_book":true,"export_book":true,"share_doc":true,"export_doc":true,"force_delete_doc":false},"organization_login":0,"login":"lihui-8dmqq","work_id":"","email":"","mobile":"175******69","member_level":1,"isPermanentPunished":false,"avatar_url":"https://mdn.alipayobjects.com/huamei_0prmtq/afts/img/A*khrYRYi6VN0AAAAAAAAAAAAADvuFAQ/original","isActive":true,"isInactive":false,"isDeactivated":false,"isTerminated":false,"isBanned":false,"isBlocked":false,"isExtcontact":false,"isPaid":false,"hasMemberLevel":true,"memberLevelName":"专业会员","isTopLevel":false,"isNewbie":false,"isWillExpire":false,"isWiki":false,"account":{"id":21009529,"name":"李辉","username":"f2eb55e8-f05b-4677-b571-9b54988e1512","mobile":"175******69","email":"","_serializer":"web.account"},"corpAccounts":{},"id":21366222,"created_at":"2021-04-12T03:57:14.000Z","updated_at":"2023-05-09T12:49:13.000Z","space_id":0,"type":"User","name":"李辉","description":null,"avatar":null,"owner_id":null,"topics_count":0,"public_topics_count":0,"members_count":0,"books_count":5,"public_books_count":1,"followers_count":1,"following_count":2,"account_id":21009529,"role":1,"status":1,"public":1,"wants_email":true,"wants_marketing_email":true,"topic_updated_at_ms":0,"deleted_slug":null,"language":"zh-cn","organization_id":0,"zone_id":0,"emp_type":null,"group_department_updated_at":null,"expired_at":"2023-04-24T15:59:59.000Z","scene":null,"source":"new_session","max_member":null,"last_logined_at":"2023-04-04T13:22:54.000Z","deleted_at":null,"grains_sum":736,"punish_expired_at":null,"extend_private":0,"hasPassword":true,"isTrial":false,"canCreateOrg":true,"hasNewEvent":false,"is_admin":false,"theme":"default"},"notification":{"notifyCount":5,"showDot":false},"settings":{"allowed_link_schema":["dingtalk:"],"enable_link_interception":true,"enable_new_user_public_ability_forbid":true,"user_registry_forbidden_level":"","watermark_enable":"","public_space_doc_search_enable":true,"lake_enabled_groups":"*","image_proxy_root":"","max_import_task_count":1,"enable_search":true,"enable_serviceworker":true,"enable_lazyload_card":"codeblock","editor_canary":{"card_lazy_init":100,"retryOriginImage":100},"enable_attachment_multipart":true,"enable_custom_video_player":true,"conference_gift_num":0,"intranet_safe_tip":["open"],"publication_enable_whitelist":[],"foreign_phone_registry_enabled_organization_whitelist":["16014876","16022684","1437","1565","1796","2838","309","22614","1780","6001397","14481","14040138","16052442","14043106","14006688","16033469","18044074","11321","2008","35721","6001216"],"disabled_login_modal_pop_default":true,"enable_open_in_mobile_app":true,"enable_issue":true,"enable_blank_page_detect":true,"zone_ant_auth_keepalive_enabled_domains":[],"enable_new_group_page_whitelist":[],"enable_web_ocr":{"enable":true,"enableBrowsers":["chrome"],"users":[106822],"percent":0},"disableDocumentCopy":true},"env":"prod","space":{"id":0,"login":"","name":"语雀","short_name":null,"status":0,"account_id":0,"logo":null,"description":"","created_at":null,"updated_at":null,"host":"https://www.yuque.com","displayName":"语雀","logo_url":"https://cdn.nlark.com/yuque/0/2022/png/303152/1665994257081-avatar/dcb74862-1409-4691-b9ce-8173b4f6e037.png","enable_password":true,"enable_watermark":false,"_serializer":"web.space"},"isYuque":true,"isPublicCloud":true,"isEnterprise":false,"isUseAntLogin":false,"defaultSpaceHost":"https://www.yuque.com","timestamp":1683712150948,"traceId":"ac122cfc16837121506552472119","siteName":"语雀","siteTip":null,"activityTip":null,"topTip":null,"readTip":{"global":22981055,"org_global":358649,"dashboard":12657571,"broadcast":12656448,"feature_events_move_to_explore":1,"feature_book_custom_index_landing":1,"public_popup":34604497,"feature_org_switch":1,"org_popup":33313731,"skylink_guide":1,"public_broadcast":34225631,"feature_dashboard_books":1,"feature_group_introduce_12565824":1,"feature_garden_introduce_21366222":1,"feature_group_introduce_33062766":1,"switch_org_postion_change_tip":1,"owner_help_introduce_tip_21366222":1,"yuque_org_question_recommend":35445124,"yuque_public_question_recommend":35581668},"questionRecommend":null,"dashboardBannerRecommend":null,"imageServiceDomains":["cdn.yuque.com","cdn.nlark.com","img.shields.io","travis-ci.org","api.travis-ci.org","npm.packagequality.com","snyk.io","coveralls.io","badgen.now.sh","badgen.net","packagephobia.now.sh","duing.alibaba-inc.com","npm.alibaba-inc.com","web.npm.alibaba-inc.com","npmjs.com","npmjs.org","npg.dockerlab.alipay.net","private-alipayobjects.alipay.com","googleusercontent.com","blogspot.com","cdn.hk01.com","camo.githubusercontent.com","gw.daily.taobaocdn.net","cdn-images-1.medium.com","medium.com","i.stack.imgur.com","imgur.com","doc.ucweb.local","lh6.googleusercontent.com","4.bp.blogspot.com","bp.blogspot.com","blogspot.com","1.bp.blogspot.com","2.bp.blogspot.com","3.bp.blogspot.com","aliwork-files.oss-accelerate.aliyuncs.com","oss-accelerate.aliyuncs.com","work.alibaba.net","work.alibaba-inc.com","work-file.alibaba.net","work-file.alibaba-inc.com","pre-work-file.alibaba-inc.com","yuque.antfin.com","yuque.antfin-inc.com","intranetproxy.alipay.com","lark-assets-prod-aliyun.oss-accelerate.aliyuncs.com","lh1.googleusercontent.com","lh2.googleusercontent.com","lh3.googleusercontent.com","lh4.googleusercontent.com","lh5.googleusercontent.com","lh6.googleusercontent.com","lh7.googleusercontent.com","lh8.googleusercontent.com","lh9.googleusercontent.com","raw.githubusercontent.com","github.com","en.wikipedia.org","rawcdn.githack.com","pre-work-file.alibaba-inc.com","alipay-rmsdeploy-image.cn-hangzhou.alipay.aliyun-inc.com","antsys-align-files-management.cn-hangzhou.alipay.aliyun-inc.com","baiyan-pre.antfin.com","baiyan.antfin.com","baiyan.dev.alipay.net","marketing.aliyun-inc.com","lark-temp.oss-cn-hangzhou.aliyuncs.com","www.yuque.com","yuque.com","cdn.nlark.com"],"sharePlatforms":["wechat","dingtalk"],"locale":"zh-cn","matchCondition":{"page":"bookOverview"},"group":{"id":26318626,"type":"User","login":"snab","name":"少年阿斌","description":"你把时间花在哪，你的收获就在哪","avatar":"https://cdn.nlark.com/yuque/0/2022/png/26318626/1648273349001-avatar/d128a7f7-917d-4523-8048-09226d5c5c56.png","avatar_url":"https://cdn.nlark.com/yuque/0/2022/png/26318626/1648273349001-avatar/d128a7f7-917d-4523-8048-09226d5c5c56.png","owner_id":null,"books_count":3,"public_books_count":3,"topics_count":0,"public_topics_count":0,"members_count":0,"abilities":{"create_book":false,"create_member":false,"destroy":false,"read":true,"read_private":false,"update":false,"manage":false,"restore":false},"settings":null,"public":1,"extend_private":0,"scene":null,"created_at":"2022-02-28T17:07:02.000Z","updated_at":"2023-05-10T08:22:16.000Z","expired_at":"2025-06-28T15:59:59.000Z","organization_id":0,"isPaid":true,"member_level":2,"memberLevelName":"超级会员","hasMemberLevel":true,"isTopLevel":true,"grains_sum":5960,"status":1,"source":"app","zone_id":0,"isPermanentPunished":false,"isWiki":false,"organization":null,"owners":null,"_serializer":"web.group"},"book":{"id":25545990,"type":"Book","slug":"java","name":"阿斌Java之路","toc":[{"type":"DOC","title":"使用说明（必读）","uuid":"Rth2gUptOY8kaqRm","url":"nrdyiu","prev_uuid":"","sibling_uuid":"DcCfPcQNXdmVM9-g","child_uuid":"","parent_uuid":"","doc_id":78843297,"level":0,"id":78843297,"open_window":1,"visible":1},{"type":"DOC","title":"Java学习路线规划","uuid":"DcCfPcQNXdmVM9-g","url":"hq59pekk26w2ymam","prev_uuid":"Rth2gUptOY8kaqRm","sibling_uuid":"TNwMwDzkxqp35YG6","child_uuid":"","parent_uuid":"","doc_id":112789160,"level":0,"id":112789160,"open_window":1,"visible":1},{"type":"DOC","title":"阿斌的知识星球","uuid":"TNwMwDzkxqp35YG6","url":"cne0nel2hny8eu4i","prev_uuid":"DcCfPcQNXdmVM9-g","sibling_uuid":"aFJTde-XXKs9Ye2X","child_uuid":"DfNY34KorhyaouBc","parent_uuid":"","doc_id":113366214,"level":0,"id":113366214,"open_window":1,"visible":1},{"type":"DOC","title":"项目亮点","uuid":"DfNY34KorhyaouBc","url":"kpylsv90giebslxn","prev_uuid":"TNwMwDzkxqp35YG6","sibling_uuid":"eOIkiGM6ZQYoExj2","child_uuid":"Y1zSbPlp4fiROb-h","parent_uuid":"TNwMwDzkxqp35YG6","doc_id":117542378,"level":1,"id":117542378,"open_window":1,"visible":1},{"type":"DOC","title":"消息推送平台Austin","uuid":"Y1zSbPlp4fiROb-h","url":"ht5eyxas0twd87nq","prev_uuid":"DfNY34KorhyaouBc","sibling_uuid":"","child_uuid":"","parent_uuid":"DfNY34KorhyaouBc","doc_id":117542497,"level":2,"id":117542497,"open_window":1,"visible":1},{"type":"DOC","title":"ChatGPT注册教程","uuid":"eOIkiGM6ZQYoExj2","url":"hkx9mhgr1m8quzcf","prev_uuid":"DfNY34KorhyaouBc","sibling_uuid":"bCPXo0uRX5vNgCID","child_uuid":"","parent_uuid":"TNwMwDzkxqp35YG6","doc_id":117429215,"level":1,"id":117429215,"open_window":1,"visible":1},{"type":"DOC","title":"面经","uuid":"bCPXo0uRX5vNgCID","url":"pzbs3w7701lcvzgq","prev_uuid":"eOIkiGM6ZQYoExj2","sibling_uuid":"rS5lwK4ssHTX7sSq","child_uuid":"","parent_uuid":"TNwMwDzkxqp35YG6","doc_id":117141212,"level":1,"id":117141212,"open_window":1,"visible":1},{"type":"DOC","title":"简历优化","uuid":"rS5lwK4ssHTX7sSq","url":"gx5w8241hgqtwhsz","prev_uuid":"bCPXo0uRX5vNgCID","sibling_uuid":"VUJqdC8p_Q11u3_u","child_uuid":"","parent_uuid":"TNwMwDzkxqp35YG6","doc_id":117076833,"level":1,"id":117076833,"open_window":1,"visible":1},{"type":"DOC","title":"架构设计文章","uuid":"VUJqdC8p_Q11u3_u","url":"lhddqzlwsmgofpi7","prev_uuid":"rS5lwK4ssHTX7sSq","sibling_uuid":"aqXUtfRKSnHrCd4K","child_uuid":"","parent_uuid":"TNwMwDzkxqp35YG6","doc_id":115391543,"level":1,"id":115391543,"open_window":1,"visible":1},{"type":"DOC","title":"活跃的社群","uuid":"aqXUtfRKSnHrCd4K","url":"amwfomvcqx7581uz","prev_uuid":"VUJqdC8p_Q11u3_u","sibling_uuid":"","child_uuid":"","parent_uuid":"TNwMwDzkxqp35YG6","doc_id":115710703,"level":1,"id":115710703,"open_window":1,"visible":1},{"type":"DOC","title":"netty","uuid":"aFJTde-XXKs9Ye2X","url":"sm96cs6aa8n5qa2r","prev_uuid":"TNwMwDzkxqp35YG6","sibling_uuid":"Mh10lsQ5Lo6_Iaj4","child_uuid":"","parent_uuid":"","doc_id":120289259,"level":0,"id":120289259,"open_window":1,"visible":1},{"type":"DOC","title":"并发包","uuid":"Mh10lsQ5Lo6_Iaj4","url":"co8h2wbyxsb099gp","prev_uuid":"aFJTde-XXKs9Ye2X","sibling_uuid":"fYO0fOnpXn0pi1WM","child_uuid":"uvLLu99zjl-QUCIr","parent_uuid":"","doc_id":108388410,"level":0,"id":108388410,"open_window":1,"visible":1},{"type":"DOC","title":"线程基础","uuid":"uvLLu99zjl-QUCIr","url":"pwt2s3pnabd74d2y","prev_uuid":"Mh10lsQ5Lo6_Iaj4","sibling_uuid":"8Tk43LJ_hJqMmPXx","child_uuid":"1YbUWUkgqb9QZScr","parent_uuid":"Mh10lsQ5Lo6_Iaj4","doc_id":113292423,"level":1,"id":113292423,"open_window":1,"visible":1},{"type":"DOC","title":"volatile原子性讨论","uuid":"1YbUWUkgqb9QZScr","url":"qao6ark7d8wq8miu","prev_uuid":"uvLLu99zjl-QUCIr","sibling_uuid":"0ryUVRjS9FhcrNIr","child_uuid":"","parent_uuid":"uvLLu99zjl-QUCIr","doc_id":118815272,"level":2,"id":118815272,"open_window":1,"visible":1},{"type":"DOC","title":"线程创建流程","uuid":"0ryUVRjS9FhcrNIr","url":"sbsdatbh9x3pgi6p","prev_uuid":"1YbUWUkgqb9QZScr","sibling_uuid":"","child_uuid":"","parent_uuid":"uvLLu99zjl-QUCIr","doc_id":115608687,"level":2,"id":115608687,"open_window":1,"visible":1},{"type":"DOC","title":"锁基础","uuid":"8Tk43LJ_hJqMmPXx","url":"licpodw9pnalbrt4","prev_uuid":"uvLLu99zjl-QUCIr","sibling_uuid":"9BE8T1_FcPUSOOV6","child_uuid":"6DSFui8hubXm4jbm","parent_uuid":"Mh10lsQ5Lo6_Iaj4","doc_id":113400115,"level":1,"id":113400115,"open_window":1,"visible":1},{"type":"DOC","title":"Synchronized锁升级","uuid":"6DSFui8hubXm4jbm","url":"lwlp41","prev_uuid":"8Tk43LJ_hJqMmPXx","sibling_uuid":"SCaEWV9PAd9pJgwy","child_uuid":"","parent_uuid":"8Tk43LJ_hJqMmPXx","doc_id":84513293,"level":2,"id":84513293,"open_window":1,"visible":1},{"type":"DOC","title":"AQS","uuid":"SCaEWV9PAd9pJgwy","url":"agbmupgrg4159wx7","prev_uuid":"6DSFui8hubXm4jbm","sibling_uuid":"","child_uuid":"","parent_uuid":"8Tk43LJ_hJqMmPXx","doc_id":113987820,"level":2,"id":113987820,"open_window":1,"visible":1},{"type":"DOC","title":"并发工具","uuid":"9BE8T1_FcPUSOOV6","url":"tbma9x40r1nv40to","prev_uuid":"8Tk43LJ_hJqMmPXx","sibling_uuid":"XqeMorjqhwYljCAS","child_uuid":"","parent_uuid":"Mh10lsQ5Lo6_Iaj4","doc_id":113400195,"level":1,"id":113400195,"open_window":1,"visible":1},{"type":"DOC","title":"并发容器","uuid":"XqeMorjqhwYljCAS","url":"op0z1zvtoln7mkx0","prev_uuid":"9BE8T1_FcPUSOOV6","sibling_uuid":"T51fX19UFaau5PGQ","child_uuid":"","parent_uuid":"Mh10lsQ5Lo6_Iaj4","doc_id":113400560,"level":1,"id":113400560,"open_window":1,"visible":1},{"type":"DOC","title":"线程池","uuid":"T51fX19UFaau5PGQ","url":"gik0iy9w7tuhq9c6","prev_uuid":"XqeMorjqhwYljCAS","sibling_uuid":"","child_uuid":"","parent_uuid":"Mh10lsQ5Lo6_Iaj4","doc_id":113400472,"level":1,"id":113400472,"open_window":1,"visible":1},{"type":"DOC","title":"并发包八股文（查缺补漏）","uuid":"fYO0fOnpXn0pi1WM","url":"rx2nn24rap3203h7","prev_uuid":"Mh10lsQ5Lo6_Iaj4","sibling_uuid":"KDOjMAhiG3uyvLuO","child_uuid":"","parent_uuid":"","doc_id":114524201,"level":0,"id":114524201,"open_window":1,"visible":1},{"type":"DOC","title":"大厂面经","uuid":"KDOjMAhiG3uyvLuO","url":"urw4w2mos8l7owck","prev_uuid":"fYO0fOnpXn0pi1WM","sibling_uuid":"RzB3wbws6nFfaTnK","child_uuid":"kTEGASH4-DTc29KX","parent_uuid":"","doc_id":113292570,"level":0,"id":113292570,"open_window":1,"visible":1},{"type":"DOC","title":"快手","uuid":"kTEGASH4-DTc29KX","url":"ecdzl3ypepqe41wx","prev_uuid":"KDOjMAhiG3uyvLuO","sibling_uuid":"WWLGz1kVm75Z7NH4","child_uuid":"c5G-ucGCn0Tma6ZN","parent_uuid":"KDOjMAhiG3uyvLuO","doc_id":116953259,"level":1,"id":116953259,"open_window":1,"visible":1},{"type":"DOC","title":"快手一面【寄】","uuid":"c5G-ucGCn0Tma6ZN","url":"wz72nwgpfgopuif6","prev_uuid":"kTEGASH4-DTc29KX","sibling_uuid":"","child_uuid":"","parent_uuid":"kTEGASH4-DTc29KX","doc_id":116953264,"level":2,"id":116953264,"open_window":1,"visible":1},{"type":"DOC","title":"字节面经汇总","uuid":"WWLGz1kVm75Z7NH4","url":"pw5wmy9ls53hdy7z","prev_uuid":"kTEGASH4-DTc29KX","sibling_uuid":"J_-amJm1lnRiROXS","child_uuid":"","parent_uuid":"KDOjMAhiG3uyvLuO","doc_id":113292665,"level":1,"id":113292665,"open_window":1,"visible":1},{"type":"DOC","title":"京东面经汇总","uuid":"J_-amJm1lnRiROXS","url":"hsf72tnb9m3aaua1","prev_uuid":"WWLGz1kVm75Z7NH4","sibling_uuid":"IgHrLwgyQ4Kx3RIT","child_uuid":"C-vKmh7XrDu4un_c","parent_uuid":"KDOjMAhiG3uyvLuO","doc_id":113292612,"level":1,"id":113292612,"open_window":1,"visible":1},{"type":"DOC","title":"京东三面总包51w","uuid":"C-vKmh7XrDu4un_c","url":"mg8vgglmgxqscq89","prev_uuid":"J_-amJm1lnRiROXS","sibling_uuid":"","child_uuid":"","parent_uuid":"J_-amJm1lnRiROXS","doc_id":116953137,"level":2,"id":116953137,"open_window":1,"visible":1},{"type":"DOC","title":"阿里一面，携程三面","uuid":"IgHrLwgyQ4Kx3RIT","url":"py8p6ffnn0bphgi6","prev_uuid":"J_-amJm1lnRiROXS","sibling_uuid":"VLOIDRGS3pmSnUc5","child_uuid":"","parent_uuid":"KDOjMAhiG3uyvLuO","doc_id":113292595,"level":1,"id":113292595,"open_window":1,"visible":1},{"type":"DOC","title":"美团三面汇总","uuid":"VLOIDRGS3pmSnUc5","url":"yahzmv0od6d3ocm0","prev_uuid":"IgHrLwgyQ4Kx3RIT","sibling_uuid":"","child_uuid":"","parent_uuid":"KDOjMAhiG3uyvLuO","doc_id":113292585,"level":1,"id":113292585,"open_window":1,"visible":1},{"type":"DOC","title":"Redis","uuid":"RzB3wbws6nFfaTnK","url":"vzisng","prev_uuid":"KDOjMAhiG3uyvLuO","sibling_uuid":"Zyi60SEsMLskaysM","child_uuid":"dvzhQ0rqRx0UgUuR","parent_uuid":"","doc_id":94914344,"level":0,"id":94914344,"open_window":1,"visible":1},{"type":"DOC","title":"数据结构","uuid":"dvzhQ0rqRx0UgUuR","url":"kh819s","prev_uuid":"RzB3wbws6nFfaTnK","sibling_uuid":"FahgsN1R6dYoo7P0","child_uuid":"","parent_uuid":"RzB3wbws6nFfaTnK","doc_id":100203628,"level":1,"id":100203628,"open_window":1,"visible":1},{"type":"DOC","title":"Redis单机功能及原理","uuid":"FahgsN1R6dYoo7P0","url":"ix673b","prev_uuid":"dvzhQ0rqRx0UgUuR","sibling_uuid":"5GODhQtngJ2oZ8VW","child_uuid":"","parent_uuid":"RzB3wbws6nFfaTnK","doc_id":103353437,"level":1,"id":103353437,"open_window":1,"visible":1},{"type":"DOC","title":"Redis的IO多路复用","uuid":"5GODhQtngJ2oZ8VW","url":"oyni7r","prev_uuid":"FahgsN1R6dYoo7P0","sibling_uuid":"NVlv4XQIlqBxvE9o","child_uuid":"","parent_uuid":"RzB3wbws6nFfaTnK","doc_id":104941367,"level":1,"id":104941367,"open_window":1,"visible":1},{"type":"DOC","title":"Redis高可用架构","uuid":"NVlv4XQIlqBxvE9o","url":"lughl1","prev_uuid":"5GODhQtngJ2oZ8VW","sibling_uuid":"n9HLDokkarJd1Nve","child_uuid":"","parent_uuid":"RzB3wbws6nFfaTnK","doc_id":105131847,"level":1,"id":105131847,"open_window":1,"visible":1},{"type":"DOC","title":"Redis面试实录","uuid":"n9HLDokkarJd1Nve","url":"potyw8","prev_uuid":"NVlv4XQIlqBxvE9o","sibling_uuid":"vIDU2O2HML4qg6uO","child_uuid":"","parent_uuid":"RzB3wbws6nFfaTnK","doc_id":105203676,"level":1,"id":105203676,"open_window":1,"visible":1},{"type":"DOC","title":"Redis面试实录2","uuid":"vIDU2O2HML4qg6uO","url":"lg10v7t5rbqgw90v","prev_uuid":"n9HLDokkarJd1Nve","sibling_uuid":"csM4ixSWtV4Jaw74","child_uuid":"","parent_uuid":"RzB3wbws6nFfaTnK","doc_id":107460936,"level":1,"id":107460936,"open_window":1,"visible":1},{"type":"DOC","title":"Redis八股文","uuid":"csM4ixSWtV4Jaw74","url":"toonob","prev_uuid":"vIDU2O2HML4qg6uO","sibling_uuid":"","child_uuid":"","parent_uuid":"RzB3wbws6nFfaTnK","doc_id":100510768,"level":1,"id":100510768,"open_window":1,"visible":1},{"type":"DOC","title":"MySQL","uuid":"Zyi60SEsMLskaysM","url":"yks0bu","prev_uuid":"RzB3wbws6nFfaTnK","sibling_uuid":"MDV7a2FwLm9GK9tB","child_uuid":"0ouFgm_Mpi88sZ3F","parent_uuid":"","doc_id":89354620,"level":0,"id":89354620,"open_window":1,"visible":1},{"type":"DOC","title":"执行原理","uuid":"0ouFgm_Mpi88sZ3F","url":"wh6f8b","prev_uuid":"Zyi60SEsMLskaysM","sibling_uuid":"tQxi8TIsJAHUqDvi","child_uuid":"","parent_uuid":"Zyi60SEsMLskaysM","doc_id":89726481,"level":1,"id":89726481,"open_window":1,"visible":1},{"type":"DOC","title":"Server层特性","uuid":"tQxi8TIsJAHUqDvi","url":"gulk08","prev_uuid":"0ouFgm_Mpi88sZ3F","sibling_uuid":"M3nfim2zLtQDrX2k","child_uuid":"","parent_uuid":"Zyi60SEsMLskaysM","doc_id":92764983,"level":1,"id":92764983,"open_window":1,"visible":1},{"type":"DOC","title":"InnoDB存储格式","uuid":"M3nfim2zLtQDrX2k","url":"qs9ik0","prev_uuid":"tQxi8TIsJAHUqDvi","sibling_uuid":"Qus9OBWk9o56TpRI","child_uuid":"","parent_uuid":"Zyi60SEsMLskaysM","doc_id":91839792,"level":1,"id":91839792,"open_window":1,"visible":1},{"type":"DOC","title":"Innodb架构与特性","uuid":"Qus9OBWk9o56TpRI","url":"gzf2g9","prev_uuid":"M3nfim2zLtQDrX2k","sibling_uuid":"DEPzQNzujj12cgK8","child_uuid":"","parent_uuid":"Zyi60SEsMLskaysM","doc_id":91856878,"level":1,"id":91856878,"open_window":1,"visible":1},{"type":"DOC","title":"索引与算法","uuid":"DEPzQNzujj12cgK8","url":"kqhs7f","prev_uuid":"Qus9OBWk9o56TpRI","sibling_uuid":"7Q7ZQ8f7sCZVyqj1","child_uuid":"","parent_uuid":"Zyi60SEsMLskaysM","doc_id":91849319,"level":1,"id":91849319,"open_window":1,"visible":1},{"type":"DOC","title":"事务与锁","uuid":"7Q7ZQ8f7sCZVyqj1","url":"xe35k4","prev_uuid":"DEPzQNzujj12cgK8","sibling_uuid":"zxb9zM5MsXoqMXs0","child_uuid":"","parent_uuid":"Zyi60SEsMLskaysM","doc_id":92721493,"level":1,"id":92721493,"open_window":1,"visible":1},{"type":"DOC","title":"MySQL面试实录","uuid":"zxb9zM5MsXoqMXs0","url":"sqr2s2","prev_uuid":"7Q7ZQ8f7sCZVyqj1","sibling_uuid":"SevVRBTOHYAqROft","child_uuid":"","parent_uuid":"Zyi60SEsMLskaysM","doc_id":93508615,"level":1,"id":93508615,"open_window":1,"visible":1},{"type":"DOC","title":"MySQL八股文","uuid":"SevVRBTOHYAqROft","url":"uro7im","prev_uuid":"zxb9zM5MsXoqMXs0","sibling_uuid":"","child_uuid":"","parent_uuid":"Zyi60SEsMLskaysM","doc_id":91839388,"level":1,"id":91839388,"open_window":1,"visible":1},{"type":"DOC","title":"JVM","uuid":"MDV7a2FwLm9GK9tB","url":"rboytq","prev_uuid":"Zyi60SEsMLskaysM","sibling_uuid":"ewvn9W9OAiVxl7HC","child_uuid":"ydEBNd53MoqrH21p","parent_uuid":"","doc_id":83322034,"level":0,"id":83322034,"open_window":1,"visible":1},{"type":"DOC","title":"JVM加载与内存","uuid":"ydEBNd53MoqrH21p","url":"vc16a0","prev_uuid":"MDV7a2FwLm9GK9tB","sibling_uuid":"ahOaNrPVqI9FNWYD","child_uuid":"","parent_uuid":"MDV7a2FwLm9GK9tB","doc_id":83583201,"level":1,"id":83583201,"open_window":1,"visible":1},{"type":"DOC","title":"垃圾回收","uuid":"ahOaNrPVqI9FNWYD","url":"cyzmyg","prev_uuid":"ydEBNd53MoqrH21p","sibling_uuid":"zL9exevN-aYt-Vl6","child_uuid":"","parent_uuid":"MDV7a2FwLm9GK9tB","doc_id":85041531,"level":1,"id":85041531,"open_window":1,"visible":1},{"type":"DOC","title":"性能监控与调优篇","uuid":"zL9exevN-aYt-Vl6","url":"zibrm1","prev_uuid":"ahOaNrPVqI9FNWYD","sibling_uuid":"22DerhyNdGOCo1j1","child_uuid":"","parent_uuid":"MDV7a2FwLm9GK9tB","doc_id":85116486,"level":1,"id":85116486,"open_window":1,"visible":1},{"type":"DOC","title":"JVM八股文（查缺补漏）","uuid":"22DerhyNdGOCo1j1","url":"elztia","prev_uuid":"zL9exevN-aYt-Vl6","sibling_uuid":"","child_uuid":"","parent_uuid":"MDV7a2FwLm9GK9tB","doc_id":83593551,"level":1,"id":83593551,"open_window":1,"visible":1},{"type":"DOC","title":"MQ消息队列","uuid":"ewvn9W9OAiVxl7HC","url":"bklg7o","prev_uuid":"MDV7a2FwLm9GK9tB","sibling_uuid":"Loi2ItxFfIKMXJdS","child_uuid":"JQ8yWoPjlzFjn84R","parent_uuid":"","doc_id":82850044,"level":0,"id":82850044,"open_window":1,"visible":1},{"type":"DOC","title":"Kafka","uuid":"JQ8yWoPjlzFjn84R","url":"uwpprc","prev_uuid":"ewvn9W9OAiVxl7HC","sibling_uuid":"oGFFdICm1-p76bye","child_uuid":"XDQwSKyEr2NBLSnb","parent_uuid":"ewvn9W9OAiVxl7HC","doc_id":80958186,"level":1,"id":80958186,"open_window":1,"visible":1},{"type":"DOC","title":"producer","uuid":"XDQwSKyEr2NBLSnb","url":"qx2tgg","prev_uuid":"JQ8yWoPjlzFjn84R","sibling_uuid":"pp4ctwhLgcoWv-cI","child_uuid":"","parent_uuid":"JQ8yWoPjlzFjn84R","doc_id":80963929,"level":2,"id":80963929,"open_window":1,"visible":1},{"type":"DOC","title":"broker","uuid":"pp4ctwhLgcoWv-cI","url":"mk9prw","prev_uuid":"XDQwSKyEr2NBLSnb","sibling_uuid":"aQGUIWocU_xg0tG2","child_uuid":"","parent_uuid":"JQ8yWoPjlzFjn84R","doc_id":81432424,"level":2,"id":81432424,"open_window":1,"visible":1},{"type":"DOC","title":"consumer","uuid":"aQGUIWocU_xg0tG2","url":"mwngst","prev_uuid":"pp4ctwhLgcoWv-cI","sibling_uuid":"fE_et5Y98KPBzZNC","child_uuid":"","parent_uuid":"JQ8yWoPjlzFjn84R","doc_id":81434768,"level":2,"id":81434768,"open_window":1,"visible":1},{"type":"DOC","title":"Kafka八股文（查缺补漏）","uuid":"fE_et5Y98KPBzZNC","url":"bhfhoh","prev_uuid":"aQGUIWocU_xg0tG2","sibling_uuid":"","child_uuid":"","parent_uuid":"JQ8yWoPjlzFjn84R","doc_id":81981361,"level":2,"id":81981361,"open_window":1,"visible":1},{"type":"DOC","title":"RocketMQ","uuid":"oGFFdICm1-p76bye","url":"vxtx3s","prev_uuid":"JQ8yWoPjlzFjn84R","sibling_uuid":"1MYRC6r3kVCNGqRI","child_uuid":"","parent_uuid":"ewvn9W9OAiVxl7HC","doc_id":82850874,"level":1,"id":82850874,"open_window":1,"visible":1},{"type":"DOC","title":"RocketMQ可靠消息Demo","uuid":"1MYRC6r3kVCNGqRI","url":"psgl53","prev_uuid":"oGFFdICm1-p76bye","sibling_uuid":"wThvsZSl8uu8AKJu","child_uuid":"","parent_uuid":"ewvn9W9OAiVxl7HC","doc_id":82878802,"level":1,"id":82878802,"open_window":1,"visible":1},{"type":"DOC","title":"RabbitMQ","uuid":"wThvsZSl8uu8AKJu","url":"trw0wh","prev_uuid":"1MYRC6r3kVCNGqRI","sibling_uuid":"sKcdJbTuhOgC5A-M","child_uuid":"","parent_uuid":"ewvn9W9OAiVxl7HC","doc_id":83071027,"level":1,"id":83071027,"open_window":1,"visible":1},{"type":"DOC","title":"MQ综合对比","uuid":"sKcdJbTuhOgC5A-M","url":"gxvpca","prev_uuid":"wThvsZSl8uu8AKJu","sibling_uuid":"","child_uuid":"","parent_uuid":"ewvn9W9OAiVxl7HC","doc_id":83077976,"level":1,"id":83077976,"open_window":1,"visible":1},{"type":"DOC","title":"操作系统","uuid":"Loi2ItxFfIKMXJdS","url":"bm510z","prev_uuid":"ewvn9W9OAiVxl7HC","sibling_uuid":"a18bSe7UbwZ_fGMS","child_uuid":"8QyLyNFoToB0FGHW","parent_uuid":"","doc_id":79973618,"level":0,"id":79973618,"open_window":1,"visible":1},{"type":"DOC","title":"进程","uuid":"8QyLyNFoToB0FGHW","url":"rw0g4r","prev_uuid":"Loi2ItxFfIKMXJdS","sibling_uuid":"ywI6tGU00C0_uH7V","child_uuid":"","parent_uuid":"Loi2ItxFfIKMXJdS","doc_id":79987302,"level":1,"id":79987302,"open_window":1,"visible":1},{"type":"DOC","title":"内存","uuid":"ywI6tGU00C0_uH7V","url":"og650h","prev_uuid":"8QyLyNFoToB0FGHW","sibling_uuid":"WWEEJ9SIlcGvkQ7o","child_uuid":"","parent_uuid":"Loi2ItxFfIKMXJdS","doc_id":80163716,"level":1,"id":80163716,"open_window":1,"visible":1},{"type":"DOC","title":"磁盘与IO","uuid":"WWEEJ9SIlcGvkQ7o","url":"vp6o82","prev_uuid":"ywI6tGU00C0_uH7V","sibling_uuid":"jvx-Yk9wjmv68oU3","child_uuid":"","parent_uuid":"Loi2ItxFfIKMXJdS","doc_id":80198174,"level":1,"id":80198174,"open_window":1,"visible":1},{"type":"DOC","title":"MESI与内存屏障与Volatile","uuid":"jvx-Yk9wjmv68oU3","url":"uq2360nvh6k0lm9x","prev_uuid":"WWEEJ9SIlcGvkQ7o","sibling_uuid":"e1j6C6BOFAldC_SI","child_uuid":"","parent_uuid":"Loi2ItxFfIKMXJdS","doc_id":108035336,"level":1,"id":108035336,"open_window":1,"visible":1},{"type":"DOC","title":"操作系统八股文（查缺补漏）","uuid":"e1j6C6BOFAldC_SI","url":"gbmr70","prev_uuid":"jvx-Yk9wjmv68oU3","sibling_uuid":"","child_uuid":"","parent_uuid":"Loi2ItxFfIKMXJdS","doc_id":80817258,"level":1,"id":80817258,"open_window":1,"visible":1},{"type":"DOC","title":"计算机网络","uuid":"a18bSe7UbwZ_fGMS","url":"xgr4mq","prev_uuid":"Loi2ItxFfIKMXJdS","sibling_uuid":"F4Il-NSSbf0qFBNq","child_uuid":"tDkFbYfv6bfJBMy7","parent_uuid":"","doc_id":78198618,"level":0,"id":78198618,"open_window":1,"visible":1},{"type":"DOC","title":"计算机网络概述","uuid":"tDkFbYfv6bfJBMy7","url":"aurix8","prev_uuid":"a18bSe7UbwZ_fGMS","sibling_uuid":"mHncQD1SagclDiKn","child_uuid":"","parent_uuid":"a18bSe7UbwZ_fGMS","doc_id":78198654,"level":1,"id":78198654,"open_window":1,"visible":1},{"type":"DOC","title":"应用层","uuid":"mHncQD1SagclDiKn","url":"gip9ef","prev_uuid":"tDkFbYfv6bfJBMy7","sibling_uuid":"K1tKDuia50khjbxv","child_uuid":"","parent_uuid":"a18bSe7UbwZ_fGMS","doc_id":78346404,"level":1,"id":78346404,"open_window":1,"visible":1},{"type":"DOC","title":"传输层","uuid":"K1tKDuia50khjbxv","url":"sgmh83","prev_uuid":"mHncQD1SagclDiKn","sibling_uuid":"UbuXJv3zcicKtgcC","child_uuid":"","parent_uuid":"a18bSe7UbwZ_fGMS","doc_id":78696309,"level":1,"id":78696309,"open_window":1,"visible":1},{"type":"DOC","title":"网络层","uuid":"UbuXJv3zcicKtgcC","url":"izhazi","prev_uuid":"K1tKDuia50khjbxv","sibling_uuid":"jqfnL-rP9jWfD07G","child_uuid":"","parent_uuid":"a18bSe7UbwZ_fGMS","doc_id":78930192,"level":1,"id":78930192,"open_window":1,"visible":1},{"type":"DOC","title":"链路层","uuid":"jqfnL-rP9jWfD07G","url":"hu1kfr","prev_uuid":"UbuXJv3zcicKtgcC","sibling_uuid":"hdVDNRX7TCPhRb8z","child_uuid":"","parent_uuid":"a18bSe7UbwZ_fGMS","doc_id":79061862,"level":1,"id":79061862,"open_window":1,"visible":1},{"type":"DOC","title":"物理层","uuid":"hdVDNRX7TCPhRb8z","url":"ggd8p6","prev_uuid":"jqfnL-rP9jWfD07G","sibling_uuid":"Z2tTcNO4dLo7fyyn","child_uuid":"","parent_uuid":"a18bSe7UbwZ_fGMS","doc_id":79060905,"level":1,"id":79060905,"open_window":1,"visible":1},{"type":"DOC","title":"计算机网络八股文（查缺补漏）","uuid":"Z2tTcNO4dLo7fyyn","url":"llurt6","prev_uuid":"hdVDNRX7TCPhRb8z","sibling_uuid":"","child_uuid":"","parent_uuid":"a18bSe7UbwZ_fGMS","doc_id":78845370,"level":1,"id":78845370,"open_window":1,"visible":1},{"type":"DOC","title":"Dubbo","uuid":"F4Il-NSSbf0qFBNq","url":"cczqbv","prev_uuid":"a18bSe7UbwZ_fGMS","sibling_uuid":"cZUhQKCysu7HQWrz","child_uuid":"KR53K6sj6WHpw1YE","parent_uuid":"","doc_id":76348814,"level":0,"id":76348814,"open_window":1,"visible":1},{"type":"DOC","title":"框架整体设计","uuid":"KR53K6sj6WHpw1YE","url":"wls6qk","prev_uuid":"F4Il-NSSbf0qFBNq","sibling_uuid":"TB8uu2O839apEV4V","child_uuid":"","parent_uuid":"F4Il-NSSbf0qFBNq","doc_id":77359157,"level":1,"id":77359157,"open_window":1,"visible":1},{"type":"DOC","title":"扩展机制SPI","uuid":"TB8uu2O839apEV4V","url":"qg3bi5","prev_uuid":"KR53K6sj6WHpw1YE","sibling_uuid":"uIejnH6pcD-VI8be","child_uuid":"","parent_uuid":"F4Il-NSSbf0qFBNq","doc_id":77358853,"level":1,"id":77358853,"open_window":1,"visible":1},{"type":"DOC","title":"服务暴露原理","uuid":"uIejnH6pcD-VI8be","url":"uslz2x","prev_uuid":"TB8uu2O839apEV4V","sibling_uuid":"Fq8QtUvA3Cm2Qy5b","child_uuid":"","parent_uuid":"F4Il-NSSbf0qFBNq","doc_id":77358995,"level":1,"id":77358995,"open_window":1,"visible":1},{"type":"DOC","title":"服务引用原理","uuid":"Fq8QtUvA3Cm2Qy5b","url":"rpr1a4","prev_uuid":"uIejnH6pcD-VI8be","sibling_uuid":"C0SCo20JtWUx8fql","child_uuid":"","parent_uuid":"F4Il-NSSbf0qFBNq","doc_id":77359083,"level":1,"id":77359083,"open_window":1,"visible":1},{"type":"DOC","title":"服务调用流程","uuid":"C0SCo20JtWUx8fql","url":"yrwndh","prev_uuid":"Fq8QtUvA3Cm2Qy5b","sibling_uuid":"rCiVwsx-eaRqDn-J","child_uuid":"","parent_uuid":"F4Il-NSSbf0qFBNq","doc_id":77359113,"level":1,"id":77359113,"open_window":1,"visible":1},{"type":"DOC","title":"集群容错","uuid":"rCiVwsx-eaRqDn-J","url":"af8v1d","prev_uuid":"C0SCo20JtWUx8fql","sibling_uuid":"ZzaKWnrkAAmwa_O_","child_uuid":"","parent_uuid":"F4Il-NSSbf0qFBNq","doc_id":77990650,"level":1,"id":77990650,"open_window":1,"visible":1},{"type":"DOC","title":"过滤器","uuid":"ZzaKWnrkAAmwa_O_","url":"qwuiok","prev_uuid":"rCiVwsx-eaRqDn-J","sibling_uuid":"Jjn-Mwqrfiwv2-ZF","child_uuid":"","parent_uuid":"F4Il-NSSbf0qFBNq","doc_id":78147502,"level":1,"id":78147502,"open_window":1,"visible":1},{"type":"DOC","title":"Dubbo八股文（查缺补漏）","uuid":"Jjn-Mwqrfiwv2-ZF","url":"tqa4r8","prev_uuid":"ZzaKWnrkAAmwa_O_","sibling_uuid":"","child_uuid":"","parent_uuid":"F4Il-NSSbf0qFBNq","doc_id":78122962,"level":1,"id":78122962,"open_window":1,"visible":1},{"type":"DOC","title":"Spring","uuid":"cZUhQKCysu7HQWrz","url":"pw617l","prev_uuid":"F4Il-NSSbf0qFBNq","sibling_uuid":"dS4Risf4nlkGdG1N","child_uuid":"eqxjuOgtiPl3LScQ","parent_uuid":"","doc_id":71050693,"level":0,"id":71050693,"open_window":1,"visible":1},{"type":"DOC","title":"Bean的生命周期","uuid":"eqxjuOgtiPl3LScQ","url":"nsgx6m","prev_uuid":"cZUhQKCysu7HQWrz","sibling_uuid":"6ql4o43cPNwMmhku","child_uuid":"","parent_uuid":"cZUhQKCysu7HQWrz","doc_id":70183319,"level":1,"id":70183319,"open_window":1,"visible":1},{"type":"DOC","title":"SpringContext的生命周期","uuid":"6ql4o43cPNwMmhku","url":"wlag2g","prev_uuid":"eqxjuOgtiPl3LScQ","sibling_uuid":"SOygAWXcQ7cFukY7","child_uuid":"","parent_uuid":"cZUhQKCysu7HQWrz","doc_id":72304155,"level":1,"id":72304155,"open_window":1,"visible":1},{"type":"DOC","title":"BeanFactory如何处理循环依赖","uuid":"SOygAWXcQ7cFukY7","url":"qb9i26","prev_uuid":"6ql4o43cPNwMmhku","sibling_uuid":"l1IpF1gWdjF1vjZe","child_uuid":"","parent_uuid":"cZUhQKCysu7HQWrz","doc_id":74071136,"level":1,"id":74071136,"open_window":1,"visible":0},{"type":"DOC","title":"Spring Bean元信息配置与解析","uuid":"l1IpF1gWdjF1vjZe","url":"ri3pey","prev_uuid":"SOygAWXcQ7cFukY7","sibling_uuid":"CxFaoZ5wRkRJbdcI","child_uuid":"","parent_uuid":"cZUhQKCysu7HQWrz","doc_id":70865255,"level":1,"id":70865255,"open_window":1,"visible":1},{"type":"DOC","title":"BeanFactory和ApplicationContext的区别","uuid":"CxFaoZ5wRkRJbdcI","url":"miwr6c","prev_uuid":"l1IpF1gWdjF1vjZe","sibling_uuid":"wORjFZJL5ZypHo5A","child_uuid":"","parent_uuid":"cZUhQKCysu7HQWrz","doc_id":73887679,"level":1,"id":73887679,"open_window":1,"visible":1},{"type":"DOC","title":"Spring事件","uuid":"wORjFZJL5ZypHo5A","url":"sp7tsb","prev_uuid":"CxFaoZ5wRkRJbdcI","sibling_uuid":"6qph2HURSZs_GERr","child_uuid":"","parent_uuid":"cZUhQKCysu7HQWrz","doc_id":73887751,"level":1,"id":73887751,"open_window":1,"visible":0},{"type":"DOC","title":"Spring AOP","uuid":"6qph2HURSZs_GERr","url":"bywv17","prev_uuid":"wORjFZJL5ZypHo5A","sibling_uuid":"","child_uuid":"","parent_uuid":"cZUhQKCysu7HQWrz","doc_id":74732829,"level":1,"id":74732829,"open_window":1,"visible":1},{"type":"DOC","title":"问题记录","uuid":"dS4Risf4nlkGdG1N","url":"ehm2eglun820qaxg","prev_uuid":"cZUhQKCysu7HQWrz","sibling_uuid":"pv3C_rJwEjVg5GdN","child_uuid":"","parent_uuid":"","doc_id":106097682,"level":0,"id":106097682,"open_window":1,"visible":1},{"type":"DOC","title":"临时","uuid":"pv3C_rJwEjVg5GdN","url":"ut9hnr","prev_uuid":"dS4Risf4nlkGdG1N","sibling_uuid":"","child_uuid":"2wShMw9LOc1HyvPA","parent_uuid":"","doc_id":71104773,"level":0,"id":71104773,"open_window":1,"visible":1},{"type":"DOC","title":"aop临时","uuid":"2wShMw9LOc1HyvPA","url":"crax36","prev_uuid":"pv3C_rJwEjVg5GdN","sibling_uuid":"CKUA-Xc22n71h9CA","child_uuid":"","parent_uuid":"pv3C_rJwEjVg5GdN","doc_id":74745603,"level":1,"id":74745603,"open_window":1,"visible":1},{"type":"DOC","title":"enviroment","uuid":"CKUA-Xc22n71h9CA","url":"qnxawd","prev_uuid":"2wShMw9LOc1HyvPA","sibling_uuid":"PL7Wlgrh2FxFTUlT","child_uuid":"","parent_uuid":"pv3C_rJwEjVg5GdN","doc_id":72272208,"level":1,"id":72272208,"open_window":1,"visible":1},{"type":"DOC","title":"注解","uuid":"PL7Wlgrh2FxFTUlT","url":"ltay4o","prev_uuid":"CKUA-Xc22n71h9CA","sibling_uuid":"rhjuQtUUMOEjy_Wj","child_uuid":"","parent_uuid":"pv3C_rJwEjVg5GdN","doc_id":72185796,"level":1,"id":72185796,"open_window":1,"visible":1},{"type":"DOC","title":"spring事件","uuid":"rhjuQtUUMOEjy_Wj","url":"py40m3","prev_uuid":"PL7Wlgrh2FxFTUlT","sibling_uuid":"2OHmn8fChLad3mTJ","child_uuid":"","parent_uuid":"pv3C_rJwEjVg5GdN","doc_id":71927879,"level":1,"id":71927879,"open_window":1,"visible":1},{"type":"DOC","title":"无标题文档","uuid":"2OHmn8fChLad3mTJ","url":"pnedss","prev_uuid":"rhjuQtUUMOEjy_Wj","sibling_uuid":"Jr77d_S51H6KmvCN","child_uuid":"","parent_uuid":"pv3C_rJwEjVg5GdN","doc_id":71643211,"level":1,"id":71643211,"open_window":1,"visible":0},{"type":"DOC","title":"国际化","uuid":"Jr77d_S51H6KmvCN","url":"obambu","prev_uuid":"2OHmn8fChLad3mTJ","sibling_uuid":"02myJ_ZD6tSB6BmR","child_uuid":"","parent_uuid":"pv3C_rJwEjVg5GdN","doc_id":71131700,"level":1,"id":71131700,"open_window":1,"visible":1},{"type":"DOC","title":"resource","uuid":"02myJ_ZD6tSB6BmR","url":"facyqw","prev_uuid":"Jr77d_S51H6KmvCN","sibling_uuid":"","child_uuid":"","parent_uuid":"pv3C_rJwEjVg5GdN","doc_id":71104807,"level":1,"id":71104807,"open_window":1,"visible":1}],"toc_updated_at":"2023-04-12T13:20:40.000Z","description":"学Java一个知识库就够了。包含全套java学习路线，配套学习资料，各知识点学习笔记。全面且深入的java八股文解读 。持续更新。。。","creator_id":26318626,"menu_type":0,"items_count":102,"likes_count":0,"watches_count":2,"user_id":26318626,"abilities":{"create_doc":false,"destroy":false,"export":false,"export_doc":false,"read":true,"read_private":false,"update":false,"create_collaborator":false,"manage":false,"share":false,"modify_setting":false},"public":1,"extend_private":0,"scene":null,"source":null,"created_at":"2022-03-20T04:36:19.000Z","updated_at":"2023-04-26T08:30:47.000Z","pinned_at":null,"archived_at":null,"isPremium":false,"premium":0,"layout":"Book","doc_typography":"classic_all","doc_viewport":"fixed","announcement":null,"should_manually_create_uid":false,"catalog_tail_type":"UPDATED_AT","catalog_display_level":1,"book_icon":{"type":"url","color":"#98D352","symbol":"book-type-default"},"cover":"https://cdn.nlark.com/yuque/0/2023/png/26318626/1677141203678-a3c0bd94-4278-4590-a83a-c4ca1483fa8c.png","comment_count":null,"organization_id":0,"status":0,"indexed_level":1,"privacy_migrated":true,"collaboration_count":1,"content_updated_at":"2023-04-26T08:30:47.423Z","content_updated_at_ms":1682497847423,"copyright_watermark":"","enable_announcement":true,"enable_auto_publish":true,"enable_comment":true,"enable_document_copy":false,"enable_export":true,"enable_search_engine":true,"enable_toc":true,"enable_trash":true,"enable_visitor_watermark":true,"enable_webhook":true,"image_copyright_watermark":"阿斌java之路","original":0,"resource_size":0,"user":null,"contributors":null,"_serializer":"web.book_detail"},"groupMemberInfo":{"usage":{"attachment_size":235939,"image_size":216284204,"video_size":0,"attachment_size_month":0,"image_size_month":1966705,"video_size_month":0,"max_upload_size":10737418240,"_serializer":"web.user_usage_statistics"},"totalDocAndNoteUsageMonth":9,"expired_at":"2025-06-28T15:59:59.000Z","countDownDays":780,"isAllowRenew":false,"receipt":null,"groupOwners":[],"hasOrder":false},"interest":{"interests":{"open_garden":false,"share_note":false,"open_api":false,"book_statistics":true,"open_ocr":true,"book_security":true,"create_public_resource":true,"book_webhook":true},"owner":{"id":21366222,"type":"User","member_level":0,"isTopLevel":false,"isMemberTopLevel":false,"isExpired":true},"limit":{"max_resource_total_size":null,"max_resource_month_size":1073741824,"max_book_number":10,"max_doc_note_number":100,"max_group_number":10,"max_book_collaborator_number":5,"max_doc_collaborator_number":5,"max_single_file_size":5242880,"max_single_image_size":5242880,"max_single_video_size":0},"limits":{"0":{"max_resource_total_size":null,"max_resource_month_size":1073741824,"max_book_number":10,"max_doc_note_number":100,"max_group_number":10,"max_book_collaborator_number":5,"max_doc_collaborator_number":5,"max_single_file_size":5242880,"max_single_image_size":5242880,"max_single_video_size":0},"1":{"max_resource_total_size":null,"max_resource_month_size":10737418240,"max_book_number":null,"max_doc_note_number":null,"max_group_number":10,"max_book_collaborator_number":50,"max_doc_collaborator_number":50,"max_single_file_size":524288000,"max_single_image_size":20971520,"max_single_video_size":524288000},"2":{"max_resource_total_size":null,"max_resource_month_size":53687091200,"max_book_number":null,"max_doc_note_number":null,"max_group_number":10,"max_single_file_size":2147483648,"max_single_image_size":52428800,"max_single_video_size":2147483648,"max_book_collaborator_number":500,"max_doc_collaborator_number":500}},"usage":{"isCurrentMonth":true,"attachment_size_month":0,"video_size_month":0,"image_size_month":112692,"doc_count_month":2,"max_upload_size":10737418240,"id":21340411,"created_at":"2021-04-12T03:57:14.000Z","updated_at":"2023-05-08T02:21:24.000Z","user_id":21366222,"attachment_size":50588,"video_size":0,"image_size":57427545,"last_updated_month":"202305","organization_id":0}},"organizations":[{"id":14043464,"login":"dataeye-saas","name":"Saas研发中心","description":null,"logo":"https://gw.alipayobjects.com/zos/bmw-prod/501b3e5b-8924-45cd-95ee-4c7470570204.svg","password_enable":1,"host":"https://dataeye-saas.yuque.com","member":{"name":null,"organization_id":14043464,"role":1,"status":1,"user":null,"organization":{"_serializer":"web.organization"},"_serializer":"web.organization_user"},"members_count":48,"_serializer":"web.organization_base"}],"paymentInfo":{"paymentBizInstId":"Z69"},"userMemberInfo":{"usage":{"attachment_size":50588,"image_size":57427545,"video_size":0,"attachment_size_month":0,"image_size_month":112692,"video_size_month":0,"max_upload_size":10737418240,"_serializer":"web.user_usage_statistics"},"totalDocAndNoteUsageMonth":2,"expired_at":"2023-04-24T15:59:59.000Z","countDownDays":-16,"isAllowRenew":true,"receipt":null,"groupOwners":[],"hasOrder":false},"login":{"loginType":"normal","enablePlatforms":["dingtalk","alipay","wechat","teambition","apple"],"isWechatMobileApp":false},"enableCoverageDeploy":false,"isDesktopApp":false,"isAssistant":false,"isAlipayApp":false,"isDingTalkApp":false,"isDingTalkMiniApp":false,"isDingTalkDesktopApp":false,"isYuqueMobileApp":false,"tracertConfig":{"spmAPos":"a385","spmBPos":null}}
     */
    private static final Pattern PATTERN = Pattern.compile("JSON\\.parse\\(decodeURIComponent\\(\"(.*)\"\\)\\);");

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36";

    /**
     * 登录语雀的cookie
     */
    private static String COOKIE;
    /**
     * 知识库主人的用户名
     */
    private static String USER;
    /**
     * 知识库的代称
     */
    private static String SLUG;
    /**
     * 知识库id
     */
    private static String BOOK_ID;
    /**
     * 当前知识库名称
     */
    private static String BOOK_NAME;
    /**
     * 知识库地址
     */
    private static String BOOK_URL;

    private static JSONObject BOOK_INFO;


    static {
        initApiCfg();
        initBookInfo();
    }

    public static void main(String[] args) throws IOException {
        pull(true);
    }

    public static void initApiCfg() {
        Properties config = ConfigReader.getConfig("config.properties");

        String cookie = config.getProperty("cookie");
        String bookUrl = config.getProperty("bookUrl");
        String macPath = config.getProperty("macPath");
        String winPath = config.getProperty("winPath");
        String customBookName = config.getProperty("customBookName");
        String basePath;
        if (cookie != null && bookUrl != null) {
            ExportMarkDown.COOKIE = cookie;
            ExportMarkDown.BOOK_URL = bookUrl;
            ExportMarkDown.BOOK_NAME = customBookName;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("mac")) {
                System.out.println("当前系统是: Mac OS");
                if (macPath == null) {
                    throw new RuntimeException("mac系统对应的保存路径未填写");
                }
                basePath = macPath;
            } else if (os.contains("win")) {
                System.out.println("当前系统是: Windows");
                if (winPath == null) {
                    throw new RuntimeException("Windows系统对应的保存路径未填写");
                }
                basePath = winPath;
            } else {
                System.out.println("当前系统不是Mac也不是Windows");
                throw new RuntimeException("当前系统不是Mac也不是Windows");
            }
            SAVE_PATH = basePath + BOOK_NAME + File.separator;

            Path path = Paths.get(SAVE_PATH);

            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            throw new RuntimeException("配置文件异常");
        }
    }

    private static void initBookInfo() {
        try {
            JSONObject bookInfo = getBookInfo(BOOK_URL);
            BOOK_INFO = bookInfo;
            JSONObject group = bookInfo.getJSONObject("group");
            JSONObject book = bookInfo.getJSONObject("book");
            String user = group.getString("login");
            String slug = book.getString("slug");
            String bookId = book.getString("id");
            String bookName = book.getString("name");

            ExportMarkDown.USER = user;
            ExportMarkDown.SLUG = slug;
            ExportMarkDown.BOOK_ID = bookId;

            System.out.println("知识库地址：" + BOOK_URL);
            System.out.println("知识库名称：" + bookName);
            System.out.println("知识库id：" + bookId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> getPathTree(JSONObject jsonObject) {
        JSONObject book = jsonObject.getJSONObject("book");

        JSONArray toc = book.getJSONArray("toc");

        List<Doc> docs = toc.toJavaList(Doc.class);
        Map<String, Doc> uuidMap = docs.stream().collect(Collectors.toMap(Doc::getUuid, Function.identity()));

        Map<String, String> pathMap = new HashMap<>();

        for (Doc doc : docs) {
            StringBuilder path = new StringBuilder();
            String parent_uuid = doc.getParent_uuid();
            while (uuidMap.containsKey(parent_uuid)) {
                Doc parentDoc = uuidMap.get(parent_uuid);
                path.insert(0, parentDoc.getTitle() + File.separator);
                parent_uuid = parentDoc.getParent_uuid();
            }
            pathMap.put(doc.getDoc_id(), path.toString());
        }

        return pathMap;
    }


    /**
     * 增量更新语雀文档
     *
     * @throws IOException 异常
     */
    public static void pull(boolean isAll) throws IOException {
        String time = findMaxFolder(SAVE_PATH, isAll);
        time = time == null ? DEFAULT_TIME : time;

        String lastUpdateTime = decodeTime(time);
        System.out.println("上次保存时间：" + lastUpdateTime);

        JSONArray docs = getDocs(BOOK_ID);

        Map<String, String> pathTree = getPathTree(BOOK_INFO);

        System.out.println("文档数量：" + docs.size());

        String deltaPath = encodeTime(LocalDateTime.now());
        deltaPath = isAll ? "all_" + deltaPath : "delta_" + deltaPath;
        int count = 0;
        int updateCount = 0;
        for (Object doc : docs) {
            long start = System.currentTimeMillis();
            JSONObject obj = (JSONObject) doc;
            String title = obj.getString("title");
            String slug = obj.getString("slug");
            String docId = obj.getString("id");

            String publisheAt = obj.getString("published_at");
            String updatedAt = obj.getString("updated_at");
            updatedAt = toUtc(updatedAt);
            if (updatedAt.compareTo(lastUpdateTime) > 0) {
                updateCount++;
            }

            if (publisheAt == null) {
                System.out.printf("%s ==> 页面无法访问，页面链接已失效或被删除%n", title);
                continue;
            }
            publisheAt = toUtc(publisheAt);

            if (!isAll) {
                if (publisheAt.compareTo(lastUpdateTime) <= 0) {
                    continue;
                }
            }

            // 替换特殊符号
            title = replace(title);
            System.out.printf("开始 ==> %s ", title);

            String url = getUrl(USER, ExportMarkDown.SLUG, slug);
            String one = getOne(url);

            String parentPath = SAVE_PATH + deltaPath + File.separator;
            String subFolderPath = pathTree.get(docId);
            if (subFolderPath != null && !subFolderPath.isEmpty()) {
                parentPath += subFolderPath;
            }

            Path pathMd = getPath(title, parentPath);

            Files.write(pathMd, Collections.singletonList(one),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            /*Path pathPdf = getPath(title, parentPath, ".pdf");

            Files.write(pathPdf, Collections.singletonList(one),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);*/

            sleep();
            count++;
            System.out.printf("完成耗时：%s%n", (System.currentTimeMillis() - start));
        }
        System.out.printf("全部处理完成 ==> 文档保存至目录：%s 发布文档数量：%d 更新文档数量：%d%n", deltaPath, count, updateCount);
    }


    private static Path getPath(String title, String parentPath) throws IOException {
        String fileName = title + ".md";

        String filePath = parentPath + fileName;
        Path path = Paths.get(filePath);
        // 文件目录不存在则创建目录
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        return path;
    }

    private static String findMaxFolder(String path, boolean isAll) {
        File folder = new File(path);
        if (!folder.isDirectory()) {
            return null;
        }
        File[] subFolders;
        if (isAll) {
            subFolders = folder.listFiles(item -> item.isDirectory() && item.getName().contains("all"));
        } else {
            subFolders = folder.listFiles(item -> item.isDirectory() && item.getName().contains("delta"));
        }

        if (subFolders == null || subFolders.length == 0) {
            return null;
        }
        Arrays.sort(subFolders, Comparator.comparing(File::getName).reversed());
        String name = subFolders[0].getName();
        return name.replace("all_", "").replace("delta_", "");
    }

    private static String replace(String s) {
        for (String invalidChar : INVALID_CHARS) {
            if (s.contains(invalidChar)) {
                // 替换无效字符
                s = s.replace(invalidChar, REPLACEMENT_CHAR);
            }
        }
        return s;
    }

    private static String getUrl(String user, String book, String docId) {
        return String.format(TEMPLATE, user, book, docId);
    }


    private static String getOne(String url) throws IOException {
        Map<String, String> header = ImmutableMap.of(
                "cookie", COOKIE
        );
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request.Builder builder = new Request.Builder()
                .url(url)
                .header("user-agent", USER_AGENT);
        addHeader(header, builder);

        Request request = builder.build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return response.body().string();
    }

    private static JSONArray getDocs(String bookId) throws IOException {
        String url = "https://www.yuque.com/api/docs?book_id=%s&only_order_by_id=true";

        url = String.format(url, bookId);

        Map<String, String> header = Collections.singletonMap("cookie", COOKIE);
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder()
                .url(url)
                .header("user-agent", USER_AGENT);

        addHeader(header, builder);

        Request request = builder.build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        String string = response.body().string();
        JSONObject jsonObject = JSON.parseObject(string);
        return jsonObject.getJSONArray("data");
    }

    private static JSONObject getBookInfo(String bookUrl) throws IOException {
        Map<String, String> header = Collections.singletonMap("cookie", COOKIE);
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(bookUrl)
                .header("user-agent", USER_AGENT);

        addHeader(header, builder);

        Request request = builder.build();

        String html;
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            html = response.body().string();
        }

        return getKeyWordInfo(html);

    }

    private static JSONObject getKeyWordInfo(String html) throws UnsupportedEncodingException {
        Matcher matcher = PATTERN.matcher(html);
        if (matcher.find()) {
            String group = matcher.group(1);
            String bookInfoJson = URLDecoder.decode(group, "UTF-8");
            return JSONObject.parseObject(bookInfoJson);
        } else {
            throw new RuntimeException("没有权限访问，或者其他异常");
        }
    }

    private static void addHeader(Map<String, String> header, Request.Builder builder) {
        header.forEach(builder::addHeader);
    }

    public static void sleep() {
        // 生成一个0-1000的随机数
        int sleepTime = RANDOM.nextInt(2000);

        // 让当前线程睡眠sleepTime毫秒
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            // ignore exception
        }
    }


    private static String decodeTime(String dateString) {
        LocalDateTime dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH_mm_ss_SSS"));

        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    private static String encodeTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH_mm_ss_SSS"));
    }

    private static String toUtc(String publishAt) {
        LocalDateTime dateTime = LocalDateTime.parse(publishAt, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        LocalDateTime localDateTime = dateTime.plusHours(8);
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    /**
     * {
     * "type":"DOC",
     * "title":"使用说明（必读）",
     * "uuid":"Rth2gUptOY8kaqRm",
     * "url":"nrdyiu",
     * "prev_uuid":"",
     * "sibling_uuid":"DcCfPcQNXdmVM9-g",
     * "child_uuid":"",
     * "parent_uuid":"",
     * "doc_id":78843297,
     * "level":0,
     * "id":78843297,
     * "open_window":1,
     * "visible":1
     * }
     */
    @Data
    public static class Doc {

        private String type;
        private String title;
        private String uuid;
        private String url;
        private String prev_uuid;
        private String sibling_uuid;
        private String child_uuid;
        private String parent_uuid;
        private String doc_id;
        private String level;
        private String id;
        private String open_window;
        private String visible;

    }

}
