package io.baselogic.batch.parameters.jobs;

import io.baselogic.batch.common.config.BatchDao;
import io.baselogic.batch.parameters.config.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = {
        TestConfig.class,
        DatabaseConfig.class,
        BatchConfig.class,
        JobConfig.class,
        StepConfig.class
})

@SpringBatchTest

@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class JobTests {
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private BatchDao batchDao;

    @Before
    public void clearMetadata() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    //---------------------------------------------------------------------------//
    // Jobs

    @Autowired
    @Qualifier("job")
    private Job job;


    //---------------------------------------------------------------------------//


    public JobParameters getJobParameters() {
        return new JobParametersBuilder()
                .addString("message", "JobParameter message")
                .addLong("commit.interval", 1L)
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
    }

    //---------------------------------------------------------------------------//


    @Test
    public void test__launch_job__all_steps() throws Exception {

        jobLauncherTestUtils.setJob(job);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        log.info(logJobExecution(jobExecution));

        if(log.isDebugEnabled()) {

            jobExecution.getStepExecutions().forEach(stepExecution -> {
                log.debug(logStepExecution(stepExecution));

            });

            // List all steps from the database:
            log.debug(batchDao.logStepExecutions());
        }

        // NOTE: Query the BATCH database for the results:
        assertThat(batchDao.countJobExecutions()).isEqualTo(2);
        assertThat(batchDao.countJobInstances()).isEqualTo(2);

        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(jobExecution.getStepExecutions().size()).isEqualTo(4);
    }


    //---------------------------------------------------------------------------//
    //---------------------------------------------------------------------------//
    //---------------------------------------------------------------------------//

    /**
     * Need to create a query for a single job execution
     * @param jobExecution
     * @return
     */
    protected String logJobExecution(JobExecution jobExecution) {

        String results = batchDao.logJobExecutions(jobExecution);
        batchDao.logJobExecutions();
        batchDao.logStepExecutions();
        batchDao.countJobExecutions();
        batchDao.countJobInstances();
        batchDao.consoleLine('m');

        return results;
    }


    /**
     * Need to create a query for a single step execution
     * @param stepExecution
     * @return
     */
    protected String logStepExecution(StepExecution stepExecution) {

        String results = batchDao.logStepExecutions(stepExecution);

        return results;
    }

} // The End...
