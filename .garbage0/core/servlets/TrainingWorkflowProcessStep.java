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
                "process.label=" + "Workflow Training Step 1"
        }
)
public class TrainingWorkflowProcessStep implements WorkflowProcess {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
            logger.info("Training process executed: called from Java code");
            logger.info("Payload: " + workItem.getWorkflowData().getPayload());
            workItem.getWorkflowData().getMetaDataMap().put("interstep.message", "Hello from step 1");
    }
}
