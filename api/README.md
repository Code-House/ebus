# EBus Client

Goal of this module is definition of high level api for accessing EBus. This means that all low level details are hidden
from caller and abstract structures are used in favor of raw bytes.

This module specifies two abstract ideas:
- Device
- Property
    - PropertyName
    - PropertyValue

```
Device [1] --> [*] PropertyName [1] \ 
                                     \
                                      [1] Property
                                     /
                  PropertyValue [1] /
```

There is no circuit separation because it is specific to different systems. Client, as it is purely software thing, is
not bounded to any circuit and is not interested in circuits at all. It's EBus Client callers responsibility to decide how
devices or read properties are grouped into circuits.

### Device
Device is a free representation of an bus participant. EBus specifies two kinds of participants master and slave. Main
difference between these two is access to the bus - master can initiate communication while slave is only replying for
received requests. API does not reflect that, it groups master and slave into one device. Usually master and slave is
the same device and separation is dictated by physical access to bus.

### Property
Every master can emmit broadcast messages as well as slave can reply for data requests. Because we always know source of
every message we can link it with Device owning that property.
What is crucial from API point of view is fact that one property can represent multiple scalar values. There are composite
properties such as "schedule" which contains few ranges. There also other kinds of communications which may result in many
values stored in one property. This is device specific and caller must be aware of what he expects in return.
Another important point from client caller perspective is naming of properties. EBus specification uses bytes for representing
commands. This is not intuitive in case of high level communication thus every property have a name.