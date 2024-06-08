package com.github.pluto.boot.web.filter;

import com.alibaba.fastjson2.JSON;
import io.micrometer.core.instrument.util.IOUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author ：L1nker4
 * @date ： 创建于  2024/5/24 20:52
 */
@Slf4j
@Component
public class HttpTraceLogFilter extends OncePerRequestFilter implements Ordered {

    private static final String NEED_TRACE_PATH_PREFIX = "/api/v1";
    private static final String IGNORE_CONTENT_TYPE = "multipart/form-data";

    public HttpTraceLogFilter() {

    }


    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 10;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!isRequestValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
            status = response.getStatus();
        } finally {
            String path = request.getRequestURI();
            if (path.startsWith(NEED_TRACE_PATH_PREFIX) && !Objects.equals(IGNORE_CONTENT_TYPE, request.getContentType())) {
                HttpTraceLog traceLog = new HttpTraceLog();
                traceLog.setPath(path);
                traceLog.setMethod(request.getMethod());
                long latency = System.currentTimeMillis() - startTime;
                traceLog.setTimeTaken(latency);
                traceLog.setTime(LocalDateTime.now().toString());
                traceLog.setStatus(status);
                traceLog.setRequest(getRequestBody(request));
                traceLog.setResponse(getResponseBody(response));
                log.info("Http trace log: {}", JSON.toJSONString(traceLog));
            }
            updateResponse(response);
        }
    }

    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    private String getRequestBody(HttpServletRequest request) {
        String requestBody = "";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            requestBody = wrapper.getContentAsString();
        }
        return requestBody;
    }

    private String getResponseBody(HttpServletResponse response) {
        String responseBody = "";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            responseBody = IOUtils.toString(wrapper.getContentInputStream(), StandardCharsets.UTF_8);
        }
        return responseBody;
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }


    @Data
    private static class HttpTraceLog {
        private String path;
        private String method;
        private Long timeTaken;
        private String time;
        private Integer status;
        private String request;
        private String response;
    }


}
