package com.ravi.mytaskmanager.service;

import com.ravi.mytaskmanager.dto.TaskDTO;
import com.ravi.mytaskmanager.entity.Task;
import com.ravi.mytaskmanager.entity.User;

import java.util.List;

public interface TaskService {

    TaskDTO createTask(Task task, Long assignedUserId); // only admin
    
    
    List<TaskDTO> getAllTasks(); // admin sees all, user sees own
    
    TaskDTO getTaskById(Long id); // admin or owner
    
    TaskDTO updateTask(Long id, Task updatedTask); // admin or owner
    
    void deleteTask(Long id); // admin or owner
    
    List<TaskDTO> getTasksByUser(Long userId); // admin or self
    
    
   
    
}
