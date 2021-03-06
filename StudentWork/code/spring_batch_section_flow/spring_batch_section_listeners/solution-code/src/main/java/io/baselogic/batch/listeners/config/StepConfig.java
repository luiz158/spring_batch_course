package io.baselogic.batch.listeners.config;

import io.baselogic.batch.listeners.listeners.*;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;


@Configuration
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class StepConfig {
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());


    //---------------------------------------------------------------------------//
    // Lab: Create @BeforeRead and log step details
    @Bean
    public Step stepA(StepBuilderFactory stepBuilderFactory,
                      CustomStepExecutionListener customStepExecutionListener,
                      ChunkListener chunkListener,
                      CustomItemReadListener itemReadListener,
                      CustomItemWriterListener itemWriterListener,
                      ItemReader<String> reader,
                      ItemWriter<String> writer
    ) {
        return stepBuilderFactory.get("step1")

                // StepExecutionListener Must be before the Chunk
                .listener(customStepExecutionListener)

                .<String, String>chunk(2)
                .faultTolerant()

                .listener(chunkListener)
                .listener(itemReadListener)
                .listener(itemWriterListener)
                .reader(reader)

                .writer(writer)
                .build();
    }


    //---------------------------------------------------------------------------//
    // Tasklets




    //---------------------------------------------------------------------------//
    // Readers

    @Bean
    public ItemReader<String> reader() {
        return new ListItemReader<>(Arrays.asList("A", "B", "C", "D", "ONE", "TWO", "THREE", "FOUR"));
    }



    //---------------------------------------------------------------------------//
    // Processors



    //---------------------------------------------------------------------------//
    // Writers

    @Bean
    public ItemWriter<String> writer() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {

                for (String item : items) {
                    log.info("Writing item: {}", item);
                }
            }
        };
    }


    //---------------------------------------------------------------------------//
    // Lab: Create @Bean declarations for all five listeners
    @Bean
    public JobListener jobListener(){
        return new JobListener();
    }


    @Bean
    public CustomStepExecutionListener customStepExecutionListener(){
        return new CustomStepExecutionListener();
    }


    @Bean
    public ChunkListener chunkListener(){
        return new ChunkListener();
    }

    @Bean
    public CustomItemReadListener itemReadListener(){
        return new CustomItemReadListener();
    }

    @Bean
    public CustomItemWriterListener customeItemWriterListener(){
        return new CustomItemWriterListener();
    }


} // The End...
