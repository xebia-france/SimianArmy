package com.netflix.simianarmy.aws.janitor.rule.alarm;

import com.netflix.simianarmy.Resource;
import com.netflix.simianarmy.aws.AWSResource;
import com.netflix.simianarmy.aws.AWSResourceType;
import com.netflix.simianarmy.aws.janitor.rule.TestMonkeyCalendar;
import com.netflix.simianarmy.client.aws.AWSClient;
import junit.framework.TestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class DeleteOnTerminationRuleTest extends TestCase {

    @Test
    public void testNonAlarmResource() {
        Resource resource = new AWSResource().withId("asg1").withResourceType(AWSResourceType.ASG);
        ((AWSResource) resource).setAWSResourceState("available");
        DeleteOnTerminationRule rule = new DeleteOnTerminationRule(new TestMonkeyCalendar(), 0, mock(AWSClient.class));
        Assert.assertTrue(rule.isValid(resource));
        Assert.assertNull(resource.getExpectedTerminationTime());
    }

    @Test
    public void testNotInstanceIdPresent() {
        Resource resource = new AWSResource()
                .withId("i-4129d8f9/xvda-diskspace-utilization")
                .withResourceType(AWSResourceType.ALARM);
        AWSClient client = mock(AWSClient.class);

        DeleteOnTerminationRule rule = new DeleteOnTerminationRule(new TestMonkeyCalendar(), 0, client);
        Assert.assertTrue(rule.isValid(resource));
        verify(client, never()).describeInstance(anyString());
    }

//    @Test
//
//    public void testInstanceNotExist() {
//        Resource resource = new AWSResource()
//                .withId("i-4129d8f9/xvda-diskspace-utilization")
//                .withResourceType(AWSResourceType.ALARM)
//                .setAdditionalField("instanceId", "i-4129d8f9");
//        AWSClient client = mock(AWSClient.class);
//        Instance instance = new Instance().withInstanceId("i-4129d8f9");
//        when(client.describeInstance("i-4129d8f9")).thenReturn(instance);
//        DateTime now = DateTime.now();
//        Date oldTermDate = new Date(now.plusDays(10).getMillis());
//
//        DeleteOnTerminationRule rule = new DeleteOnTerminationRule(new TestMonkeyCalendar(), 10, client);
//        boolean ruleIsValid = rule.isValid(resource);
//        verify(client).describeInstance(eq("i-4129d8f9"));
////        Assert.assertFalse(ruleIsValid);
//        Assert.assertEquals(oldTermDate, resource.getExpectedTerminationTime());
//    }
}