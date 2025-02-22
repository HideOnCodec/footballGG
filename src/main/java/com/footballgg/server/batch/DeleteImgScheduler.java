package com.footballgg.server.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeleteImgScheduler {
    private final JobLauncher jobLauncher;
    private final Job job;

    @Async(value = "asyncExecutor")
    //@Scheduled(cron = "0 0 0 * * SUN")
    @Scheduled(cron = "0 */1 * * * *")
    public void runDeleteImgJob()
            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(job, new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis()) // 유니크 파라미터 추가
                .toJobParameters());
    }
}
