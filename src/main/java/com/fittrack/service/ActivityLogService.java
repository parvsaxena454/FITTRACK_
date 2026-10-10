package com.fittrack.service;

import com.fittrack.entity.ActivityLog;
import com.fittrack.entity.User;
import com.fittrack.repository.ActivityLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogService(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    @Transactional
    public void logActivity(User user, String actionType, String description, String ipAddress) {
        ActivityLog log = new ActivityLog(user, actionType, description, ipAddress != null ? ipAddress : "127.0.0.1");
        activityLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<ActivityLog> getRecentLogs() {
        return activityLogRepository.findTop100ByOrderByTimestampDesc();
    }

    @Transactional(readOnly = true)
    public List<ActivityLog> getUserLogs(User user) {
        return activityLogRepository.findByUserOrderByTimestampDesc(user);
    }

    @Transactional(readOnly = true)
    public long getTotalLogsCount() {
        return activityLogRepository.count();
    }
}
