/**
 * @Description 用于DTO字段校验的分组接口
 * @see org.springframework.validation.annotation.Validated
 * bean 字段默认都是 Default 分组，@Validated 在分组验证时并没有添加 Default.class 的分组。
 * 在 bean 属性加上 group 后，出现其他非 group 字段不执行校验，除非分组接口继承 Default。
 * @Author <font color='blue'>zhangcm</font>
 */
package org.isite.commons.cloud.data.op;