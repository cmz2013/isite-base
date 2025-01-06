package org.isite.user.po;

import lombok.Getter;
import lombok.Setter;
import org.isite.mybatis.data.Po;

import javax.persistence.Table;
import java.util.Date;

/**
 * @Description VIP会员(通过购买成为VIP)
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
@Setter
//@AllArgsConstructor 不建议使用，因为字段顺序一旦调整，构造函数传参就会错误，如果类型匹配很容易忽略该错误
@Table(name = "vip")
public class VipPo extends Po<Long> {

    private Long userId;
    private Date expireTime;

    public VipPo() {
        super();
    }

    public VipPo(Long userId, Date expireTime) {
        super();
        this.userId = userId;
        this.expireTime = expireTime;
    }
}
