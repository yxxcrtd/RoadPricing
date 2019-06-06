package com.igoosd.rps.controller;

import com.igoosd.common.Assert;
import com.igoosd.common.enums.UserTypeEnum;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.HashKit;
import com.igoosd.model.TUser;
import com.igoosd.rps.service.UserService;
import com.igoosd.rps.util.SessionUser;
import com.igoosd.rps.util.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 2018/1/17.
 */
@Api(tags = "登录登出接口")
@RestController
public class LoginController {

    @Autowired
    private WebUtils webUtils;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录接口", notes = "loginName(必填)、password(必填)、remember（必填，复选框 true/false）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "登录名", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "remember", value = "记住我", required = true, dataType = "boolean", paramType = "form"),
    })
    @PostMapping("/login")
    public ResultMsg login(HttpServletRequest request, HttpServletResponse response, String loginName, String password, boolean remember) {

        Assert.hasText(loginName, "用户名不能为空");
        Assert.hasText(password, "密码不能为空");

        TUser tempUser = new TUser();
        tempUser.setLoginName(loginName);
        tempUser.setDelete(false);
        List<TUser> list = userService.findList(tempUser);
        if (CollectionUtils.isEmpty(list)) {
            return ResultMsg.resultFail("用户名或密码错误");
        }
        TUser user = list.get(0);
        if (!HashKit.md5(password + user.getSalt()).equals(user.getPassword())) {
            return ResultMsg.resultFail("用户名或密码错误");
        }
        UserTypeEnum userTypeEnum = UserTypeEnum.getUserTypeEnumByVaule(user.getUserType());
        Assert.notNull(userTypeEnum,"不合法的用户类型");
        // 验证码校验...暂时不做
        //封装LoginUser
        SessionUser sessionUser = new SessionUser();
        sessionUser.setLoginName(user.getLoginName());
        sessionUser.setUserName(user.getUserName());
        sessionUser.setIp(webUtils.getIP(request));
        sessionUser.setSex(user.getSex());
        sessionUser.setPhone(user.getPhone());
        sessionUser.setPermissions(userTypeEnum.getPermissions());
        //session cookie 设置
        webUtils.loginUser(request, response, sessionUser, remember);
        return ResultMsg.resultSuccess(sessionUser);
    }

    @ApiOperation(value = "登出")
    @PostMapping("/logout")
    public ResultMsg logout(HttpServletRequest request, HttpServletResponse response) {
        webUtils.logoutUser(request, response);
        return ResultMsg.resultSuccess();
    }
}
