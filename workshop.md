<a id="top"></a>
<img src="https://raw.githubusercontent.com/avwsolutions/DOD-AMS-Workshop/master/content/header.png" alt="workshop header">

<a href="#top" class="top" id="getting-started">Top</a>
## Getting Started: FAQs

### What is a IT Operational Data Lake?

As [Wikipedia] (https://en.wikipedia.org/wiki/Data_lake) describes 

*“A data lake is a method of storing data within a system that facilitates the colocation of data in variant schemas and structural forms, usually object blobs or files. The main challenge is not creating a data lake, but taking advantages of the opportunities it presents of unsiloed data”*

To make it more IT Operations specific.

An **IT Operational data lake** is a method of storing and structuring **IT landscape availability, performance, transaction and usability data** and **take** the **advantage** to **improve** the **quality** and **experience** of **customer journeys** through your **value chains**. Which then can be seen as **next-level chain monitoring awareness**. Main opportunities are to become more **proactive** and even **predictive** in your monitoring approach by using it for **diagnose, analytics** and even **machine learning**. This **dataset** (using the **metadata**) can then be easily accessed by **visualizations** or **alerting** mechanisms.

To support all this functionality, we need tools and technology that near real-time can support big loads of monitoring data like events, metrics and health-states. 

### Which tools and technologies are used?

Our preferred tooling and technology choices for that are ELK (Elasticsearch-Logstash-Kibana) for events and Graphite/Grafana for your metrics. To easily integrate your monitoring within your application stacks we choose Kafka as message broker technology.

### What will this workshop teach me?

This workshop aims to be the one-stop shop for getting your hands dirty. Since this is a hands-on workshop we will start with easily installing the software components like Elasticsearch, Logstash, Kibana, Graphite and Grafana.
Apart from the installation of our main components we will setup some of the code that is needed for setting up the data flow from log sources. We will end this with a simple, but effective dashboard.
At the end we will setup some code that needed to near real-time send events and metrics without parsing it from log sources. Instead we will use the Kafka Broker technology.
First we will simulate a Producer for events and metrics. At the end we will create consumers, which will put the normalized data in the data lake.

Even if you have no prior experience with these tools, this workshop should be all you need to get started.

## Using this Document

This document contains a series of several sections, each of which explains a particular aspect of the data lake solution. In each section, you will be typing commands (or writing code). All the code used in the workshop is available in the [Github repo] (https://github.com/avwsolutions/DOD-AMS-Workshop).

<a href="#top" class="top" id="table-of-contents">Top</a>
## Table of Contents

- [Preface](#preface)
    - [Prerequisites](#prerequisites)
    - [Setting up your computer](#setup)
-   [1.0 Installing and initial configuration of the first components](#first)
    -   [1.1 Install & configure Elasticsearch](#elasticsearch)
    -   [1.2 Install & configure Logstash](#logstash)
    -   [1.3 Install & configure Kibana](#kibana)
    -   [1.4 Install & configure Graphite](#graphite)
    -   [1.5 Install & configure Grafana](#grafana)
-   [2.0 Setting up your first logstash configuration](#bankitlog)
    -   [2.1 Scenario](#case)
    -   [2.2 Docker Images](#docker-images)
    -   [2.3 Our First Image](#our-image)
    -   [2.4 Dockerfile](#dockerfiles)
-  [3.0 Birthday training](#dockercompetition)
  - [3.1 Pull voting-app images](#pullimage)
  - [3.2 Customize the App](#customize)
    - [3.2.1 Modify app.py](#modifyapp)
    - [3.2.2 Modify config.json](#modifyconfig)
    - [3.2.3 Building and running the app](#buildvotingapp)
    - [3.2.4 Build and tag images](#buildandtag)
    - [3.2.5 Push images to Docker Hub](#pushimages)
  - [3.3 Enter competition](#confirmtraining)
  - [3.4 Check your submission status](#checkstatus)
-  [4.0 Next Steps: Docker Birthday #3 App Challenge](#wrap-up)
-  [References](#references)

------------------------------
<a href="#table-of-contents" class="top" id="preface">Top</a>
## Preface

> Note: This workshop uses the folowing versions.
- [Elasticsearch](https://www.elastic.co/downloads/elasticsearch), version **2.3.3**
- [Logstash](https://www.elastic.co/downloads/logstash), version **2.3.3**
- [Kibana](https://www.elastic.co/downloads/kibana), version **4.5.1**
- [Graphite](https://github.com/graphite-project), version **0.9.15**
- [Grafana](http://grafana.org/download/), version **3.0.4**
- [Kafka](http://kafka.apache.org/downloads.html), version **.0.10.0.0**

If you find any part of the workshop exercises incompatible with a future version, please raise an [issue](https://github.com/avwsolutions/DOD-AMS-Workshop/issues). Thanks!

<a id="prerequisites"></a>
### Prerequisites

There are no specific skills needed for this workshop beyond a basic comfort with the command line and using a text editor. Prior experience in implementing monitoring applications will be helpful but is not required. As you proceed further along the workshop, we'll make use of Github.

<a id="setup"></a>
### Setting up your computer
Setting up a consistent test environment can be a time consuming task, but thankfully with use of Vagrant this is a very easy task. Benefits of Vagrant are the consistent workflow and the easiness of environment lifecycle, like disposal.
The [installation](https://www.vagrantup.com/docs/installation/) and [getting started guide](https://www.vagrantup.com/docs/getting-started/) has detailed instructions for setting up Vagrant on Mac, Linux and Windows.

To spin off the VM go to the directory where you saved the .box & Vagrantfile and run the following commands

```
$vagrant up
Bringing machine 'datalake' up with 'virtualbox' provider...
==> datalake: Importing base box 'DOD-AMS-WORKSHOP'...
==> datalake: Matching MAC address for NAT networking...
==> datalake: Setting the name of the VM: DOD-AMS_datalake_1466797115802_30773
==> datalake: Clearing any previously set network interfaces...
==> datalake: Preparing network interfaces based on configuration...
    datalake: Adapter 1: nat
    datalake: Adapter 2: hostonly
==> datalake: Forwarding ports...
    datalake: 22 (guest) => 10123 (host) (adapter 1)
    datalake: 80 (guest) => 9080 (host) (adapter 1)
    datalake: 5601 (guest) => 5601 (host) (adapter 1)
    datalake: 3000 (guest) => 3000 (host) (adapter 1)
    datalake: 2003 (guest) => 2003 (host) (adapter 1)
    datalake: 2003 (guest) => 2003 (host) (adapter 1)
    datalake: 9200 (guest) => 9200 (host) (adapter 1)
    datalake: 2003 (guest) => 2181 (host) (adapter 1)
    datalake: 2003 (guest) => 9092 (host) (adapter 1)
==> datalake: Booting VM...
==> datalake: Waiting for machine to boot. This may take a few minutes...
    datalake:** SSH address: 127.0.0.1:10123**
    datalake:** SSH username: vagrant**
    datalake:** SSH auth method: private key**
    datalake: 
    datalake: Vagrant insecure key detected. Vagrant will automatically replace
    datalake: this with a newly generated keypair for better security.
    datalake: 
    datalake: Inserting generated public key within guest...
    datalake: Removing insecure key from the guest if it's present...
    datalake: Key inserted! Disconnecting and reconnecting using new SSH key...
==> datalake: Machine booted and ready!
==> datalake: Checking for guest additions in VM...
    datalake: No guest additions were detected on the base box for this VM! Guest
    datalake: additions are required for forwarded ports, shared folders, host only
    datalake: networking, and more. If SSH fails on this machine, please install
    datalake: the guest additions and repackage the box to continue.
    datalake: 
    datalake: This is not an error message; everything may continue to work properly,
    datalake: in which case you may ignore this message.
==> datalake: Setting hostname...
==> datalake: Configuring and enabling network interfaces...
==> datalake: Mounting shared folders...
    datalake: /home/vagrant/sync => /Users/avwinbox/VagrantBoxes/DOD-AMS
```
> Note: For Windows user the ssh command isn't available by default. You can PuTTY to connect to the local created VM by address 127.0.0.1 with port 10123.
Default username & password are 'vagrant/vagrant'

```
$vagrant ssh
Last login: Sun Jun 19 21:00:06 2016
-bash: warning: setlocale: LC_CTYPE: cannot change locale (UTF-8): No such file or directory
[vagrant@datalake ~]$
```

<a id="first"></a>
## 1.0 Installing and initial configuration of the first components

Now that you have everything setup, it's time to get our hands dirty. In this first section you will install and setup all first used tools. For this some configuration templates be helpful, which are part of the [DOD-AMS-Workshop](https://github.com/avwsolutions/DOD-AMS-Workshop) repository.

> Note : Be aware that for this task internet connectivity is needed. For convenience there is already a cache version available.

To get started type the following command in your terminal
```
$ sudo su -
$ cd /usr/local/src
$ git clone https://github.com/avwsolutions/DOD-AMS-Workshop
```

