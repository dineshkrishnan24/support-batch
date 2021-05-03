package com.growins.supportbatch.readers;

import com.growins.supportbatch.models.dto.TicketDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
public class ChangeStatusReader {

    @Autowired
    private DataSource mysqlDataSource;

    @Value("${days.to.close:30}")
    private int daysToClose;

    private static String READER_NAME = "changeStatusReader";

    private static final String CHANGE_STATUS_SELECT_QUERY = "SELECT id, status " +
            " FROM  ticket " +
            " WHERE updated_time <= '?' AND status = '?'";

    public static String RESOLVED_STATUS = "4";

    public ItemReader<TicketDTO> reader() {

        log.info("Change status job going to read data ");
        PreparedStatementSetter preparedStatementSetter = queryPreparedStatement();
        JdbcCursorItemReader<TicketDTO> itemReader = new JdbcCursorItemReaderBuilder<TicketDTO>()
                .name(READER_NAME)
                .dataSource(mysqlDataSource)
                .sql(CHANGE_STATUS_SELECT_QUERY)
                .preparedStatementSetter(preparedStatementSetter)
                .rowMapper(formRowMapper())
                .build();
        return itemReader;
    }

    private PreparedStatementSetter queryPreparedStatement() {
        LocalDateTime date = LocalDateTime.now().minus(daysToClose, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateStr = date.format(formatter);
        log.info("change status reader values are : "+dateStr);
        return preparedStatement ->  {
            preparedStatement.setString(1, dateStr);
            preparedStatement.setString(2, RESOLVED_STATUS);
        };
    }

    private RowMapper<TicketDTO> formRowMapper() {

        return (resultSet, rowNum) -> {
            TicketDTO ticketDTO = new TicketDTO();
            ticketDTO.setId(resultSet.getLong("id"));
            ticketDTO.setStatus(resultSet.getString("status"));
            return ticketDTO;
        };

    }

}
