package org.isite.data.controller;

import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.data.po.DataLogPo;
import org.isite.data.service.DataLogService;
import org.isite.data.support.dto.DataLogDto;
import org.isite.data.support.vo.DataLog;
import org.isite.jpa.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.converter.PageQueryConverter.toPageQuery;
import static org.isite.commons.cloud.utils.MessageUtils.getMessage;
import static org.isite.commons.lang.Assert.isFalse;
import static org.isite.data.support.constants.DataUrls.URL_DATA;

/**
 * @Description 数据接口日志Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class DataLogController extends BaseController {

	private DataLogService dataLogService;

	/**
	 * 执行完数据补偿，返回日志信息，如果返回空即为清除日志
	 */
    @PutMapping(URL_DATA + "/log/{logId}/retry")
	public Result<DataLog> retry(@PathVariable("logId") String logId) {
		DataLogPo logPo = dataLogService.get(logId);
		isFalse(logPo.getStatus(), getMessage("dataLog.compensate.failed", "Failed to compensate data"));
		return toResult(convert(dataLogService.retry(logPo), DataLog::new));
	}

	/**
	 * 删除日志
	 */
    @DeleteMapping(URL_DATA + "/log/{id}")
	public Result<Long> delete(@PathVariable("id") String id) {
		return toResult(dataLogService.delete(id));
	}

	/**
	 * 查询日志信息
	 */
	@GetMapping(URL_DATA + "/logs")
	public PageResult<DataLog> findPage(PageRequest<DataLogDto> request) {
		Page<DataLogPo> page = dataLogService.findPage(toPageQuery(request, DataLogPo::new));
		return toPageResult(request, convert(page.getResult(), DataLog::new), page.getTotal());
	}

	@Autowired
	public void setDataLogService(DataLogService dataLogService) {
		this.dataLogService = dataLogService;
	}
}