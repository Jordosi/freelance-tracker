package ru.jordosi.freelance_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryCreateDto;
import ru.jordosi.freelance_tracker.dto.time_entry.TimeEntryUpdateDto;
import ru.jordosi.freelance_tracker.exception.ResourceNotFoundException;
import ru.jordosi.freelance_tracker.model.Project;
import ru.jordosi.freelance_tracker.model.Task;
import ru.jordosi.freelance_tracker.model.TimeEntry;
import ru.jordosi.freelance_tracker.model.User;
import ru.jordosi.freelance_tracker.repository.TimeEntryRepository;
import ru.jordosi.freelance_tracker.repository.UserRepository;
import ru.jordosi.freelance_tracker.service.AccessControlService;
import ru.jordosi.freelance_tracker.service.TaskService;
import ru.jordosi.freelance_tracker.service.TimeEntryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeEntryServiceImpl implements TimeEntryService {
    private final TimeEntryRepository timeEntryRepository;
    private final TaskService taskService;
    private final UserRepository userRepository;
    private final AccessControlService accessControlService;

    @Override
    @Transactional
    public TimeEntry createTimeEntry(TimeEntryCreateDto dto, Long userId) {
        Task task = taskService.getTaskByIdEntity(dto.getTaskId());
        User user = getFreelancerAndCheck(userId);
        checkFreelancerAssignmentToProject(task.getProject(), user);
        TimeEntry timeEntry = TimeEntry.builder()
                .entryDate(dto.getEntryDate())
                .timeSpent(dto.getTimeSpent())
                .task(task)
                .user(user)
                .build();
        timeEntry = timeEntryRepository.save(timeEntry);
        return timeEntry;
    }

    @Override
    @Transactional
    public TimeEntry updateTimeEntry(Long id, TimeEntryUpdateDto dto, Long userId) {
        Task task = taskService.getTaskByIdEntity(dto.getTaskId());
        User user = getFreelancerAndCheck(userId);
        checkFreelancerAssignmentToProject(task.getProject(), user);
        TimeEntry timeEntry = timeEntryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time entry not found"));
        timeEntry.setEntryDate(dto.getEntryDate());
        timeEntry.setTimeSpent(dto.getTimeSpent());
        timeEntry.setTask(task);
        return timeEntryRepository.save(timeEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeEntry> getTimeEntriesByTask(Long taskId, Long userId) {
        Task task = taskService.getTaskByIdEntity(taskId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole().equals(User.Role.FREELANCER)) {
            checkFreelancerAssignmentToProject(task.getProject(), user);
        }
        else {
            accessControlService.validateProjectAccessForRead(task.getProject());
        }
        return timeEntryRepository.findByTaskId(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalTimeForTask(Long taskId, Long userId) {
        Task task = taskService.getTaskByIdEntity(taskId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRole().equals(User.Role.FREELANCER)) {
            checkFreelancerAssignmentToProject(task.getProject(), user);
        }
        else {
            accessControlService.validateProjectAccessForRead(task.getProject());
        }
        return timeEntryRepository.getTotalTimeSpentByTask(taskId).orElse(0);
    }

    @Override
    @Transactional
    public void deleteTimeEntry(Long id, Long userId) {
        Task task = timeEntryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time entry does not exist")).getTask();
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        checkFreelancerAssignmentToProject(task.getProject(), user);
        TimeEntry entry = timeEntryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Time entry does not exist"));
        if (!entry.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can delete only your own time entries");
        }
        timeEntryRepository.deleteById(id);
    }

    private User getFreelancerAndCheck(Long userId) {
        return userRepository.findByIdAndRole(userId, User.Role.FREELANCER).orElseThrow(() -> new ResourceNotFoundException("Freelancer with given ID not found"));
    }

    private void checkFreelancerAssignmentToProject(Project project, User freelancer) {
        if (!project.getFreelancers().contains(freelancer)) {
            throw new AccessDeniedException("You are not assigned to this project");
        }
    }
}
