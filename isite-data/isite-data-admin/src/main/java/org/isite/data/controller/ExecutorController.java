package org.isite.data.controller;

import org.isite.commons.cloud.data.dto.PageRequest;
import org.isite.commons.cloud.data.vo.PageResult;
import org.isite.commons.cloud.data.vo.Result;
import org.isite.commons.web.controller.BaseController;
import org.isite.data.po.ExecutorPo;
import org.isite.data.service.ExecutorService;
import org.isite.data.support.dto.ExecutorDto;
import org.isite.data.support.vo.Executor;
import org.isite.data.support.vo.Host;
import org.isite.jpa.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.isite.commons.cloud.converter.DataConverter.convert;
import static org.isite.commons.cloud.converter.PageQueryConverter.toPageQuery;
import static org.isite.data.converter.HostConverter.toHosts;
import static org.isite.data.support.constants.UrlConstants.URL_DATA;

/**
 * @Description 执行器Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class ExecutorController extends BaseController {
    /**
     * 执行器Service
     */
    private ExecutorService executorService;
    /**
     * 服务发现客户端
     */
    private DiscoveryClient discoveryClient;

    /**
     * 查询执行器
     */
    @GetMapping(URL_DATA + "/executors")
    public PageResult<Executor> findPage(PageRequest<ExecutorDto> request) {
        Page<ExecutorPo> page = executorService.findPage(toPageQuery(request, ExecutorPo::new));
        return toPageResult(request, convert(page.getResult(), Executor::new), page.getTotal());
    }

    /**
     * 查询服务器
     */
    @GetMapping(URL_DATA + "/executor/{serviceId}")
    public Result<List<Host>> findServer(@PathVariable("serviceId") String serviceId) {
        return toResult(toHosts(discoveryClient.getInstances(serviceId)));
    }

    @Autowired
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Autowired
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }
}
