package com.growins.supportbatch.writters;

import com.growins.supportbatch.models.dto.TicketDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Slf4j
@Service
public class ChangeStatusWriter {

    @Autowired
    private DataSource mysqlDataSource;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String CHANGE_STATUS_QUERY = "UPDATE ticket " +
            "SET status='5' where id=?";

    public ItemWriter<TicketDTO> writer() {
        log.info("change status Writer Initiated");

        return new JdbcBatchItemWriterBuilder<TicketDTO>()
                .dataSource(mysqlDataSource)
                .namedParametersJdbcTemplate(namedParameterJdbcTemplate)
                .sql(CHANGE_STATUS_QUERY)
                .itemPreparedStatementSetter(formPreparedStatement())
                .build();
    }

    private ItemPreparedStatementSetter<TicketDTO> formPreparedStatement() {

        return (ticketDTO, preparedStatement) -> {
            preparedStatement.setLong(1, ticketDTO.getId());
        };

    }

}
