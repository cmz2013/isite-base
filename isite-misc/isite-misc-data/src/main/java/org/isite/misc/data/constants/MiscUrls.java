package org.isite.misc.data.constants;

import static org.isite.commons.cloud.data.constants.UrlConstants.URL_MY;
import static org.isite.commons.cloud.data.constants.UrlConstants.URL_PUBLIC;

/**
 * @Description URL常量
 * url常量命名规则约定：API_/MY_/PUBLIC_ + HTTP Method + 资源Path
 * @Author <font color='blue'>zhangcm</font>
 */
public class MiscUrls {

    private MiscUrls() {
    }

    public static final String URL_MISC = "/misc";
    /**
     * 公共接口：根据父节点ID查询地区
     */
    public static final String PUBLIC_GET_REGIONS = URL_PUBLIC + URL_MISC + "/regions/{pid}";
    /**
     * 公共接口：查询所有省份
     */
    public static final String PUBLIC_GET_PROVINCES = URL_PUBLIC + URL_MISC + "/provinces";
    /**
     * 添加文件记录，返回ID
     */
    public static final String MY_POST_FILE_RECORD = URL_MY + URL_MISC + "/file/record";
    /**
     * 根据ID更新文件记录，返回更新条数
     */
    public static final String PUT_FILE_RECORD = URL_MISC + "/file/record";
    /**
     * 添加标签记录
     */
    public static final String POST_TAG_RECORD = URL_MISC + "/tag/record";
}
