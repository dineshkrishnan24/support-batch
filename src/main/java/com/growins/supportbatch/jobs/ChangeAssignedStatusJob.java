package com.growins.supportbatch.jobs;

import com.growins.supportbatch.models.dto.AgentDTO;
import com.growins.supportbatch.models.dto.TicketDTO;
import com.growins.supportbatch.processors.ChangeAssignedStatusProcessor;
import com.growins.supportbatch.readers.ChangeAssignedStatusReader;
import com.growins.supportbatch.writters.ChangeAssignedStatusWriter;
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
public class ChangeAssignedStatusJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ChangeAssignedStatusReader assignedStatusReader;

    @Autowired
    private ChangeAssignedStatusProcessor assignedStatusProcessor;

    @Autowired
    private ChangeAssignedStatusWriter assignedStatusWriter;

    private static String CHANGE_ASSIGNED_STATUS_JOB_NAME = "CHANGE_ASSIGNED_STATUS_JOB";

    public Job runChangeAssignedStatusJob() {
        log.info("Process assigned change status job");
        return jobBuilderFactory.get(CHANGE_ASSIGNED_STATUS_JOB_NAME + System.nanoTime())
                .start(changeAssignedStatusStep())
                .build();

    }

    private Step changeAssignedStatusStep() {
        log.info("Change assigned status Job step 1 ");
        return stepBuilderFactory.get("changeAssignedStatusStep1")
                .<AgentDTO, AgentDTO>chunk(50)
                .reader(processChangeAssignedStatusReaderFunc())
                .processor(processChangeAssignedStatusProcessorFunc())
                .writer(processChangeAssignedStatusWriterFunc())
                .build();
    }

    @Bean
    ItemReader<AgentDTO> processChangeAssignedStatusReaderFunc() { return assignedStatusReader.reader(); }

    @Bean
    ItemProcessor<AgentDTO, AgentDTO> processChangeAssignedStatusProcessorFunc() {
        return assignedStatusProcessor;
    }

    @Bean
    ItemWriter<AgentDTO> processChangeAssignedStatusWriterFunc() {
        return assignedStatusWriter.writer();
    }
}
