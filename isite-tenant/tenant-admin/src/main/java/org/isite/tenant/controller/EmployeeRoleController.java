package org.isite.tenant.controller;

import org.isite.commons.web.controller.BaseController;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 给员工设置角色。Administrator 角色的员工，至少要有一个，不能都删除
 */
@RestController
public class EmployeeRoleController extends BaseController {
}
