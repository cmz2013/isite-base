package org.isite.user.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vip")
public class VipPo extends Po<Long> {

    private Long userId;
    private Date expireTime;
}
