/*
 *
 *  Copyright 2012 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.simianarmy.aws.janitor.rule.alarm;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.netflix.simianarmy.MonkeyCalendar;
import com.netflix.simianarmy.Resource;
import com.netflix.simianarmy.client.aws.AWSClient;
import com.netflix.simianarmy.janitor.Rule;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * The rule is for checking whether an EBS volume is not attached to any instance and had the
 * DeleteOnTermination flag set in the previous attachment. This is an error case that AWS didn't
 * handle. The volume should have been deleted as soon as it was detached.
 * <p>
 * NOTE: since the information came from the history, the rule will work only if Edda is enabled
 * for Janitor Monkey.
 */
public class DeleteOnTerminationRule implements Rule {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteOnTerminationRule.class);

    private final String INSTANCE_ID_FIELDNAME = "InstanceId";

    private final MonkeyCalendar calendar;

    private final AWSClient awsClient;

    private final int retentionDays;

    private final InstanceState TERMINATED = new InstanceState().withCode(64).withName("terminated");

    /**
     * The termination reason for the DeleteOnTerminationRule.
     */
    public static final String TERMINATION_REASON = "Not attached and DeleteOnTerminate flag was set";

    /**
     * Constructor.
     */
    public DeleteOnTerminationRule(MonkeyCalendar calendar, int retentionDays, AWSClient awsClient) {
        Validate.notNull(calendar);
        Validate.isTrue(retentionDays >= 0);
        this.calendar = calendar;
        this.retentionDays = retentionDays;
        this.awsClient = awsClient;
    }

    @Override
    public boolean isValid(Resource resource) {
        Validate.notNull(resource);
        if (!"ALARM".equals(resource.getResourceType().name())) {
            return true;
        }

        String instanceId = resource.getAdditionalField(INSTANCE_ID_FIELDNAME);
        // If the referenced instance is terminated
        if (instanceId != null && !instanceId.isEmpty()) {
            Instance instance = awsClient.describeInstance(instanceId);
            if (instance == null || instance.getState().equals(TERMINATED)) {
                if (resource.getExpectedTerminationTime() == null) {
                    Date terminationTime = calendar.getBusinessDay(calendar.now().getTime(), retentionDays);
                    resource.setExpectedTerminationTime(terminationTime);
                    resource.setTerminationReason(TERMINATION_REASON);
                    LOGGER.info(String.format(
                            "Alarm %s is marked to be cleaned at %s as it is detached and DeleteOnTermination was set",
                            resource.getId(), resource.getExpectedTerminationTime()));
                } else {
                    LOGGER.info(String.format("Resource %s is already marked.", resource.getId()));
                }
                return false;
            }
        }
        return true;
    }
}
