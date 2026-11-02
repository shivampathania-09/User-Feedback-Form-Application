package com.college.feedback;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model representing a user's feedback submission.
 */
public class FeedbackBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName;
    private String email;
    private String category;
    private int rating;
    private String comments;
    private boolean recommend;
    private LocalDateTime submissionTime;

    public FeedbackBean() {
        this.submissionTime = LocalDateTime.now();
    }

    public FeedbackBean(String fullName, String email, String category, int rating, String comments, boolean recommend) {
        this.fullName = fullName;
        this.email = email;
        this.category = category;
        this.rating = rating;
        this.comments = comments;
        this.recommend = recommend;
        this.submissionTime = LocalDateTime.now();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }
}
