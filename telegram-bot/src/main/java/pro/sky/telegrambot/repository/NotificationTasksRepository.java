package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface NotificationTasksRepository extends JpaRepository<NotificationTask, Integer> {

    default List<NotificationTask> getNotificationTasksByDateTime(LocalDateTime dateTime) {
        return findAll()
                .stream()
                .filter(notificationTask -> notificationTask.getDateTime().equals(dateTime))
                .collect(Collectors.toList());
    }
}
