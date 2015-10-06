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
package com.netflix.simianarmy.aws.janitor.crawler;

import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricAlarm;
import com.netflix.simianarmy.Resource;
import com.netflix.simianarmy.ResourceType;
import com.netflix.simianarmy.aws.AWSResource;
import com.netflix.simianarmy.aws.AWSResourceType;
import com.netflix.simianarmy.client.aws.AWSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

/**
 * The crawler to crawl AWS CloudWatch alarms for janitor monkey.
 */
public class AlarmJanitorCrawler extends AbstractAWSJanitorCrawler {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmJanitorCrawler.class);

    /**
     * The constructor.
     * @param awsClient the AWS client
     */
    public AlarmJanitorCrawler(AWSClient awsClient) {
        super(awsClient);
    }

    @Override
    public EnumSet<? extends ResourceType> resourceTypes() {
        return EnumSet.of(AWSResourceType.ALARM);
    }

    @Override
    public List<Resource> resources(ResourceType resourceType) {
        if ("ALARM".equals(resourceType.name())) {
            return getAlarmResources();
        }
        return Collections.emptyList();
    }

    @Override
    public List<Resource> resources(String... alarmNames) {
        return getAlarmResources(alarmNames);
    }

    private List<Resource> getAlarmResources(String... alarmNames) {
        List<Resource> resources = new LinkedList<>();

        AWSClient awsClient = getAWSClient();

        for (MetricAlarm alarm: awsClient.describeAlarms(alarmNames)) {
            Resource alarmResource = new AWSResource()
                    .withId(alarm.getAlarmName())
                    .withRegion(getAWSClient().region())
                    .withResourceType(AWSResourceType.ALARM);
            ((AWSResource) alarmResource).setAWSResourceState(alarm.getStateValue());
            for(Dimension dim : alarm.getDimensions()){
                alarmResource.setAdditionalField(dim.getName(), dim.getValue());
            }
            resources.add(alarmResource);
        }
        return resources;
    }
}
