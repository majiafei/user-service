package com.ruanmou.house.exception;

import com.ruanmou.house.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * api的异常控制器
 * @ProjectName: house
 * @Package: com.runmou.house.common.housecommon.exception
 * @ClassName: ApiExceptionController
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/17 10:51
 */
@Controller
public class ApiExceptionController implements ErrorController {

    // 错误路径
    public static final String  ERROR_PATH = "/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    public ApiExceptionController() {
        System.out.println("=================");
    }

    /**
     * 根据错误码跳转到指定的错误界面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public String error(HttpServletRequest request, HttpServletResponse response){
        int status = response.getStatus();

        // 判断状态码
        switch (status) {
            case 403:
                return "403";
            case 404:
                return "404";
            case 405:
                return "405";
            case 500:
                return "500";
        }


        return "index";
    }

    @RequestMapping(ERROR_PATH)
    @ResponseBody
    public ApiResponse error1(HttpServletRequest request, HttpServletResponse response){
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributeMap = this.errorAttributes.getErrorAttributes(webRequest, false);
        int status = getStatus(request);

        return ApiResponse.ofMessage(status, (String) errorAttributeMap.getOrDefault("message", "失败"));
    }

    private int getStatus(HttpServletRequest request) {
        Integer status = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if (status == null) {
            return 500;
        }

        return status;

    }

}
