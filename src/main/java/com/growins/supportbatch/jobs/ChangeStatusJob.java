package com.growins.supportbatch.jobs;

import com.growins.supportbatch.models.dto.TicketDTO;
import com.growins.supportbatch.processors.ChangeStatusProcessor;
import com.growins.supportbatch.readers.ChangeStatusReader;
import com.growins.supportbatch.writters.ChangeStatusWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChangeStatusJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ChangeStatusReader changeStatusReader;

    @Autowired
    private ChangeStatusProcessor changeStatusProcessor;

    @Autowired
    private ChangeStatusWriter changeStatusWriter;

    private static String CHANGE_STATUS_JOB_NAME = "CHANGE_STATUS_JOB";
    
    public Job runChangeStatusJob() {
        log.info("Process change status job");
        return jobBuilderFactory.get(CHANGE_STATUS_JOB_NAME + System.nanoTime())
                .start(changeStatusStep())
                .build();

    }

    private Step changeStatusStep() {
        log.info("Change status Job step 1 ");
        return stepBuilderFactory.get("changeStatusStep1")
                .<TicketDTO, TicketDTO>chunk(50)
                .reader(processChangeStatusReaderFunc())
                .processor(processChangeStatusProcessorFunc())
                .writer(processChangeStatusWriterFunc())
                .build();
    }

    @Bean
    ItemReader<TicketDTO> processChangeStatusReaderFunc() { return changeStatusReader.reader(); }

    @Bean
    ItemProcessor<TicketDTO, TicketDTO> processChangeStatusProcessorFunc() {
        return changeStatusProcessor;
    }

    @Bean
    ItemWriter<TicketDTO> processChangeStatusWriterFunc() {
        return changeStatusWriter.writer();
    }



}
