package io.baselogic.batch.introduction.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class JobConfig {

    //---------------------------------------------------------------------------//
    // Jobs

    //---------------------------------------------------------------------------//
    // LAB: Create Job

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step step1) {
        return jobBuilderFactory.get("helloWorldJob")
                .start(step1)
                .build();
    }



} // The End...
