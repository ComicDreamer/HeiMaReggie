package cuculi.Filter;

/*
组织用户未登录时访问资源
 */

import com.alibaba.fastjson.JSON;
import cuculi.common.BaseContext;
import cuculi.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    /*
    1. 获取用户访问的url
    2. 检查url是否需要限制访问，如果不限制访问，放行
    3. 检查用户是否已经登录，如果已经登录，放行
    4. 否则阻止访问，返回阻止R.error()
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
            String url = httpServletRequest.getRequestURI();

            log.info("拦截到请求：{}", url);

            String[] availableUrls = {
              "/employee/login",
              "/employee/logout",
                    "/common/**",
              "/backend/**",
              "/front/**",
                    "/user/login",
                    "/user/sendMessage"
            };

            for (String availableUrl : availableUrls){
                if (PATH_MATCHER.match(availableUrl, url)){
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }
            }

            if (httpServletRequest.getSession().getAttribute("employee") != null){
                BaseContext.setCurrentId((Long) httpServletRequest.getSession().getAttribute("employee"));
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            if (httpServletRequest.getSession().getAttribute("user") != null){
                BaseContext.setCurrentId((Long) httpServletRequest.getSession().getAttribute("user"));
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            log.info("拒绝访问");
            return;
    }
}
