package sky.pro.telegram_friend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sky.pro.telegram_friend.excepton.InvalidArgumentException;
import sky.pro.telegram_friend.model.NotificationTask;
import sky.pro.telegram_friend.repository.TaskRepository;

import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.naming.directory.InvalidAttributesException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sky.pro.telegram_friend.model.ConstantsForText.*;


@Service
public class BotServiceImpl implements BotService {
    private final Logger logger = LoggerFactory.getLogger(BotServiceImpl.class);
    private final TaskRepository taskRepository;

    public BotServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * @param textMessage строка для парсинга
     * @return да - нет
     * проверка на нормальность
     */
    @Override
    public boolean isGoodPattern(String textMessage) {
        Pattern pattern = Pattern.compile(STRING_FORMAT);
        Matcher matcher = pattern.matcher(textMessage);
        return matcher.matches();
    }

    /**
     * сохранить одну запись в БД
     */
    @Override
    public void saveMessage(Long chatId, String textMessage) {
        NotificationTask notificationTask = parseMessage(chatId, textMessage);
        if (notificationTask != null) {
            taskRepository.save(notificationTask);
        }
    }

    @Override
    public List<NotificationTask> getAll() {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        return taskRepository.findAllScheduledTasks(localDateTime);

    }

    @Override
    public void setItemFromDataBase(Long id)  {
        NotificationTask notificationTask = getItemFromDataBase(id);
        notificationTask.setStatus(true);
        taskRepository.save(notificationTask);
    }

    /**
     * разобрать строку на составляющие и сложить по полям
     */
    private NotificationTask parseMessage(Long chatId, String textMessage) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(STRING_PATTERN);
        NotificationTask notificationTask = new NotificationTask();
        Pattern pattern = Pattern.compile(STRING_FORMAT);
        try {
            Matcher matcher = pattern.matcher(textMessage);
            if (matcher.matches()) {
                notificationTask.setIdChat(chatId);
                LocalDateTime localDateTime = LocalDateTime.parse(matcher.group(1), format);
                notificationTask.setStamp(localDateTime);
                notificationTask.setTextChat(matcher.group(3));
                notificationTask.setStatus(false);
            }
        } catch (Exception e) {
            logger.warn("Method was stopped-check text!" + e.getMessage());
        }
        return notificationTask;
    }

    /**
     * найти запись по идентификатору
     *
     * @param id идентификатор
     * @return NotificationTask
     */
    private NotificationTask getItemFromDataBase(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(()-> new InvalidArgumentException("Информация по идентификатору не найдена" + id));

    }
}
