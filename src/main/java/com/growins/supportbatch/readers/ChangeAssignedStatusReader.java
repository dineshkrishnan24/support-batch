package com.growins.supportbatch.readers;

import com.growins.supportbatch.models.dto.AgentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
public class ChangeAssignedStatusReader {

    @Autowired
    private DataSource mysqlDataSource;

    private static String READER_NAME = "changeAssignedStatusReader";

    private static final String CHANGE_ASSIGNED_STATUS_SELECT_QUERY = "SELECT id, assigned_status " +
            " FROM  agent ";

    public ItemReader<AgentDTO> reader() {

        log.info("Change assigned status job going to read data ");
        JdbcCursorItemReader<AgentDTO> itemReader = new JdbcCursorItemReaderBuilder<AgentDTO>()
                .name(READER_NAME)
                .dataSource(mysqlDataSource)
                .sql(CHANGE_ASSIGNED_STATUS_SELECT_QUERY)
                .rowMapper(formRowMapper())
                .build();
        return itemReader;
    }

    private RowMapper<AgentDTO> formRowMapper() {

        return (resultSet, rowNum) -> {
            AgentDTO agentDTO = new AgentDTO();
            agentDTO.setId(resultSet.getLong("id"));
            agentDTO.setAssignedStatus(resultSet.getString("assigned_status"));
            return agentDTO;
        };

    }

}
