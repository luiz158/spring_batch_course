package io.baselogic.batch.schedule;

import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledLauncher {

	@Autowired
	public JobOperator jobOperator;



	@Scheduled(fixedDelay = 5_000L)

	public void runJob() throws Exception {

	    // Need to have operator add instance to registrar
		this.jobOperator.startNextInstance("scheduledJob");
	}

} // The End...
