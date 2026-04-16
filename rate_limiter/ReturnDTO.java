package rate_limiter;

public record ReturnDTO(
    ValidQueueEnum status,
    long time_await
) {
    
}
