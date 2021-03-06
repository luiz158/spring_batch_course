package io.baselogic.batch.split.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class JobConfig {

    //---------------------------------------------------------------------------//
    // Jobs


    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   TaskExecutor taskExecutor,
                   Step stepA, Step stepB, Step stepC, Step stepD) {

        Flow flow1 = new FlowBuilder<SimpleFlow>("flow1")
                .start(stepA)
                .next(stepB)
                .build();

        Flow flow2 = new FlowBuilder<SimpleFlow>("flow2")
                .start(stepC)
                .build();

        return jobBuilderFactory.get("job")
                .start(flow1)

//                .split(new SimpleAsyncTaskExecutor())
                .split(taskExecutor)

                .add(flow2)

                .next(stepD)

                .end()
                .build();
    }


} // The End...
