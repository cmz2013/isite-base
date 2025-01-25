package org.isite.tenant.client;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class EmployeeAccessor {

    /**
     * 查询一个发放薪资的员工ID，员工ID除以shardTotal取余，如果余数为shardIndex，则返回该员工ID
     */
    public static Long getEmployeeId(int shardIndex, int shardTotal, long minId) {
        return 0L; //TODO
    }
}
