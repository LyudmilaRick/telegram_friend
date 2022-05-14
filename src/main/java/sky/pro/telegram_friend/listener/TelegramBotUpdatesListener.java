package sky.pro.telegram_friend.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import com.vdurmont.emoji.EmojiParser;
import sky.pro.telegram_friend.TelegramFriendApplication;
import sky.pro.telegram_friend.model.NotificationTask;
import sky.pro.telegram_friend.service.BotService;

import static sky.pro.telegram_friend.model.ConstantsForText.*;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final BotService  botService;
    private final Logger      logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(BotService botService, TelegramBot telegramBot) {
        this.botService = botService;
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    /**
     * .4 Научиться реагировать на команду /start и выводить приветственное сообщение
     *
     * @param updates параметр
     * @return return
     * Научиться парсить сообщения с создаваемым напоминанием и сохранять их в БД
     */
    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            logger.info("Processing update: {}", update);

            Long chatId = update.message().chat().id();
            String textMessage = update.message().text();
            try {

                if (textMessage.startsWith(TEXT_START)) {
                    sendGreeting(chatId, TEXT_GREETING);
                    sendEmoji(chatId, EMOJI_KISSING);
                } else {
                    if (botService.isGoodPattern(textMessage)) {
                        botService.saveMessage(chatId, textMessage);
                        sendGreeting(chatId, TEXT_OK);
                        sendEmoji(chatId, EMOJI_CHECK);
                    }
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
                sendEmoji(chatId, EMOJI_NOT);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * 2.5 Научиться по шедулеру раз в минуту выбирать записи из БД,
     * для которых должны быть отправлены нотификации
     * Для полученной коллекции записей
     * необходимо рассылать уведомления в нужные чаты.
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        List<NotificationTask> items = botService.getAll();
        for (NotificationTask item : items) {
            sendEmoji(item.getIdChat(), item.getTextChat());
            botService.setItemFromDataBase(item.getIdTask());
        }
    }

    /**
     * немного солнца в холодной воде
     *
     * @param chatId   чат
     * @param emoji_Id картинка
     */
    private void sendEmoji(Long chatId, String emoji_Id) {
        String emoji = EmojiParser.parseToUnicode(emoji_Id);
        SendMessage requestStick = new SendMessage(chatId, emoji)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
        telegramBot.execute(requestStick);
    }

    private void sendGreeting(Long chatId, String text) {
        SendMessage request = new SendMessage(chatId, text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1);
        telegramBot.execute(request);
    }

}
