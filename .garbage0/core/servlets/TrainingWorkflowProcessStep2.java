package com.epam.aem.core.servlets;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = WorkflowProcess.class,
        property = {
                "process.label=" + "Workflow Training Step 2"
        }
)
public class TrainingWorkflowProcessStep2 implements WorkflowProcess {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
            logger.info("Training process 2  executed: called from Java code");
            logger.info("Payload: " + workItem.getWorkflowData().getPayload());
            String message = workItem.getWorkflowData().getMetaDataMap().get("interstep.message", String.class);
            logger.info("Data received from process step 1: " + message);
    }
}
