Time Series Data
================

Time-series data is data that is measured over time. One of the key problems with data over time is that not all data arrives at a convenient time interval. Consider, for example, two independent sensors recording data every ten seconds. One of those sensors may have started reporting data at 10:03'02 AM and another at 10:03'05 AM, so now you are receiving data that looks likes the following:
  
======== ====== =======
  Time   Sensor  Value
======== ====== =======
10:03'02   S1    54.223
10:03'05   S2     8.45
10:03'12   S1    54.678
10:03'15   S2    10.02
10:03'22   S1    54.100
10:03'25   S2     9.70
10:03'32   S1    54.988
10:03'35   S2     8.99
======== ====== =======

If you are wanting data to be normalized to one point every 30 seconds, this doesn't quite line up. To address this, the :java:type:`TimeSeries` data structure supports consolidation of data into discrete Time/Data pairs at a desired frequency within a specific window. 

Missing Data
------------

What happens, though, when you have missing data? 