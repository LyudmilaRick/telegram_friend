package sky.pro.telegram_friend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramFriendApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelegramFriendApplication.class, args);
    }

}
