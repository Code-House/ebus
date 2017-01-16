# EBus Server
This software is EBus handling software which allows to work with high and low level access to EBus devices. Basic 
architecture of this software is as following:

```
High level     EBusClient   --->    Property Map  ---> Property, Property Name
                   |                      |                  \  /
Mid level      EBusCodec    ---> Command Registry ---> Property Value, Converter
                   |
Low level     Transmittable ---> Header, Data
```

Intention of this project is provisioning of cohesive API which allows to send/receive and also intercept and track ebus
communication. Because most of vendors do not publish their protocol details and standard ebus telegrams are not commonly
used, only one way to get knowledge what happens is by reverse engineering. Job was already done and there are amazing
results produced by @john30 in [ebusd project](https://github.com/john30/ebusd/). If you are looking for linux daemon that's
address you should visit first.