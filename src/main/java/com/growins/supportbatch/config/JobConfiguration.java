package com.growins.supportbatch.config;

import com.growins.supportbatch.jobs.ChangeAssignedStatusJob;
import com.growins.supportbatch.jobs.ChangeStatusJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@EnableScheduling
@EnableBatchProcessing
@Configuration
public class JobConfiguration {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ChangeStatusJob changeStatusJob;

    @Autowired
    private ChangeAssignedStatusJob changeAssignedStatusJob;

//    @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(fixedDelay = 30000)
    public void runChangeStatusJob() {
        log.info("Change status Job Started");
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        try {
            jobLauncher.run(changeStatusJob.runChangeStatusJob(), jobParameters);
        } catch (Exception e) {
            log.error("Exception while starting change status job : ",e);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void runChangeAssignedStatusJob() {
        log.info("Change assigned status Job Started");
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        try {
            jobLauncher.run(changeAssignedStatusJob.runChangeAssignedStatusJob(), jobParameters);
        } catch (Exception e) {
            log.error("Exception while starting change assigned status job : ",e);
        }
    }

}
