package org.isite.operation.controller;

import com.github.pagehelper.Page;
import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.web.controller.BaseController;
import org.isite.operation.po.InviteRecordPo;
import org.isite.operation.service.InviteRecordService;
import org.isite.operation.support.dto.InviteRecordDto;
import org.isite.operation.support.vo.InviteRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.converter.PageQueryConverter.toPageQuery;
import static org.isite.commons.cloud.data.constants.UrlConstants.URL_MY;
import static org.isite.operation.converter.InviteRecordConverter.toInviteRecordSelectivePo;
import static org.isite.operation.support.constants.UrlConstants.URL_OPERATION;

/**
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class InviteRecordController extends BaseController {

    private InviteRecordService inviteRecordService;

    /**
     * 查询邀请记录（用于管理后台查询数据）
     */
    @GetMapping(URL_OPERATION + "/invite/records")
    public PageResult<InviteRecord> findPage(PageRequest<InviteRecordDto> request) {
        try (Page<InviteRecordPo> page = inviteRecordService.findPage(toPageQuery(request, InviteRecordPo::new))) {
            return toPageResult(request, convert(page.getResult(), InviteRecord::new), page.getTotal());
        }
    }

    /**
     * 查询当前用户邀请记录（用于用户查询数据）
     */
    @GetMapping(URL_MY + URL_OPERATION + "/activity/{activityId}/invite/records")
    public PageResult<InviteRecord> findPage(
            @PathVariable("activityId") Integer activityId, PageRequest<?> request) {
        try (Page<InviteRecordPo> page = inviteRecordService.findPage(
                toPageQuery(request, () -> toInviteRecordSelectivePo(activityId)))) {
            return toPageResult(request, convert(page.getResult(), InviteRecord::new), page.getTotal());
        }
    }

    @Autowired
    public void setInviteRecordService(InviteRecordService inviteRecordService) {
        this.inviteRecordService = inviteRecordService;
    }
}
