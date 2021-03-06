package io.baselogic.batch.retry.listeners;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.annotation.*;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.util.List;

@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class CustomChunkListener {
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    //---------------------------------------------------------------------------//
    // Lab: Create @BeforeChunk lifecycle method
    @BeforeChunk
    public void beforeChunk(ChunkContext context) {

        String stepName = context.getStepContext().getStepExecution().getStepName();

        log.info("--> Before Chunk:");
        log.info("\t Step Name: {}", stepName);
    }

    //---------------------------------------------------------------------------//
    // Lab: Create @AfterChunk lifecycle method
    @AfterChunk
    public void afterChunk(ChunkContext context) {

        String stepName = context.getStepContext().getStepExecution().getStepName();

        int readCount = context.getStepContext().getStepExecution().getReadCount();
        int writeCount = context.getStepContext().getStepExecution().getWriteCount();
        int writeSkipCount = context.getStepContext().getStepExecution().getWriteSkipCount();

        log.info("--> After Chunk:");
        log.info("\t Step Name: {}", stepName);
        log.info("\t readCount: {}", readCount);
        log.info("\t writeCount: {}", writeCount);
        log.info("\t writeSkipCount: {}", writeSkipCount);
    }

    //---------------------------------------------------------------------------//
    // Lab: Create @AfterChunk lifecycle method
    @AfterChunkError
    public void afterChunkError(ChunkContext context) {

        String stepName = context.getStepContext().getStepExecution().getStepName();

        int readCount = context.getStepContext().getStepExecution().getReadCount();
        int writeCount = context.getStepContext().getStepExecution().getWriteCount();
        int writeSkipCount = context.getStepContext().getStepExecution().getWriteSkipCount();

        log.info("--> After Chunk ERROR:");
        log.info("\t Step Name: {}", stepName);
        log.info("\t readCount: {}", readCount);
        log.info("\t writeCount: {}", writeCount);
        log.info("\t writeSkipCount: {}", writeSkipCount);
    }

} // The End...
