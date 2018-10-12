package io.baselogic.batch.common.endpoints;


import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class JobEndpoint {
    private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    private AtomicBoolean enabled = new AtomicBoolean(true);

    //---------------------------------------------------------------------------//
    // Lab: Note this endpoint URI '/launch'
    @GetMapping("/launch")
    public String launchJob(@RequestParam(value = "launchJob", required = false, defaultValue="true") boolean launchJob)
            throws JobExecutionException {
        enabled.set(launchJob);

        StringBuilder sb = new StringBuilder();
        // Note: Run job 1st time
        sb.append("Run 1: [\n").append(startSimpleJob()).append("]\n\n");
        // Note: Run job 2nd time
        sb.append("Run 2: [\n").append(startSimpleJob()).append("]\n\n");
        // Note: Run job 3rd time
        sb.append("Run 3: [\n").append(startSimpleJob()).append("]\n\n");
        return sb.toString();
    }

    //---------------------------------------------------------------------------//
    public String startSimpleJob() throws JobExecutionException {

        ExitStatus exitStatus = ExitStatus.UNKNOWN;
        String result = exitStatus.getExitCode();

        if (enabled.get()) {

            log.info("launching jobExecution...");

            JobExecution jobExecution = jobLauncher
                    .run(job,
                            // Note: Identical Job Parameters:
                            new JobParametersBuilder()
                                    .addLong("date", new Date().getTime())
                                    .toJobParameters());

            result = getJobExecutionDetails(jobExecution);

            log.info("...jobExecution completed");
            log.info(result);

        }
        return result;
    }

    public static String getJobExecutionDetails(JobExecution jobExecution){
        StringBuilder sb = new StringBuilder();

        sb.append("{ 'title' : '***Job Execution Details ***'").append(",");
        sb.append(" 'exit_status' : '").append(jobExecution.getExitStatus()).append("',");
        sb.append(" 'steps_executed' : '").append(jobExecution.getStepExecutions().size()).append("',");
        sb.append("}");

        return sb.toString();
    }

} // The End...