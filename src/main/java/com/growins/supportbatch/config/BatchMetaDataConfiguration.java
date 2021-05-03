package com.growins.supportbatch.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BatchMetaDataConfiguration extends DefaultBatchConfigurer {

    @Override
    public void setDataSource(DataSource dataSource) {
        // For Disable creation of batch meta data table.
        // override to do not set datasource even if a datasource exist.
        // initialize will use a Map based JobRepository (instead of database).
    }
}

