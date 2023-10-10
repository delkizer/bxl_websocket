package net.spotv.smartalarm.config;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

public class QuartzJobLauncher implements Job {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private org.springframework.batch.core.Job sampleJob;
    
    @Autowired
    private org.springframework.batch.core.Job lgeMetaJob;    

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
        	//jobLauncher.run(sampleJob, new JobParametersBuilder().addLong("uniqueness", System.currentTimeMillis()).toJobParameters());
            jobLauncher.run(lgeMetaJob
            		, new JobParametersBuilder().addLong("uniqueness", System.currentTimeMillis()).toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
