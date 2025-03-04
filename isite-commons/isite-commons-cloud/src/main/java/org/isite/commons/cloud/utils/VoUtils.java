package org.isite.commons.cloud.utils;

import org.isite.commons.cloud.data.vo.Vo;

import java.util.List;
/**
 * @Author <font color='blue'>zhangcm</font>
 */
public class VoUtils {

    private VoUtils() {
    }

    /**
     * 根据id筛选VO
     */
    public static <V extends Vo<I>, I> V get(List<V> vos, I id) {
        if (null == id) {
            return null;
        }
        for (V vo : vos) {
            if (vo.getId().equals(id)) {
                return vo;
            }
        }
        return null;
    }

    public static <V extends Vo<I>, I> boolean contain(List<V> vos, I id) {
        if (null == id) {
            return Boolean.FALSE;
        }
        for (V vo : vos) {
          if (id.equals(vo.getId())) {
              return Boolean.TRUE;
          }
        }
        return Boolean.FALSE;
    }
}
