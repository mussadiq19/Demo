package com.sovai.platform.config;

import com.sovai.platform.domain.risk.scheduler.RiskScannerJob;
import com.sovai.platform.domain.skills.scheduler.SkillsAnalysisJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class QuartzConfig {

    @Value("${quartz.risk-scan-cron}")
    private String riskScanCron;

    @Value("${quartz.skills-analysis-cron}")
    private String skillsAnalysisCron;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        return schedulerFactoryBean;
    }

    @Bean
    public JobDetail riskScannerJobDetail() {
        return JobBuilder.newJob(RiskScannerJob.class)
                .withIdentity("riskScannerJob")
                .usingJobData("companyId", 1L)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger riskScannerTrigger(JobDetail riskScannerJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(riskScannerJobDetail)
                .withIdentity("riskScannerTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(riskScanCron))
                .build();
    }

    @Bean
    public JobDetail skillsAnalysisJobDetail() {
        return JobBuilder.newJob(SkillsAnalysisJob.class)
                .withIdentity("skillsAnalysisJob")
                .usingJobData("companyId", 1L)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger skillsAnalysisTrigger(JobDetail skillsAnalysisJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(skillsAnalysisJobDetail)
                .withIdentity("skillsAnalysisTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(skillsAnalysisCron))
                .build();
    }

    private Properties quartzProperties() {
        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", "SovaiScheduler");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        properties.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
        properties.setProperty("org.quartz.jobStore.useProperties", "false");
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quartz.threadPool.threadCount", "10");
        return properties;
    }
}
