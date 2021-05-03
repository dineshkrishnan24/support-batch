package com.growins.supportbatch.processors;

import com.growins.supportbatch.models.dto.AgentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangeAssignedStatusProcessor implements ItemProcessor<AgentDTO, AgentDTO> {

    @Override
    public AgentDTO process(AgentDTO agentDTO) throws Exception {
        return agentDTO;
    }

}
