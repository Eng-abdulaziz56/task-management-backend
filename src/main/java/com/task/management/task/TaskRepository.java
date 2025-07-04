package com.task.management.task;

import com.task.management.task.enums.TaskPriority;
import com.task.management.task.enums.TaskStatus;
import com.task.management.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByUser(User user);
    Optional<Task> findByIdAndUser(Integer id, User user);

    @Query("""
SELECT t FROM Task t
WHERE t.user = :user
AND (:priority IS NULL OR t.priority = :priority)
AND (:status IS NULL OR t.status = :status)
AND (
    LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR 
    LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))
)
""")

    List<Task> searchTasks(
            @Param("user") User user,
            @Param("priority") TaskPriority priority,
            @Param("status") TaskStatus status,
            @Param("search") String search
    );
}
