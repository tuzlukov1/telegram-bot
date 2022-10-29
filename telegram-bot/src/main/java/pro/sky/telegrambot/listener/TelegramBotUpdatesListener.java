package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private SendResponse response;
    private Boolean greetings = false;
    NotificationTask notificationTask;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private NotificationTaskService notificationTaskService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {

        try {
            updates.forEach(update -> {
                logger.info("Processing update: {}", update);
                if (update.message().text().equals("/start") && greetings == false) {
                    SendMessage message = new SendMessage(
                            update.message().chat().id(),
                            "Welcome to Friendly Reminda Bot! Nice to meet you.");
                    response = telegramBot.execute(message);
                    greetings = true;
                }

                Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

                Matcher matcher = pattern.matcher(update.message().text());
                if (matcher.matches()) {
                    String date = matcher.group(1);
                    String item = matcher.group(3);

                    String success = "Your reminder was set! " + date + " i will remind you to do: " + item;
                    SendMessage message = new SendMessage(
                            update.message().chat().id(), success
                    );
                    response = telegramBot.execute(message);

                    notificationTask = new NotificationTask();
                    notificationTask.setDateTime(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                    notificationTask.setText(item);
                    notificationTask.setChatId(update.message().chat().id());

                    notificationTaskService.createNotificationTask(notificationTask);
                }

                if (!response.isOk()) {
                    logger.error(String.valueOf(response.errorCode()));
                }
            });
        } finally {
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }
    }

}
