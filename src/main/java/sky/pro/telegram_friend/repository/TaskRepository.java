package sky.pro.telegram_friend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sky.pro.telegram_friend.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий. Просто репозиторий
 * написать в репозитории метод, который ищет записи, у которых время совпадает с текущим.
 */
@Repository
public interface TaskRepository extends JpaRepository<NotificationTask, Long>{
    @Query(value = "SELECT *  FROM notification_task WHERE stamp = ?1 AND status = false", nativeQuery = true)
    List<NotificationTask> findAllScheduledTasks(LocalDateTime localDateTime);
}
