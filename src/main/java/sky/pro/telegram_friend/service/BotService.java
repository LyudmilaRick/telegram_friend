package sky.pro.telegram_friend.service;

import sky.pro.telegram_friend.model.NotificationTask;

import java.util.List;

public interface BotService {

    boolean isGoodPattern(String textMessage);

    void saveMessage(Long chatId, String textMessage);

    List<NotificationTask> getAll();

    void setItemFromDataBase(Long id);

}
