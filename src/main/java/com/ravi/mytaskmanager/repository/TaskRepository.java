package com.ravi.mytaskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ravi.mytaskmanager.entity.Task;
import com.ravi.mytaskmanager.entity.User;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User user);
}

