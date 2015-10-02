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
//CHECKSTYLE IGNORE Javadoc

package com.netflix.simianarmy.aws.janitor.crawler;

import com.amazonaws.services.cloudwatch.model.MetricAlarm;
import com.amazonaws.services.ec2.model.Volume;
import com.netflix.simianarmy.Resource;
import com.netflix.simianarmy.aws.AWSResource;
import com.netflix.simianarmy.aws.AWSResourceType;
import com.netflix.simianarmy.client.aws.AWSClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAlarmJanitorCrawler {

    @Test
    public void testResourceTypes() {
        List<MetricAlarm> alarms = createAlarmList();
        AlarmJanitorCrawler crawler = new AlarmJanitorCrawler(createMockAWSClient(alarms));
        EnumSet<?> types = crawler.resourceTypes();
        Assert.assertEquals(types.size(), 1);
        Assert.assertEquals(types.iterator().next().name(), "ALARMS");
    }

    @Test
    public void testAlarmsWithNullNames() {
        List<MetricAlarm> alarms = createAlarmList();
        AlarmJanitorCrawler crawler = new AlarmJanitorCrawler(createMockAWSClient(alarms));
        List<Resource> resources = crawler.resources();
        verifyAlarms(resources, alarms);
    }

    @Test
    public void testAlarmsWithNames() {
        List<MetricAlarm> alarms = createAlarmList();
        String[] names = {"i-4129d8f9-cpu", "i-4129d8f9-diskspace-utilization"};
        AlarmJanitorCrawler crawler = new AlarmJanitorCrawler(createMockAWSClient(alarms, names));
        List<Resource> resources = crawler.resources(names);
        verifyAlarms(resources, alarms);
    }

    @Test
    public void testVolumesWithResourceType() {
        List<MetricAlarm> alarms = createAlarmList();
        AlarmJanitorCrawler crawler = new AlarmJanitorCrawler(createMockAWSClient(alarms));
        for (AWSResourceType resourceType : AWSResourceType.values()) {
            List<Resource> resources = crawler.resources(resourceType);
            if (resourceType == AWSResourceType.ALARMS) {
                verifyAlarms(resources, alarms);
            } else {
                Assert.assertTrue(resources.isEmpty());
            }
        }
    }

    private void verifyAlarms(List<Resource> resources, List<MetricAlarm> alarms) {
        Assert.assertEquals(resources.size(), alarms.size());
        for (int i = 0; i < resources.size(); i++) {
            MetricAlarm alarm = alarms.get(i);
            verifyAlarm(resources.get(i), alarm.getAlarmName());
        }
    }

    private void verifyAlarm(Resource alarm, String alarmName) {
        Assert.assertEquals(alarm.getResourceType(), AWSResourceType.ALARMS);
        Assert.assertEquals(alarm.getId(), alarmName);
        Assert.assertEquals(alarm.getRegion(), "us-east-1");
        Assert.assertEquals(((AWSResource) alarm).getAWSResourceState(), "OK");
    }

    private AWSClient createMockAWSClient(List<MetricAlarm> alarms, String... alarmNames) {
        AWSClient awsMock = mock(AWSClient.class);
        when(awsMock.describeAlarms(alarmNames)).thenReturn(alarms);
        when(awsMock.region()).thenReturn("us-east-1");
        return awsMock;
    }

    private List<MetricAlarm> createAlarmList() {
        List<MetricAlarm> alarms = new LinkedList<>();
        alarms.add(mkAlarm("i-4129d8f9-cpu"));
        alarms.add(mkAlarm("i-4129d8f9-diskspace-utilization"));
        return alarms;
    }

    private MetricAlarm mkAlarm(String alarmName) {
        return new MetricAlarm().withAlarmName(alarmName).withStateValue("OK");
    }

}
