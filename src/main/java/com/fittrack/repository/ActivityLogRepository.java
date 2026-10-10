package com.fittrack.repository;

import com.fittrack.entity.ActivityLog;
import com.fittrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findTop100ByOrderByTimestampDesc();

    List<ActivityLog> findByUserOrderByTimestampDesc(User user);

    long countByActionType(String actionType);
}
