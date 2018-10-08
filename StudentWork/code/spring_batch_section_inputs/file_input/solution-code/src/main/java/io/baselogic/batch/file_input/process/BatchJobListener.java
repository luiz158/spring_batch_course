package io.baselogic.batch.file_input.process;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@SuppressWarnings({"Duplicates", "SpringJavaInjectionPointsAutowiringInspection"})
public class BatchJobListener implements JobExecutionListener {

	private Logger LOG = LoggerFactory.getLogger(BatchJobListener.class);
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOG.info("Job : {} has started", jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LOG.info("Job : {} has ended with status {}", 
				jobExecution.getJobInstance().getJobName(), 
				jobExecution.getExitStatus().getExitCode());		

	}

}
