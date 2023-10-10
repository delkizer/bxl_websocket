package net.spotv.smartalarm.job;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class SampleJob implements Job {
   
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        
        // 설정된 시간이 아니면 아무것도 하지 않습니다.
        System.out.println("job started 현재 시각: " + formattedNow);
    }
}
