package kfu.itis.maslennikov.financetracker.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@WebFilter(urlPatterns = "/*")
public class LoggingFilter extends HttpFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ServletContext servletContext;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Map<String, String[]> params = req.getParameterMap();

        if (params != null) {
            String paramStr = params.entrySet().stream().map(
                    e -> e.getKey() + "=" + Arrays.toString(e.getValue())
            ).collect(Collectors.joining(", ", "{", "}"));

            this.servletContext.log("from context " + req.getRemoteAddr() + " : request params: " + paramStr);

            LOGGER.info("{}: request params : {}", req.getRemoteAddr(), paramStr);
        }
        chain.doFilter(req,res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.servletContext = filterConfig.getServletContext();
        this.servletContext.log("LoggingFilter initialized");
    }
}
