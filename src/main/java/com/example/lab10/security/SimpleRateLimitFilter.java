package com.example.lab10.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/*
 * This filter limits how many times a client can POST
 * to /login and /register in a short time.
 */
@Component
public class SimpleRateLimitFilter extends OncePerRequestFilter {

    // Max number of allowed requests in the time window
    private static final int LIMIT = 4;

    // Time window in seconds
    private static final long WINDOW_SECONDS = 60;

    /*
     * Map that stores request timestamps per IP address.
     */
    private final Map<String, Deque<Long>> hits = new ConcurrentHashMap<>();

    /*
     * Decide when this filter should NOT run.
     * I only want to rate-limit:
     * - POST /login
     * - POST /register
     * Everything else is ignored.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!HttpMethod.POST.matches(request.getMethod())) return true;

        String path = request.getRequestURI();
        return !(path.equals("/login") || path.equals("/register"));
    }

    /*
     * Main filter logic.
     * This runs once per request and checks if the client
     * exceeded the allowed number of attempts.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        // Identify client by IP address
        String ip = request.getRemoteAddr();

        // Current time in seconds
        long now = Instant.now().getEpochSecond();

        // Get or create the queue of timestamps for this IP
        Deque<Long> q = hits.computeIfAbsent(ip, k -> new ConcurrentLinkedDeque<>());

        /*
         * Removes timestamps that are outside the time window.
         * This keeps only recent requests.
         */
        while (true) {
            Long head = q.peekFirst();
            if (head == null) break;
            if (now - head > WINDOW_SECONDS) q.pollFirst();
            else break;
        }

        /*
         * If the limit is reached, block the request.
         */
        if (q.size() >= LIMIT) {
            response.setStatus(429); // Too Many Requests
            response.setHeader("Retry-After", String.valueOf(WINDOW_SECONDS));

            // Pass info to the rate-limit error page
            request.setAttribute("statusCode", 429);
            request.setAttribute("message", "Too Many Requests - rate limit triggered. Please wait and try again.");
            request.setAttribute("retryAfterSeconds", WINDOW_SECONDS);

            // Forward to a friendly error page
            request.getRequestDispatcher("/rate-limit").forward(request, response);
            return;
        }

        // Register this request timestamp
        q.addLast(now);

        // Continue with the normal filter chain
        chain.doFilter(request, response);
    }
}