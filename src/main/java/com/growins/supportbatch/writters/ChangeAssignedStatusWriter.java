package com.growins.supportbatch.writters;

import com.growins.supportbatch.models.dto.AgentDTO;
import com.growins.supportbatch.models.dto.TicketDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Slf4j
@Service
public class ChangeAssignedStatusWriter {


    @Autowired
    private DataSource mysqlDataSource;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String CHANGE_ASSIGNED_STATUS_QUERY = "UPDATE agent " +
            "SET assigned_status='0' where id=?";

    public ItemWriter<AgentDTO> writer() {
        log.info("change status Writer Initiated");

        return new JdbcBatchItemWriterBuilder<AgentDTO>()
                .dataSource(mysqlDataSource)
                .namedParametersJdbcTemplate(namedParameterJdbcTemplate)
                .sql(CHANGE_ASSIGNED_STATUS_QUERY)
                .itemPreparedStatementSetter(formPreparedStatement())
                .build();
    }

    private ItemPreparedStatementSetter<AgentDTO> formPreparedStatement() {

        return (agentDTO, preparedStatement) -> {
            preparedStatement.setLong(1, agentDTO.getId());
        };

    }


}
