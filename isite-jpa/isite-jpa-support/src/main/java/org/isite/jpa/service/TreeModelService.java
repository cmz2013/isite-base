package org.isite.jpa.service;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.isite.commons.lang.Assert;
import org.isite.commons.lang.Constants;
import org.isite.commons.lang.Reflection;
import org.isite.commons.lang.utils.TypeUtils;
import org.isite.jpa.data.TreeModel;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
@Getter
public abstract class TreeModelService<P extends TreeModel<I>, I, N extends Number> extends BaseService<P, I, N> {

    protected TreeModelService() {
        super();
    }

    /**
     * 如果如果的泛型参数<P>,在子类依然使用泛型参数，则需要重新该方法
     */
    @Override
    protected Class<P> initPoClass() {
        return TypeUtils.cast(Reflection.getGenericParameter(this.getClass(), TreeModelService.class));
    }

    /**
     * 根据父ID获取pids
     */
    public String getPids(I pid) {
        if (isRoot(pid)) {
            return Constants.BLANK_STR;
        }
        P po = get(pid);
        Assert.notNull(po, "id not found: " + pid);
        return getPids(po);
    }

    /**
     * 根据父节点获取pids
     */
    public String getPids(P parent) {
        return StringUtils.isBlank(parent.getPids()) ? parent.getId().toString() :
                parent.getPids() + Constants.COMMA + parent.getId();
    }

    /**
     * 根据pids右模糊查询
     */
    public abstract List<P> findLikePids(String pids);

    /**
     * 根据pid是判断该节点是否为根节点
     */
    public boolean isRoot(I pid) {
        return null == pid || pid.equals(Constants.ZERO) || Constants.BLANK_STR.equals(pid);
    }
}
