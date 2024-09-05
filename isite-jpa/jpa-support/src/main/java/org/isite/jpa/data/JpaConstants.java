package org.isite.jpa.data;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class JpaConstants {

    private JpaConstants() {
    }

    public static final String FIELD_ID = "id";
    public static final String FIELD_PIDS = "pids";
    public static final String FIELD_CREATE_TIME = "createTime";
    public static final String FIELD_UPDATE_TIME = "updateTime";
    public static final String FIELD_INTERNAL = "internal";

    public static final String INTERNAL_DATA_ILLEGAL_DELETE = "internal data can not be deleted";
    public static final String INTERNAL_DATA_ILLEGAL_UPDATE = "internal data can not be updated";
    public static final String INTERNAL_DATA_ILLEGAL_INSERTED = "internal data can not be inserted";
}
