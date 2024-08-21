package medical_record;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*") // すべてのリクエストをフィルタリング
public class SessionFilter implements Filter {

    public void init(FilterConfig fConfig) throws ServletException {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String loginURI = httpRequest.getContextPath() + "/login.jsp";
        boolean loggedIn = (session != null && session.getAttribute("username") != null);
        boolean loginRequest = httpRequest.getRequestURI().equals(loginURI);

        if (loggedIn || loginRequest || httpRequest.getRequestURI().contains("LoginServlet")) {
            chain.doFilter(request, response); // ログイン済みまたはログインリクエストの場合は次のフィルタまたはサーブレットに進む
        } else {
            httpResponse.sendRedirect(loginURI); // ログインページにリダイレクト
        }
    }

    public void destroy() {}
}
