package com.fittrack.service;

import com.fittrack.entity.FitnessContent;
import com.fittrack.entity.User;
import com.fittrack.repository.FitnessContentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FitnessContentService {

    private final FitnessContentRepository fitnessContentRepository;
    private final ActivityLogService activityLogService;

    public FitnessContentService(FitnessContentRepository fitnessContentRepository,
                                 ActivityLogService activityLogService) {
        this.fitnessContentRepository = fitnessContentRepository;
        this.activityLogService = activityLogService;
    }

    // User submits fitness content
    @Transactional
    public FitnessContent submitContent(User author, String title, String category, String summary,
                                        String bodyContent, String coverImageUrl) {
        FitnessContent content = new FitnessContent(author, title, category, summary, bodyContent);
        content.setCoverImageUrl(coverImageUrl != null && !coverImageUrl.isBlank() ? coverImageUrl : "/images/default-article.jpg");
        content.setStatus("PENDING");
        FitnessContent saved = fitnessContentRepository.save(content);
        activityLogService.logActivity(author, "CONTENT_SUBMITTED",
                "Submitted article for review: '" + title + "' (" + category + ")", null);
        return saved;
    }

    // Public / Users: view approved content
    @Transactional(readOnly = true)
    public List<FitnessContent> getApprovedContent() {
        return fitnessContentRepository.findByStatusOrderBySubmittedAtDesc("APPROVED");
    }

    // User: view my submissions
    @Transactional(readOnly = true)
    public List<FitnessContent> getUserContentSubmissions(User author) {
        return fitnessContentRepository.findByAuthorOrderBySubmittedAtDesc(author);
    }

    // Admin: view all submissions
    @Transactional(readOnly = true)
    public List<FitnessContent> getAllContent() {
        return fitnessContentRepository.findAllByOrderBySubmittedAtDesc();
    }

    // Admin: view pending submissions
    @Transactional(readOnly = true)
    public List<FitnessContent> getPendingContent() {
        return fitnessContentRepository.findByStatusOrderBySubmittedAtDesc("PENDING");
    }

    @Transactional(readOnly = true)
    public Optional<FitnessContent> getContentById(Long id) {
        return fitnessContentRepository.findById(id);
    }

    // Admin: Approve content
    @Transactional
    public FitnessContent approveContent(Long contentId, String adminUsername, String feedback) {
        FitnessContent content = fitnessContentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found"));

        content.setStatus("APPROVED");
        content.setReviewedBy(adminUsername);
        content.setReviewedAt(LocalDateTime.now());
        content.setAdminFeedback(feedback != null && !feedback.isBlank() ? feedback : "Approved by editorial team.");

        FitnessContent saved = fitnessContentRepository.save(content);
        activityLogService.logActivity(content.getAuthor(), "CONTENT_APPROVED",
                "Article '" + content.getTitle() + "' approved by @" + adminUsername, null);
        return saved;
    }

    // Admin: Reject content
    @Transactional
    public FitnessContent rejectContent(Long contentId, String adminUsername, String feedback) {
        FitnessContent content = fitnessContentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found"));

        content.setStatus("REJECTED");
        content.setReviewedBy(adminUsername);
        content.setReviewedAt(LocalDateTime.now());
        content.setAdminFeedback(feedback != null && !feedback.isBlank() ? feedback : "Needs revision before approval.");

        FitnessContent saved = fitnessContentRepository.save(content);
        activityLogService.logActivity(content.getAuthor(), "CONTENT_REJECTED",
                "Article '" + content.getTitle() + "' rejected by @" + adminUsername + ". Note: " + feedback, null);
        return saved;
    }

    // Delete content
    @Transactional
    public void deleteContent(Long contentId, User user, boolean isAdmin) {
        FitnessContent content = fitnessContentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found"));

        if (!isAdmin && !content.getAuthor().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized to delete this content.");
        }

        fitnessContentRepository.delete(content);
    }

    @Transactional(readOnly = true)
    public long getPendingCount() {
        return fitnessContentRepository.countByStatus("PENDING");
    }

    @Transactional(readOnly = true)
    public long getApprovedCount() {
        return fitnessContentRepository.countByStatus("APPROVED");
    }
}
