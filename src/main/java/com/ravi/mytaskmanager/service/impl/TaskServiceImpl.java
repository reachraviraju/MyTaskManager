package com.ravi.mytaskmanager.service.impl;

import com.ravi.mytaskmanager.dto.TaskDTO;
import com.ravi.mytaskmanager.entity.Role;
import com.ravi.mytaskmanager.entity.Task;
import com.ravi.mytaskmanager.entity.User;
import com.ravi.mytaskmanager.exception.CustomAccessDeniedException;
import com.ravi.mytaskmanager.exception.ResourceNotFoundException;
import com.ravi.mytaskmanager.repository.TaskRepository;
import com.ravi.mytaskmanager.repository.UserRepository;
import com.ravi.mytaskmanager.service.AuthService;
import com.ravi.mytaskmanager.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, AuthService authService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    public TaskDTO createTask(Task task, Long assignedUserId) {
        User currentUser = authService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new CustomAccessDeniedException("Only admin can create tasks.");
        }

        User assignedUser = userRepository.findById(assignedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", assignedUserId));

        task.setAssignedTo(assignedUser);
        return entityToDto(taskRepository.save(task));
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        User currentUser = authService.getCurrentUser();
        List<Task> tasks;

        if (currentUser.getRole() == Role.ADMIN) {
            tasks = taskRepository.findAll();
        } else {
            tasks = taskRepository.findByAssignedTo(currentUser);
        }

        return tasks.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        User currentUser = authService.getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        if (currentUser.getRole() != Role.ADMIN && !task.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new CustomAccessDeniedException("You can only view your own tasks.");
        }

        return entityToDto(task);
    }

    @Override
    public TaskDTO updateTask(Long id, Task updatedTask) {
        User currentUser = authService.getCurrentUser();
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        if (currentUser.getRole() != Role.ADMIN && !existingTask.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new CustomAccessDeniedException("You can only update your own tasks.");
        }

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());

        return entityToDto(taskRepository.save(existingTask));
    }

    @Override
    public void deleteTask(Long id) {
        User currentUser = authService.getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        if (currentUser.getRole() != Role.ADMIN && !task.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new CustomAccessDeniedException("You can only delete your own tasks.");
        }

        taskRepository.delete(task);
    }

    @Override
    public List<TaskDTO> getTasksByUser(Long userId) {
        User currentUser = authService.getCurrentUser();

        if (currentUser.getRole() != Role.ADMIN && !currentUser.getId().equals(userId)) {
            throw new CustomAccessDeniedException("You can only view your own tasks.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return taskRepository.findByAssignedTo(user)
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    private TaskDTO entityToDto(Task task) {
        Long assignedUserId = (task.getAssignedTo() != null) ? task.getAssignedTo().getId() : null;
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted(), assignedUserId);
    }
}
