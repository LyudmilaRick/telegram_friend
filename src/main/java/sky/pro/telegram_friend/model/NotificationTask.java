package sky.pro.telegram_friend.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Создать сущность notification task
 * Цель задачи — получить класс с аннотацией @Entity,
 * который будет повторять структуру нашей таблицы в БД
 * и будет пригоден для использования в коде нашего приложения.
 **/

@Entity
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTask;
    private Long idChat;
    private LocalDateTime stamp;
    private String textChat;
    private boolean status;

    public NotificationTask(Long idTask, Long idChat, LocalDateTime stamp, String textChat, boolean status) {
        this.idTask = idTask;
        this.idChat = idChat;
        this.stamp = stamp;
        this.textChat = textChat;
        this.status = status;
    }

    public NotificationTask() {
    }

    public Long getIdTask() {
        return idTask;
    }

    public void setIdTask(Long idTask) {
        this.idTask = idTask;
    }

    public Long getIdChat() {
        return idChat;
    }

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    public LocalDateTime getStamp() {
        return stamp;
    }

    public void setStamp(LocalDateTime stamp) {
        this.stamp = stamp;
    }

    public String getTextChat() {
        return textChat;
    }

    public void setTextChat(String textChat) {
        this.textChat = textChat;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return idTask.equals(that.idTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTask);
    }
}
