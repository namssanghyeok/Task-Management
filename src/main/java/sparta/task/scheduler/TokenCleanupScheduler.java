package sparta.task.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sparta.task.service.RefreshTokenService;

@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {
    private final RefreshTokenService refreshTokenService;

    // @Scheduled(cron = "*/10 * * * * *") 매 10초
    // @Scheduled(cron = "*/10 1 * * * *") xx시 1분에 들어오면 10초 마다 실행
    // NOTE: 매일 새벽 1시
    @Scheduled(cron = "0 0 1 * * *")
    public void cleanupRefreshToken() {
        this.refreshTokenService.deleteExpiredTokens();
    }
}
