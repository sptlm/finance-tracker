package kfu.itis.maslennikov.financetracker.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class FlashMessageFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        
        chain.doFilter(req, resp);

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute("successMessage");
            session.removeAttribute("errorMessage");
        }
    }
}