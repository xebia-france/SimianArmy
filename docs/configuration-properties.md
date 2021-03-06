# Configuration Properties

What follows is an overview of all configuration properties you can set. See [Configuration](configuration.md) on how to pass properties to the Docker image.

Properties marked with an asterisk (\*) are custom properties added by this Docker image to simplify configuration, e.g. to avoid having to configure long Java class names.

## Client Properties

| Key | Default |
| --- | ------- |
| /simianarmy/client/aws/accountkey | |
| /simianarmy/client/aws/secretkey | |
| /simianarmy/client/aws/region | |
| /simianarmy/client/aws/assumerolearn | |
| /simianarmy/client/aws/accountname | |
| /simianarmy/client/aws/proxyhost | |
| /simianarmy/client/aws/proxyport | |
| /simianarmy/client/aws/proxyuser | |
| /simianarmy/client/aws/proxypassword | |
| /simianarmy/client/localdb/enabled\* | false |
| /simianarmy/client/cloudformationmode/enabled\* | false |

See https://github.com/Netflix/SimianArmy/wiki/Client-Settings for a detailed description of the properties.

## Global Properties

| Key | Default |
| --- | ------- |
| /simianarmy/recorder/sdb/domain | |
| /simianarmy/recorder/localdb/file | |
| /simianarmy/recorder/localdb/maxevents | |
| /simianarmy/recorder/localdb/password | |
| /simianarmy/scheduler/frequency | 1 |
| /simianarmy/scheduler/frequencyunit | HOURS |
| /simianarmy/scheduler/threads | 1 |
| /simianarmy/calendar/openhour | 9 |
| /simianarmy/calendar/closehour | 15 |
| /simianarmy/calendar/timezone | America/Los_Angeles |
| /simianarmy/calendar/ismonkeytime | false |
| /simianarmy/tags/owner | |
| /simianarmy/aws/email/region | |

See https://github.com/Netflix/SimianArmy/wiki/Global-Settings for a detailed description of the properties.

## Chaos Monkey Properties

| Key | Default |
| --- | ------- |
| /simianarmy/chaos/enabled | true |
| /simianarmy/chaos/leashed | true |
| /simianarmy/chaos/burnmoney | false |
| /simianarmy/chaos/terminateondemand/enabled | false |
| /simianarmy/chaos/mandatorytermination/enabled | false |
| /simianarmy/chaos/mandatorytermination/windowindays | |
| /simianarmy/chaos/mandatorytermination/defaultprobability | |
| /simianarmy/chaos/asg/enabled | false |
| /simianarmy/chaos/asg/probability | 1.0 |
| /simianarmy/chaos/asg/maxterminationsperday | 1.0 |
| /simianarmy/chaos/asgtag/key | |
| /simianarmy/chaos/asgtag/value | |
| /simianarmy/chaos/shutdowninstance/enabled | true |
| /simianarmy/chaos/blockallnetworktraffic/enabled | false |
| /simianarmy/chaos/detachvolumes/enabled | false |
| /simianarmy/chaos/burncpu/enabled | false |
| /simianarmy/chaos/burnio/enabled | false |
| /simianarmy/chaos/killprocesses/enabled | false |
| /simianarmy/chaos/nullroute/enabled | false |
| /simianarmy/chaos/failec2/enabled | false |
| /simianarmy/chaos/faildns/enabled | false |
| /simianarmy/chaos/faildynamodb/enabled | false |
| /simianarmy/chaos/fails3/enabled | false |
| /simianarmy/chaos/filldisk/enabled | false |
| /simianarmy/chaos/networkcorruption/enabled | false |
| /simianarmy/chaos/networklatency/enabled | false |
| /simianarmy/chaos/networkloss/enabled | false |
| /simianarmy/chaos/notification/global/enabled | false |
| /simianarmy/chaos/notification/sourceemail | |
| /simianarmy/chaos/notification/receiveremail | |

See https://github.com/Netflix/SimianArmy/wiki/Chaos-Settings for a detailed description of the properties. Also, consult https://github.com/Netflix/SimianArmy/wiki/The-Chaos-Monkey-Army to learn more about the different Chaos Monkey strategies.

## Janitor Monkey Properties

Janitor Monkey is disabled and cannot be configured at the moment.

## Conformity Monkey Properties

Conformity Monkey is disabled and cannot be configured at the moment.

## VolumeTagging Monkey Properties

VolumeTagging Monkey is disabled and cannot be configured at the moment.
