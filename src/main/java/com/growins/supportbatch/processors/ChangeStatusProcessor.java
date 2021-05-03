package com.growins.supportbatch.processors;

import com.growins.supportbatch.models.dto.TicketDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangeStatusProcessor implements ItemProcessor<TicketDTO, TicketDTO> {
    @Override
    public TicketDTO process(TicketDTO ticketDTO) throws Exception {
        return ticketDTO;
    }
}
