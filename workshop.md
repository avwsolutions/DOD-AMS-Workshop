<a id="top"></a>
<img src="https://raw.githubusercontent.com/avwsolutions/DOD-AMS-Workshop/master/content/header.png" alt="workshop header">

<a href="#top" class="top" id="getting-started">Top</a>
## Getting Started: FAQs

### What is a IT Operational Data Lake?

As Wikipedia describes 

*“A data lake is a method of storing data within a system that facilitates the colocation of data in variant schemas and structural forms, usually object blobs or files. The main challenge is not creating a data lake, but taking advantages of the opportunities it presents of unsiloed data”*

To make it more IT Operations specific.

An IT Operational data lake is a method of storing and structuring IT landscape availability, performance, transaction and usability data and take the advantage to improve the quality and experience of customer journeys through your value chains. Which then can be seen as next-level chain monitoring awareness. Main opportunities are to become more proactive and even predictive in your monitoring approach by using it for diagnose, analytics and even machine learning. This dataset (using the metadata) can then be easily accessed by visualizations or alerting mechanisms.

To support all this functionality, we need tools and technology that near real-time can support big loads of monitoring data like events, metrics and health-states. 

### Which tools and technologies are used?

Our preferred tooling and technology choices for that are ELK (Elasticsearch-Logstash-Kibana) for events and Graphite/Grafana for your metrics. To easily integrate your monitoring within your application stacks we choose Kafka as message broker technology.

### What will this workshop teach me?

This workshop aims to be the one-stop shop for getting your hands dirty. Since this is a hands-on workshop we will start with easily installing the software components like Elasticsearch, Logstash, Kibana, Graphite and Grafana.
Apart from the installation of our main components we will setup some of the code that is needed for setting up the data flow from log sources. We will end this with a simple, but effective dashboard.
At the end we will setup some code that needed to near real-time send events and metrics without parsing it from log sources. Instead we will use the Kafka Broker technology.
First we will simulate a Producer for events and metrics. At the end we will create consumers, which will put the normalized data in the data lake.

Even if you have no prior experience with these tools, this workshop should be all you need to get started.

