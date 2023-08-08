package com.star.webssh.filtter;

import com.alibaba.fastjson.JSONObject;

import com.star.webssh.common.JWTUtils;
import com.star.webssh.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mr.Wang
 * @create 2023-05-24-11:25
 */
@WebFilter(urlPatterns = "/*")
@Slf4j
public class loginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //将request,response转换
        HttpServletRequest httpServletRequest= (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse= (HttpServletResponse) servletResponse;

        //获取url
         String url = httpServletRequest.getRequestURL().toString();
        if(url.contains("/login")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        //从cookie中拿到token

        //===============================
        Cookie[] cookies = httpServletRequest.getCookies();


        if (cookies == null || cookies.length == 0) {
            return ;
        }
        String jwt=null;
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if (name.equals("Admin-Token")) {
                jwt = cookie.getValue();
                break;
            }
        }

        //====================================================
//        String jwt = httpServletRequest.getHeader("Token");


        if(!StringUtils.hasLength(jwt)){
            log.info("密钥为：{}",jwt);
            R error = R.error("NOT_LOGIN");
            //将Result转为json将其相应回去
            String jsonString = JSONObject.toJSONString(error);
            servletResponse.getWriter().write(jsonString);
            return;
        }
        //有密钥，看是否能解析成功
        try {
            JWTUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            R error = R.error("NOT_LOGIN");
            //将Result转为json将其相应回去
            String jsonString = JSONObject.toJSONString(error);
            servletResponse.getWriter().write(jsonString);
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
