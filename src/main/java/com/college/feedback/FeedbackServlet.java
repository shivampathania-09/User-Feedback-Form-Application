package com.college.feedback;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FeedbackServlet handling feedback submissions and displaying summary dashboard.
 * Supports both POST and GET HTTP methods for viva evaluation.
 * Compatible with Jakarta EE 9/10 / Tomcat 10+.
 */
@WebServlet(name = "FeedbackServlet", urlPatterns = {"/FeedbackServlet", "/submit-feedback"})
public class FeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Thread-safe static in-memory store for feedback entries
    private static final List<Feedback> feedbackList = Collections.synchronizedList(new ArrayList<>());

    // Thread-safe auto-incrementing ID counter
    private static final AtomicInteger idCounter = new AtomicInteger(1);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Extract form parameters
        String name = getParameter(request, "name", getParameter(request, "fullName", "Anonymous"));
        String email = getParameter(request, "email", "N/A");
        String category = getParameter(request, "category", "General");
        String ratingStr = getParameter(request, "rating", "5");
        String comments = getParameter(request, "comments", "No comments provided.");

        int rating = parseRating(ratingStr);
        String timestamp = LocalDateTime.now().format(FORMATTER);

        // Construct new Feedback model object marked with POST
        Feedback feedback = new Feedback(
                idCounter.getAndIncrement(),
                name,
                email,
                category,
                rating,
                comments,
                timestamp,
                "POST"
        );

        // Store in thread-safe collection
        feedbackList.add(feedback);

        // Render response
        renderResponsePage(response, feedback, "POST", false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String name = request.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            name = request.getParameter("fullName");
        }
        String email = request.getParameter("email");

        // If name & email are present, process as a GET submission (viva demonstration)
        if (name != null && !name.trim().isEmpty() && email != null && !email.trim().isEmpty()) {
            String category = getParameter(request, "category", "General");
            String ratingStr = getParameter(request, "rating", "5");
            String comments = getParameter(request, "comments", "No comments provided.");

            int rating = parseRating(ratingStr);
            String timestamp = LocalDateTime.now().format(FORMATTER);

            // Construct new Feedback model object marked with GET
            Feedback feedback = new Feedback(
                    idCounter.getAndIncrement(),
                    name.trim(),
                    email.trim(),
                    category,
                    rating,
                    comments,
                    timestamp,
                    "GET"
            );

            // Store in thread-safe collection
            feedbackList.add(feedback);

            // Render response with confirmation card
            renderResponsePage(response, feedback, "GET", false);
        } else {
            // Direct visit to /FeedbackServlet without submission parameters -> Show Dashboard
            renderResponsePage(response, null, "GET", true);
        }
    }

    /**
     * Renders modern HTML output matching styles.css glassmorphic design.
     */
    private void renderResponsePage(HttpServletResponse response, Feedback latestFeedback, 
                                    String currentMethod, boolean isDashboardViewOnly) 
            throws IOException {
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"en\">");
            out.println("<head>");
            out.println("    <meta charset=\"UTF-8\">");
            out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("    <title>" + (isDashboardViewOnly ? "Feedback Dashboard" : "Feedback Submitted Successfully") + "</title>");
            out.println("    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">");
            out.println("    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>");
            out.println("    <link href=\"https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap\" rel=\"stylesheet\">");
            out.println("    <link rel=\"stylesheet\" href=\"css/styles.css\">");
            out.println("</head>");
            out.println("<body>");
            out.println("    <div class=\"app-container app-container-wide\">");

            // Confirmation Card (Shown when a feedback is submitted)
            if (latestFeedback != null) {
                out.println("        <div class=\"feedback-card success-card\">");
                out.println("            <div class=\"success-badge\">");
                out.println("                <span class=\"badge-icon\">✓</span>");
                out.println("            </div>");
                out.println("            <h1 class=\"card-title\">Feedback Recorded Successfully!</h1>");
                out.println("            <p class=\"card-subtitle\">Thank you, <strong>" + escapeHtml(latestFeedback.getName()) + "</strong>. Your response has been saved in server memory.</p>");

                // Prominent Method Badge Indicator
                out.println("            <div class=\"method-status-banner\">");
                out.println("                <span class=\"banner-label\">Processed Via:</span>");
                if ("POST".equalsIgnoreCase(latestFeedback.getSubmissionMethod())) {
                    out.println("                <span class=\"method-badge-pill post-pill\">HTTP POST (Payload Transmitted in Body)</span>");
                } else {
                    out.println("                <span class=\"method-badge-pill get-pill\">HTTP GET (Payload Transmitted via URL Query String)</span>");
                }
                out.println("            </div>");

                // Summary details grid
                out.println("            <div class=\"summary-container\">");
                out.println("                <div class=\"summary-item\">");
                out.println("                    <span class=\"label\">Feedback ID</span>");
                out.println("                    <span class=\"value\">#" + latestFeedback.getId() + "</span>");
                out.println("                </div>");
                out.println("                <div class=\"summary-item\">");
                out.println("                    <span class=\"label\">Full Name</span>");
                out.println("                    <span class=\"value\">" + escapeHtml(latestFeedback.getName()) + "</span>");
                out.println("                </div>");
                out.println("                <div class=\"summary-item\">");
                out.println("                    <span class=\"label\">Email Address</span>");
                out.println("                    <span class=\"value\">" + escapeHtml(latestFeedback.getEmail()) + "</span>");
                out.println("                </div>");
                out.println("                <div class=\"summary-item\">");
                out.println("                    <span class=\"label\">Category</span>");
                out.println("                    <span class=\"value\"><span class=\"badge-tag\">" + escapeHtml(latestFeedback.getCategory()) + "</span></span>");
                out.println("                </div>");
                out.println("                <div class=\"summary-item\">");
                out.println("                    <span class=\"label\">Rating Score</span>");
                out.println("                    <span class=\"value stars-value\">" + renderStars(latestFeedback.getRating()) + " (" + latestFeedback.getRating() + "/5)</span>");
                out.println("                </div>");
                out.println("                <div class=\"summary-item\">");
                out.println("                    <span class=\"label\">Submission Time</span>");
                out.println("                    <span class=\"value\">" + latestFeedback.getTimestamp() + "</span>");
                out.println("                </div>");
                out.println("                <div class=\"summary-item full-width\">");
                out.println("                    <span class=\"label\">Comments &amp; Observations</span>");
                out.println("                    <p class=\"value comments-text\">" + escapeHtml(latestFeedback.getComments()) + "</p>");
                out.println("                </div>");
                out.println("            </div>");

                out.println("            <div class=\"actions-row\">");
                out.println("                <a href=\"index.html\" class=\"submit-btn primary-btn action-link\">Submit Another Feedback</a>");
                out.println("            </div>");
                out.println("        </div>");
            } else {
                // Standalone Dashboard Header
                out.println("        <div class=\"feedback-card\">");
                out.println("            <header class=\"card-header\">");
                out.println("                <span class=\"badge-pill\">Server Dashboard</span>");
                out.println("                <h1 class=\"card-title\">Feedback Submissions Summary</h1>");
                out.println("                <p class=\"card-subtitle\">Live view of in-memory feedback recorded during the active Tomcat session.</p>");
                out.println("            </header>");
                out.println("            <div class=\"actions-row text-center\">");
                out.println("                <a href=\"index.html\" class=\"submit-btn primary-btn action-link\">+ Open Feedback Form</a>");
                out.println("            </div>");
                out.println("        </div>");
            }

            // Summary Data Table Card
            out.println("        <div class=\"feedback-card table-card\">");
            out.println("            <div class=\"table-header-row\">");
            out.println("                <div class=\"table-title-wrap\">");
            out.println("                    <h2 class=\"table-title\">All In-Memory Feedback Records</h2>");
            out.println("                    <span class=\"record-count\">Total Submissions: " + feedbackList.size() + "</span>");
            out.println("                </div>");
            out.println("                <a href=\"index.html\" class=\"mini-link\">+ New Entry</a>");
            out.println("            </div>");

            if (feedbackList.isEmpty()) {
                out.println("            <div class=\"empty-table-state\">");
                out.println("                <p class=\"empty-icon\">📭</p>");
                out.println("                <p class=\"empty-msg\">No feedback submissions received yet in this server session.</p>");
                out.println("                <a href=\"index.html\" class=\"submit-btn primary-btn action-link mini-btn\">Go To Form</a>");
                out.println("            </div>");
            } else {
                out.println("            <div class=\"table-responsive\">");
                out.println("                <table class=\"data-table\">");
                out.println("                    <thead>");
                out.println("                        <tr>");
                out.println("                            <th>ID</th>");
                out.println("                            <th>Method</th>");
                out.println("                            <th>Name</th>");
                out.println("                            <th>Email</th>");
                out.println("                            <th>Category</th>");
                out.println("                            <th>Rating</th>");
                out.println("                            <th>Comments</th>");
                out.println("                            <th>Timestamp</th>");
                out.println("                        </tr>");
                out.println("                    </thead>");
                out.println("                    <tbody>");

                // Iterate over synchronizedList safely
                synchronized (feedbackList) {
                    for (Feedback fb : feedbackList) {
                        String isLatestClass = (latestFeedback != null && fb.getId() == latestFeedback.getId()) ? " class=\"highlighted-row\"" : "";
                        out.println("                        <tr" + isLatestClass + ">");
                        out.println("                            <td class=\"id-cell\">#" + fb.getId() + "</td>");
                        if ("POST".equalsIgnoreCase(fb.getSubmissionMethod())) {
                            out.println("                            <td><span class=\"method-tag-small post-tag\">POST</span></td>");
                        } else {
                            out.println("                            <td><span class=\"method-tag-small get-tag\">GET</span></td>");
                        }
                        out.println("                            <td class=\"font-medium\">" + escapeHtml(fb.getName()) + "</td>");
                        out.println("                            <td class=\"email-cell\">" + escapeHtml(fb.getEmail()) + "</td>");
                        out.println("                            <td><span class=\"badge-tag\">" + escapeHtml(fb.getCategory()) + "</span></td>");
                        out.println("                            <td class=\"stars-cell\">" + renderStars(fb.getRating()) + "</td>");
                        out.println("                            <td class=\"comment-cell\" title=\"" + escapeHtml(fb.getComments()) + "\">" + escapeHtml(fb.getComments()) + "</td>");
                        out.println("                            <td class=\"time-cell\">" + fb.getTimestamp() + "</td>");
                        out.println("                        </tr>");
                    }
                }

                out.println("                    </tbody>");
                out.println("                </table>");
                out.println("            </div>");
            }

            out.println("        </div>"); // table-card end
            out.println("    </div>");     // app-container end
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String val = request.getParameter(name);
        return (val != null && !val.trim().isEmpty()) ? val.trim() : defaultValue;
    }

    private int parseRating(String ratingStr) {
        try {
            int r = Integer.parseInt(ratingStr);
            return (r >= 1 && r <= 5) ? r : 5;
        } catch (NumberFormatException e) {
            return 5;
        }
    }

    private String renderStars(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                sb.append("★");
            } else {
                sb.append("☆");
            }
        }
        return sb.toString();
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
