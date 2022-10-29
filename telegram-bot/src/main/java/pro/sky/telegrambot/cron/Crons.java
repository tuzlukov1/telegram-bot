package pro.sky.telegrambot.cron;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTasksRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class Crons {

    @Autowired
    private TelegramBot telegramBot;

    private SendResponse response;

    @Autowired
    NotificationTasksRepository notificationTasksRepository;

    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        var currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> notificationTasks = notificationTasksRepository
                .getNotificationTasksByDateTime(currentTime);
        for (int i = 0; i < notificationTasks.size(); i++) {
            String notification = "Friendly reminder:" + notificationTasks.get(i).getText();
            SendMessage message = new SendMessage(
                    notificationTasks.get(i).getChatId(), notification
            );
            response = telegramBot.execute(message);
        }
    }
}
