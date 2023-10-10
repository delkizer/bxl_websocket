package net.spotv.smartalarm.config;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import net.spotv.smartalarm.job.LgeMetaJob;
import net.spotv.smartalarm.job.SampleJob;

@Configuration
@PropertySource("classpath:job-config.properties")
public class QuartzConfig {
    @Value("${sample.job.execution.cron}")
    private String cronExpression;
    
    @Value("${lgemeta.job.execution.cron}")
    private String lgemetaCronExpression;    
    
    @Bean
    public JobDetail sampleJobDetail() {
        return JobBuilder.newJob(SampleJob.class)
                .withIdentity("sampleJob")
                .storeDurably()
                .build();
    }
    
    @Bean
    public Trigger sampleJobTrigger() {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        return TriggerBuilder.newTrigger()
                .forJob(sampleJobDetail())
                .withIdentity("sampleTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
    
    @Bean
    public JobDetail lgeJobDetail() {
    	return JobBuilder.newJob(LgeMetaJob.class)
    			.withIdentity("LgeMetaJob")
                .storeDurably()
                .build();    			
    }

    @Bean
    public Trigger lgeJobTrigger() {
    	CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(lgemetaCronExpression);
    	
        return TriggerBuilder.newTrigger()
                .forJob(lgeJobDetail())
                .withIdentity("LgeMetaJob")
                .withSchedule(scheduleBuilder)
                .build();
    	
    }
    
}
