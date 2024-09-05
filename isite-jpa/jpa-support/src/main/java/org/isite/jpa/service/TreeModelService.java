package org.isite.jpa.service;

import lombok.Getter;
import org.isite.jpa.data.TreeModel;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.isite.commons.lang.Assert.notNull;
import static org.isite.commons.lang.Reflection.getGenericParameter;
import static org.isite.commons.lang.Constants.BLANK_STRING;
import static org.isite.commons.lang.Constants.COMMA;
import static org.isite.commons.lang.Constants.ZERO;
import static org.isite.commons.lang.utils.TypeUtils.cast;
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
        return cast(getGenericParameter(this.getClass(), TreeModelService.class));
    }

    /**
     * 根据父ID获取pids
     */
    public String getPids(I pid) {
        if (isRoot(pid)) {
            return BLANK_STRING;
        }
        P po = get(pid);
        notNull(po, "id not found: " + pid);
        return getPids(po);
    }

    /**
     * 根据父节点获取pids
     */
    public String getPids(P parent) {
        return isBlank(parent.getPids()) ? parent.getId().toString() : parent.getPids() + COMMA + parent.getId();
    }

    /**
     * 根据pids右模糊查询
     */
    public abstract List<P> findLikePids(String pids);

    /**
     * 根据pid是判断该节点是否为根节点
     */
    public boolean isRoot(I pid) {
        return null == pid || pid.equals(ZERO) || BLANK_STRING.equals(pid);
    }
}
