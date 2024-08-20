package org.isite.data.controller;

import org.isite.commons.web.controller.BaseController;
import org.isite.commons.cloud.data.PageRequest;
import org.isite.commons.cloud.data.PageResult;
import org.isite.tenant.data.dto.EmployeeDto;
import org.isite.tenant.data.vo.Employee;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static org.isite.data.support.constants.UrlConstants.URL_DATA;

/**
 * @Description 用户管理Controller
 * @Author <font color='blue'>zhangcm</font>
 */
@RestController
public class UserController extends BaseController {
    /**
     * 查询用户列表
     */
    @GetMapping(URL_DATA + "/users")
    public PageResult<Employee> findPage(PageRequest<EmployeeDto> request) {
        return toPageResult(request, new ArrayList<Employee>(), 0);
    }
}
