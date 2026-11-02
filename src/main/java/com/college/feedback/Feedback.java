package com.college.feedback;

import java.io.Serializable;

/**
 * Model representing an individual user feedback submission.
 */
public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String email;
    private String category;
    private int rating;
    private String comments;
    private String timestamp;
    private String submissionMethod; // "POST" or "GET"

    // Default Constructor
    public Feedback() {
    }

    // Parameterized Constructor
    public Feedback(int id, String name, String email, String category, int rating, 
                    String comments, String timestamp, String submissionMethod) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.category = category;
        this.rating = rating;
        this.comments = comments;
        this.timestamp = timestamp;
        this.submissionMethod = submissionMethod;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSubmissionMethod() {
        return submissionMethod;
    }

    public void setSubmissionMethod(String submissionMethod) {
        this.submissionMethod = submissionMethod;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                ", timestamp='" + timestamp + '\'' +
                ", submissionMethod='" + submissionMethod + '\'' +
                '}';
    }
}
