package io.baselogic.batch.nested.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
public class BatchQueryDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public static final String LINE = "+" + new String(new char[55]).replace('\0', '-') + "+";

    public static final String columnFormat = "| %1$-20s ";
    public static final String resultFormat = "| %1$-30s |";

    private final String JOB_EXECUTIONS = "SELECT " +
            "je.JOB_EXECUTION_ID, je.JOB_INSTANCE_ID, ji.JOB_NAME, je.START_TIME, je.END_TIME, je.STATUS, je.EXIT_CODE, je.EXIT_MESSAGE, bjep.* " +
            "FROM BATCH_JOB_EXECUTION je inner join BATCH_JOB_INSTANCE ji inner join BATCH_JOB_EXECUTION_PARAMS bjep " +
            "where je.JOB_INSTANCE_ID=ji.JOB_INSTANCE_ID and je.JOB_EXECUTION_ID = bjep.JOB_EXECUTION_ID order by je.JOB_EXECUTION_ID desc";


    private final String STEP_EXECUTIONS = "SELECT " +
            "bse.STEP_EXECUTION_ID,bse.JOB_EXECUTION_ID,ji.JOB_NAME, bse.STEP_NAME, bse.START_TIME, bse.END_TIME, bse.COMMIT_COUNT, " +
            "bse.READ_COUNT, bse.WRITE_COUNT, bse.STATUS, bse.EXIT_MESSAGE, bse.LAST_UPDATED " +
            "FROM BATCH_STEP_EXECUTION bse inner join BATCH_JOB_INSTANCE ji inner join BATCH_JOB_EXECUTION je " +
            "where ji.JOB_INSTANCE_ID = je.JOB_INSTANCE_ID and bse.JOB_EXECUTION_ID = je.JOB_EXECUTION_ID order by bse.job_execution_id desc";


    /**
     * Get Job Executions for debugging
     *
     *
     * @return String formatted table of current Job Executions in the database
     */
    public String logJobExecutions() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n\n").append(LINE).append("\n");
        sb.append(String.format("|%s|", StringUtils.center("JOB EXECUTIONS (DB)", 55))).append("\n");
        sb.append(LINE).append("\n");
        sb.append(String.format("|%s|%s|", StringUtils.center("COLUMN", 22), StringUtils.center("RESULT", 32))).append("\n");
        sb.append(LINE).append("\n");

        return jdbcTemplate.query(JOB_EXECUTIONS, (rs) -> {
                while (rs.next()) {

                    sb.append( printRow("je.JOB_EXECUTION_ID", rs.getString(1)));
                    sb.append( printRow("je.JOB_INSTANCE_ID", rs.getString(2)));

                    sb.append( printRow("ji.JOB_NAME", rs.getString(3)));
                    sb.append( printRow("je.START_TIME", rs.getString(4)));
                    sb.append( printRow("je.END_TIME", rs.getString(5)));
                    sb.append( printRow("je.STATUS", rs.getString(6)));
                    sb.append( printRow("je.EXIT_CODE", rs.getString(7)));
                    sb.append( printRow("je.EXIT_MESSAGE", rs.getString(8)));
                    sb.append( printRow("bjep.*", rs.getString(9)));

                    sb.append(LINE).append("\n");
                }

                sb.append("\n");
                return sb.toString();
        });
    }



    public String logJobExecutions(JobExecution jobExecution) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n\n").append(LINE).append("\n");
        sb.append(String.format("|%s|", StringUtils.center("JOB EXECUTION", 55))).append("\n");
        sb.append(LINE).append("\n");
        sb.append(String.format("|%s|%s|", StringUtils.center("COLUMN", 22), StringUtils.center("RESULT", 32))).append("\n");
        sb.append(LINE).append("\n");

        sb.append( printRow("JOB_EXECUTION_ID", ""+jobExecution.getJobId()) );
        sb.append( printRow("JOB_INSTANCE_ID", ""+jobExecution.getJobInstance().getInstanceId()) );

        sb.append( printRow("JOB_NAME", ""+jobExecution.getJobConfigurationName()));
        sb.append( printRow("START_TIME", ""+jobExecution.getStartTime()));
        sb.append( printRow("END_TIME", ""+jobExecution.getEndTime()));
        sb.append( printRow("STATUS", ""+jobExecution.getStatus()));
        sb.append( printRow("EXIT_CODE", ""+jobExecution.getExitStatus().getExitCode()));
        sb.append( printRow("EXIT_MESSAGE", ""+jobExecution.getExitStatus().getExitDescription()));

        sb.append(LINE).append("\n");
        sb.append("\n\n");

        return sb.toString();
    }



    /**
     * Get Step Executions for debugging
     *
     * TODO: Need to add the following columns:
     *
     sb.append("rollbackCount: ").append(stepExecution.getRollbackCount()).append("\n");
     sb.append("readSkipCount: ").append(stepExecution.getReadSkipCount()).append("\n");
     sb.append("processSkipCount: ").append(stepExecution.getProcessSkipCount()).append("\n");
     sb.append("writeSkipCount: ").append(stepExecution.getWriteSkipCount()).append("\n");
     sb.append("exitStatus: ").append(stepExecution.getExitStatus()).append("\n");
     sb.append("terminateOnly: ").append(stepExecution.isTerminateOnly()).append("\n");
     sb.append("filterCount: ").append(stepExecution.getFilterCount()).append("\n");
     sb.append("failureExceptions: ").append(stepExecution.getFailureExceptions()).append("\n");
     *
     * @return String formatted table of current Step Executions in the database
     */
    public String logStepExecutions() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n\n").append(LINE).append("\n");
        sb.append(String.format("|%s|", StringUtils.center("STEP EXECUTIONS (DB)", 55))).append("\n");
        sb.append(LINE).append("\n");
        sb.append(String.format("|%s|%s|", StringUtils.center("COLUMN", 22), StringUtils.center("RESULT", 32))).append("\n");
        sb.append(LINE).append("\n");

        return jdbcTemplate.query(STEP_EXECUTIONS, (rs) -> {
            while (rs.next()) {

                sb.append(printRow("STEP_EXECUTION_ID", rs.getString(1)));

                sb.append( printRow("JOB_EXECUTION_ID", rs.getString(2)));
                sb.append( printRow("ji.JOB_NAME", rs.getString(3)));
                sb.append( printRow("STEP_NAME", rs.getString(4)));

                sb.append( printRow("START_TIME", rs.getString(5)));
                sb.append( printRow("END_TIME", rs.getString(6)));
                sb.append( printRow("COMMIT_COUNT", rs.getString(7)));
                sb.append( printRow("READ_COUNT", rs.getString(8)));
                sb.append( printRow("WRITE_COUNT", rs.getString(9)));

                sb.append( printRow("STATUS", rs.getString(10)));
                sb.append( printRow("EXIT_MESSAGE", rs.getString(11)));
                sb.append( printRow("LAST_UPDATED", rs.getString(11)));

//                sb.append(LINE).append("\n");
//                sb.append(String.format("|%s|", StringUtils.left("STATUS:", 55))).append("\n");
//                sb.append(LINE).append("\n");
//                sb.append(String.format("|%s|", StringUtils.left(""+rs.getString(10), 55))).append("\n");
                sb.append(LINE).append("\n");
            }

            sb.append("\n");
            return sb.toString();
        });
    }

    public String logStepExecutions(StepExecution stepExecution) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n\n").append(LINE).append("\n");
        sb.append(String.format("|%s|", StringUtils.center("STEP EXECUTION", 55))).append("\n");
        sb.append(LINE).append("\n");
        sb.append(String.format("|%s|%s|", StringUtils.center("COLUMN", 22), StringUtils.center("RESULT", 32))).append("\n");
        sb.append(LINE).append("\n");

        sb.append( printRow("JOB_EXECUTION_ID: ", ""+stepExecution.getJobExecutionId()) );
        sb.append( printRow("JOB_NAME: ", ""+stepExecution.getJobExecution().getJobConfigurationName()) );

        sb.append( printRow("STEP_NAME: ", stepExecution.getStepName()) );

        sb.append( printRow("STATUS: ", ""+stepExecution.getStatus()));
        sb.append( printRow("READ_COUNT: ", ""+stepExecution.getReadCount()));
        sb.append( printRow("WRITE_COUNT: ", ""+stepExecution.getWriteCount()));
        sb.append( printRow("COMMIT_COUNT: ", ""+stepExecution.getCommitCount()));
        sb.append( printRow("ROLLBACK_COUNT: ", ""+stepExecution.getRollbackCount()));
        sb.append( printRow("READ_SKIP_COUNT: ", ""+stepExecution.getReadSkipCount()));
        sb.append( printRow("PROCESS_SKIP_COUNT: ", ""+stepExecution.getProcessSkipCount()));
        sb.append( printRow("WRITE_SKIP_COUNT: ", ""+stepExecution.getWriteSkipCount()));
        sb.append( printRow("START_TIME: ", ""+stepExecution.getStartTime()));
        sb.append( printRow("END_TIME: ", ""+stepExecution.getEndTime()));
        sb.append( printRow("LAST_UPDATED: ", ""+stepExecution.getLastUpdated()));
        sb.append( printRow("TERMINATE_ONLY: ", ""+stepExecution.isTerminateOnly()));
        sb.append( printRow("FILTER_COUNT: ", ""+stepExecution.getFilterCount()));
        sb.append( printRow("FAILURE_EXCEPTIONS: ", ""+stepExecution.getFailureExceptions()));

        sb.append(LINE).append("\n");
        sb.append(String.format("|%s|", StringUtils.left("EXIT_STATUS:", 55))).append("\n");
        sb.append(LINE).append("\n");
        sb.append(String.format("|%s|", StringUtils.left(""+stepExecution.getExitStatus(), 55))).append("\n");
        sb.append(LINE).append("\n");
        sb.append("\n\n");

        return sb.toString();
    }





    private String printRow(String k, String v) {

        return (String.format(columnFormat, k)) + (String.format(resultFormat, v)) +"\n";

    }

} // The End...
