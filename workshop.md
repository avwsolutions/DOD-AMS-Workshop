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
-   [2.0 BankIT Scenario : part1 ](#bankit1)
    -   [2.1 Your first logstash configuration](#basics)
    -   [2.2 Connecting the syslog](#syslog)
    -   [2.3 Our application logs](#logfile)
    -   [2.4 Using grok filtering](#grok)
    -   [2.5 Our first kibana dashboard](#fkibana)
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
# cd /usr/local/src
# git clone https://github.com/avwsolutions/DOD-AMS-Workshop
```

<a id="elasticsearch"></a>
## 1.1 Install & configure Elasticsearch

> Note : Elastic recommends using Oracle Java over OpenJDK. In this workshop we will use OpenJDK which also is supported.

Below the commands for installing the openJDK JRE and checking if the path & version (1.7 or greater) are correct. 

```
$ sudo yum -y install jre
$ sudo java -version
openjdk version "1.8.0_91"
OpenJDK Runtime Environment (build 1.8.0_91-b14)
OpenJDK 64-Bit Server VM (build 25.91-b14, mixed mode)
```

> Note : Be aware that for this task internet connectivity is needed. For convenience the Elastic repository is configured and there is already a cache yum download available.

Below the commands for installing elasticsearch. 

```
$ sudo yum -y install elasticsearch
```

Now we will configure the elasticsearch instance to only accept local port access. Be aware of this when connecting from your Computer.

```
$ sudo vi /etc/elasticsearch/elasticsearch.yml
```

Add the uncommented line to the YML file.

```
# Set the bind address to a specific IP (IPv4 or IPv6):
#
# network.host: 192.168.0.1
**network.host: localhost**
#
# Set a custom port for HTTP:
#
# http.port: 9200
```
Since we run a local firewall we need to enable access to the port

```
$ sudo su -
# cd /usr/lib/firewalld/services
# vi elasticsearch.xml

<?xml version="1.0" encoding="utf-8"?>
<service>
  <short>Elasticsearch</short>  <description>Elasticsearch server REST API, which is based on http traffic.</description>
  <port protocol="tcp" port="9200"/>
</service>

# firewall-cmd --permanent --add-service elasticsearch
# firewall-cmd --reload
```

At last we can configure the service configuration and start the service. Notice it is using systemd

```
$ sudo systemctl daemon-reload
$ sudo systemctl enable elasticsearch.service
$ sudo systemctl start elasticsearch.service
```

<a id="logstash"></a>
## 1.2 Install & configure Logstash 

> Note : Be aware that for this task internet connectivity is needed. For convenience the Elastic repository is configured and there is already a cache yum download available.

Below the commands for installing logstash.

```
$ sudo yum -y install logstash 
```

At last we can configure the service configuration. Notice it is not using systemd.

```
$ sudo systemctl daemon-reload
$ sudo systemctl enable logstash.service
```

<a id="kibana"></a>
## 1.3 Install & configure Kibana

> Note : Be aware that for this task internet connectivity is needed. For convenience the Elastic repository is configured and there is already a cache yum download available.

Below the commands for installing kibana.

```
$ sudo yum -y install kibana 
```
Since we run a local firewall we need to enable access to the port

```
$ sudo su -
# cd /usr/lib/firewalld/services
# vi kibana.xml

<?xml version="1.0" encoding="utf-8"?>
<service>
  <short>Kibana</short>  <description>Kibana Appl server, which is based on http traffic.</description>
  <port protocol="tcp" port="5601"/>
</service>

# firewall-cmd --permanent --add-service kibana 
# firewall-cmd --reload
```

At last we can configure the service configuration. Notice it is using systemd.

```
$ sudo systemctl daemon-reload
$ sudo systemctl enable kibana.service
$ sudo systemctl start kibana.service
```

<a id="graphite"></a>
## 1.4 Install & configure Graphite

> Note : Be aware that for this task internet connectivity is needed. For convenience the Epel repository is configured and there is already a cache yum download available.

Below the commands for installing Graphite. As you can see Graphite has more things to taken care.
First we are going to install the dependencies and mandatory web server, python tools and compilers.

```
$ sudo yum -y install httpd gcc gcc-c++ git pycairo mod_wsgi epel-release
$ sudo yum -y install python-pip python-devel blas-devel lapack-devel libffi-devel
```
Now we will download the latest graphite packages from github.

> Note : Be aware that for this task internet connectivity is needed. For convenience there is already a cache version available. 

```
$ sudo su -
# cd /usr/local/src
# git clone https://github.com/graphite-project/graphite-web.git
# git clone https://github.com/graphite-project/carbon.git
```
Now that we have the sources we are ready to install the binaries. This is done through the `pip` command. Since there are some changes "[syncdb deprecated]"(https://docs.djangoproject.com/en/1.7/topics/migrations/) in Django 1.9.x release, it is mandatory to first update the requirements.txt input file.

```
$ sudo su -
# vi /usr/local/src/graphite-web/requirements.txt

# old value => Django>=1.4
Django==1.8
```
Now start the `pip` installer.

```
$ sudo su -
# pip install -r /usr/local/src/graphite-web/requirements.txt

#verify if Django has the right version

# python
Python 2.7.5 (default, Nov 20 2015, 02:00:19) 
[GCC 4.8.5 20150623 (Red Hat 4.8.5-4)] on linux2
Type "help", "copyright", "credits" or "license" for more information.
>>> import django
>>> django.VERSION
(1, 8, 0, 'final', 0)
>>> quit()

```

Next steps are running the setup script for installing carbon.

```
$ sudo su -
# cd /usr/local/src/carbon/
# python setup.py install
```
Now repeat these steps for graphite-web.

```
$ sudo su -
# cd /usr/local/src/graphite-web/
# python setup.py install
```

Copy over all template configurations (graphite-web and carbon-*) and setup the service startup files.

```
$ sudo cp /opt/graphite/conf/carbon.conf.example /opt/graphite/conf/carbon.conf
$ sudo cp /opt/graphite/conf/storage-schemas.conf.example /opt/graphite/conf/storage-schemas.conf
$ sudo cp /opt/graphite/conf/storage-aggregation.conf.example /opt/graphite/conf/storage-aggregation.conf
$ sudo cp /opt/graphite/conf/relay-rules.conf.example /opt/graphite/conf/relay-rules.conf
$ sudo cp /opt/graphite/conf/aggregation-rules.conf.example /opt/graphite/conf/aggregation-rules.conf
$ sudo cp /opt/graphite/webapp/graphite/local_settings.py.example /opt/graphite/webapp/graphite/local_settings.py
$ sudo cp /opt/graphite/conf/graphite.wsgi.example /opt/graphite/conf/graphite.wsgi
$ sudo cp /opt/graphite/examples/example-graphite-vhost.conf /etc/httpd/conf.d/graphite.conf

# Copy over service startup scripts and make them executable
 
$ sudo cp /usr/local/src/carbon/distro/redhat/init.d/carbon-* /etc/init.d/
$ sudo chmod +x /etc/init.d/carbon-*

```

Now it is time to configure graphite-web component. This step is import because we will now create the local SQLite DB file for graphite-web, which is for administrative purposes. Don't be confused this is not the database where the metrics are stored.To have minimized security you're asked to fill in the username and password. Please leave the username default '*root*' and choose '*password*' as password.

```
$ sudo su -
# cd /opt/graphite
# PYTHONPATH=/opt/graphite/webapp/ django-admin.py syncdb --settings=graphite.settings
```

> Response : Yes, accept default '*root*', leave the email address '*empty*' and use '*password*' as password.

Also the static content will be generated. Also don't forget to set the correct owner for the *httpd* server.

> Note : Currently we are running in SELinux *Permissive* mode instead of *Enforcing*.In production environments always move your webserver content to the */var/www/html* or implement the correct *SELinux* acccess control Security contexts. 

```
$ sudo su -
# cd /opt/graphite
# PYTHONPATH=/opt/graphite/webapp/ django-admin.py collectstatic --settings=graphite.settings

# chown -R apache:apache /opt/graphite/storage/
# chown -R apache:apache /opt/graphite/static/
# chown -R apache:apache /opt/graphite/webapp/

```
Now we have to update the firewall configuration again.

```
$ sudo su -
# cd /usr/lib/firewalld/services
# vi carbonrelay.xml

<?xml version="1.0" encoding="utf-8"?>
<service>
  <short>Carbon-relay</short>  <description>Port for retrieving metrics for various sources.</description>
  <port protocol="tcp" port="2003"/>
  <port protocol="udp" port="2003"/>
</service>

# firewall-cmd --permanent --add-service http
# firewall-cmd --permanent --add-service carbonrelay 
# firewall-cmd --reload

```
Since there default virtual host configuration template for *httpd* is not working due CentOS restrictions (httpd 2.4), we have to add some additional configuration.

**Correct configuration**

- Add the following configuration for the '*/opt/graphite/static*' directory

```
<Directory /opt/graphite/static/>
 	<IfVersion < 2.4>
                         Order deny,allow
                         Allow from all
                 </IfVersion>
                 <IfVersion >= 2.4>
                         Require all granted
                 </IfVersion>
         </Directory>
```

- And uncomment the following lines

```
	# WSGIScriptAlias /graphite /srv/graphite-web/conf/graphite.wsgi/graphite
        # Alias /graphite/static /opt/graphite/webapp/content
        #  <Location "/graphite/static/">
        #        SetHandler None
        # </Location>
```

```
$ sudo vi /etc/httpd/conf.d/graphite.conf
...

<VirtualHost *:80>
        ServerName graphite
        DocumentRoot "/opt/graphite/webapp"
        ErrorLog /opt/graphite/storage/log/webapp/error.log
        CustomLog /opt/graphite/storage/log/webapp/access.log common

        # I've found that an equal number of processes & threads tends
        # to show the best performance for Graphite (ymmv).
        WSGIDaemonProcess graphite processes=5 threads=5 display-name='%{GROUP}' inactivity-timeout=120
        WSGIProcessGroup graphite
        WSGIApplicationGroup %{GLOBAL}
        WSGIImportScript /opt/graphite/conf/graphite.wsgi process-group=graphite application-group=%{GLOBAL}

        # XXX You will need to create this file! There is a graphite.wsgi.example
        # file in this directory that you can safely use, just copy it to graphite.wgsi
        WSGIScriptAlias / /opt/graphite/conf/graphite.wsgi


        # XXX To serve static files, either:
        # * Install the whitenoise Python package (pip install whitenoise)
        # * Collect static files in a directory by running:
        #     django-admin.py collectstatic --noinput --settings=graphite.settings
        #   And set an alias to serve static files with Apache:
        Alias /static/ /opt/graphite/static/

        <Directory /opt/graphite/static/>
        <IfVersion < 2.4>
                        Order deny,allow
                        Allow from all
                </IfVersion>
                <IfVersion >= 2.4>
                        Require all granted
                </IfVersion>
        </Directory>
        ########################
        # URL-prefixed install #
        ########################
        # If using URL_PREFIX in local_settings for URL-prefixed install (that is not located at "/"))
        # your WSGIScriptAlias line should look like the following (e.g. URL_PREFX="/graphite"

        WSGIScriptAlias /graphite /srv/graphite-web/conf/graphite.wsgi/graphite
        Alias /graphite/static /opt/graphite/webapp/content
        <Location "/graphite/static/">
               SetHandler None
        </Location>
	# XXX In order for the django admin site media to work you
        # must change @DJANGO_ROOT@ to be the path to your django
        # installation, which is probably something like:
        # /usr/lib/python2.6/site-packages/django
        Alias /media/ "@DJANGO_ROOT@/contrib/admin/media/"

        # The graphite.wsgi file has to be accessible by apache. It won't
        # be visible to clients because of the DocumentRoot though.
        <Directory /opt/graphite/conf/>
                <IfVersion < 2.4>
                        Order deny,allow
                        Allow from all
                </IfVersion>
                <IfVersion >= 2.4>
                        Require all granted
                </IfVersion>
        </Directory>

</VirtualHost>

```




At last we can configure the service configuration. Notice it is not using systemd.

```
$ sudo systemctl daemon-reload
$ sudo systemctl enable httpd.service
$ sudo systemctl start httpd.service
$ curl http://localhost
```

```
$ sudo systemctl enable carbon-relay
$ sudo systemctl enable carbon-aggregator
$ sudo systemctl enable carbon-cache
$ sudo systemctl start carbon-relay
$ sudo systemctl start carbon-aggregator
$ sudo systemctl start carbon-cache

```

<a id="grafana"></a>
## 1.5 Install & configure Grafana

> Note : Be aware that for this task internet connectivity is needed. For convenience the Grafana repository is configured and there is already a cache yum download available.

Below the commands for installing grafana.

```
$ sudo yum -y install grafana 
```
Since we run a local firewall we need to enable access to the port

```
$ sudo su -
# cd /usr/lib/firewalld/services
# vi grafana.xml

<?xml version="1.0" encoding="utf-8"?>
<service>
  <short>Grafana</short>  <description>Grafana Appl server, which is based on http traffic.</description>
  <port protocol="tcp" port="3000"/>
</service>

# firewall-cmd --permanent --add-service grafana
# firewall-cmd --reload
```

At last we can configure the service configuration. Notice it is using systemd.

```
$ sudo systemctl daemon-reload
$ sudo systemctl enable grafana-server.service
$ sudo systemctl start grafana-server.service
$ curl http://localhost:3000/login
```
<a id="bankit1"></a>
## 2.0 BankIT Scenario : part1 

You are hired as an experienced Engineer at a new FINTEC startup, named Bank IT. You are part of the DevOps team which delivers the core Cloud Platform and is used by the BankIT application. Within the current sprint you are designated to help Kenny setting up the ITOps data lake. Kenny already has setup the functional needs, but has a lack of technical skills.

in this part of the scenario we have to gather the events from the Middleware and Operating System.BankIT itself is a web based application, which has Tomcat application server as Middleware and Linux as Operating System. 

**Below the functional needs:**

- All Operating System messages (syslog) must be available in the data lake.
- All Middleware messages must be available in the data lake.
- Security related messages (facility 4/10 - audit) must be masked.
- Original timestamp must be used from source ( not on arrival )

<a id="basics"></a>
## 2.1 Your first logstash configuration 

Now it is finally time to play around with logstash. If you never used logstash, these excercises are there to be familiar with the syntax. Otherwise skip this section and start at <a href="#syslog" class="syslog" id="basics">2.2 Connecting the syslog</a>

Do your first inline code with LogStash using your standard input (command) and output (screen) channels.

```
$ /opt/logstash/bin/logstash -e 'input { stdin { } } output { stdout {} }'
```
After the logstash agent is initialized, it is waiting for input. Now type in '*Hello DevOpsDays AMS2016*' and press <ENTER>.
You will see logstash to respond to that with a message.

```
Settings: Default pipeline workers: 1
Pipeline main started
Hello DevOpsDays AMS2016
2016-06-25T02:16:59.848Z datalake.monitor.now Hello DevOpsDays AMS2016
```

No close the worker `CTRL-C` and add some additional code. Do you see what are the differences ?

```
/opt/logstash/bin/logstash -e 'input { stdin { } } filter { mutate { gsub => [ "message","DevOpsDays.*","Student" ] } }output { stdout { codec => json } }'
``` 
You will see logstash to respond to that with a message.

```
Settings: Default pipeline workers: 1
Pipeline main started
Hello DevOpsDays AMS2016
{"message":"Hello Student","@version":"1","@timestamp":"2016-06-25T02:28:36.809Z","host":"datalake.monitor.now"}
```
Last thing I want you to show is the use of conditional support. As with programming languages the if/if else/else can be used with a chosen expression.
Now first create a directory, for example '*/tmp/example*' and create three files as listed below.

> Note : Logstash can run code from one file or a directory which we show below. Be aware of the correct naming that input code is always imported first.

```
000-input.conf

input { stdin { } }
```

```
500-filter.conf

filter {
	if [message] == "carrot" {
     		mutate { add_field => { "guess" => "You are a Bugs Bunny" } }
	} else if [message] == "pizza" {
     		mutate { add_field => { "guess" => "You are Mario Bros" } }
	} else {
     		mutate { add_field => { "guess" => "Guessing seems impossible" } }
	}
}
```

```
900-output.conf

output {
  #elasticsearch { hosts => ["localhost:9200"] }
  stdout { codec => rubydebug }
}
```

Now start the code by the command below en play with the input [carrot, pizza or other]

```
/opt/logstash/bin/logstash  -f /tmp/example
```






