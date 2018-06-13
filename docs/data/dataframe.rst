Data Frames
===========

Data Frames are simply a two-dimensional table of data points that share a common index but different data values. This allows you to consolidate data into similar buckets, which is where the :java:type:`Series` interface comes in. Being able to convert data into a homogeneous structure makes many operations, particularly reporting, on your data much easier to do.  

For example, if you were recording data points from three different sensors, your data might look like:

.. table:: Recorded data

	======== ====== =======
	  Time   Sensor  Value
	======== ====== =======
	10:03      S1    54.223
	10:04      S2     8.450
	10:05      S1    54.678
	10:05      S2    10.020
	10:11      S1    54.100
	10:12      S2     9.700
	10:15      S3   180.000
	10:16      S2     8.990
	10:19      S1	 58.234
	======== ====== =======

Here, S3 only reported once, at 10:15 where the others reported much more frequently. We would record this data in three separate :java:type:`TimeSeries` objects, one for each sensor, so that the data is kept in-tact for each one. All data points retrieved, so long as they have a unique time-stamp (down to the millisecond) are, are stored in the :java:type:`TimeSeries` object with no data loss. 

Building a Data Frame
---------------------

DataFrame objects consist data points stored at indexed values; most likely the index will be time data, but can be anything that is sortable so that there is a clear concept of ordering in the DataFrame. For example, if we were to take our three independent TimeSeries objects from earlier and add them into a single DataFrame object, we would have a data frame that resembles the following: 

.. _data-frame:

.. table:: Data put into a DataFrame as-is

	======== ======= ======= ======= 
	  Time     S1      S2       S3
	======== ======= ======= =======
	10:03     54.223
	10:04             8.450
	10:05     54.678 10.020
	10:11     54.100
	10:12             9.700
	10:15                    180.000
	10:16             8.990
	10:19    58.234
	======== ======= ======= =======

Merging data into a :java:type:`DataFrame` object *does not* modify the underlying data in any way. 


Let's say that we wanted to produce a chart of all our sensor data at a 5-minute average resolution. You would need to write some code to organize the data into proper 5-minute buckets, and then perform some computation on them. This is exactly what the :java:type:`TimeSeries` class is designed to do. By using the TimeSeries' ability to downsample and time-box data, we can create a consistent index for our table of data. 

If we were to convert our data instead to 5-minute averages, it would look like this:

.. _five-minute-table:

.. table:: Data averaged into 5-minute intervals

	======== ======= ======= ======= 
	  Time     S1      S2       S3
	======== ======= ======= =======
	10:00     54.223  8.450
	10:05     54.678 10.020
	10:10     54.100  9.700
	10:15     58.234  8.990  180.000
	======== ======= ======= =======


Because each sensor only reported one data point per 5-minute interval, our data fits without any computation when being compacted into a matrix with a consistent time index. 

Filling in missing data
-----------------------

Oftentimes, and for a variety of reasons, the data gathered has holes in it. Perhaps a sensor missed recording a data point because it was low on power or busy; data was lost in transmission; or sometimes the granularity of our inputs don't line up. In these cases, particularly when wanting to present a consistent view of our data for charts and infographics, it is important to be able to accurately fill in the blanks where it makes sense. 

The :java:type:`DataFrame` class provides methods for filling in the data holes. Currently it supports only simple backfill methods, where a copy of a data point is migrated forwards or backwards (or both) to fill in the gaps. In the future, there will be an option to use additional models for filling in data such as linear appromiximation. 

If we were to take the data in :ref:`five-minute-table` and back-fill our data, we would end up with the following data frame: 

======== ======= ======= ======= 
  Time     S1      S2       S3
======== ======= ======= =======
10:00     54.223  8.450  180.000
10:05     54.678 10.020  180.000
10:10     54.100  9.700  180.000
10:15     58.234  8.990  180.000
======== ======= ======= =======

Here, when we have empty slots, we use the next non-null value to back-fill the missing data. This provides us with a complete data-frame so that a plot of our data makes more sense. Alternatively, we can leverage forward-filling to do the opposite, as needed. 


