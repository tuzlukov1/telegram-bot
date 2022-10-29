package pro.sky.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTasksRepository;

@Service
public class NotificationTaskService {

    @Autowired
    private NotificationTasksRepository notificationTasksRepository;

    public void createNotificationTask(NotificationTask notificationTask) {
        notificationTasksRepository.save(notificationTask);
    }
}
