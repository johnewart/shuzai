.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.joda.time DateTime

.. java:import:: org.slf4j Logger

.. java:import:: org.slf4j LoggerFactory

.. java:import:: java.math BigDecimal

.. java:import:: java.math RoundingMode

TimeSeries
==========

.. java:package:: net.johnewart.shuzai
   :noindex:

.. java:type:: public class TimeSeries implements Series<DateTime, BigDecimal>

Constructors
------------
TimeSeries
^^^^^^^^^^

.. java:constructor:: public TimeSeries()
   :outertype: TimeSeries

TimeSeries
^^^^^^^^^^

.. java:constructor:: public TimeSeries(List<? extends Number> elements, List<DateTime> timeIndices)
   :outertype: TimeSeries

TimeSeries
^^^^^^^^^^

.. java:constructor:: public TimeSeries(Number defaultValue, TimeRange range)
   :outertype: TimeSeries

Methods
-------
add
^^^

.. java:method:: public void add(DateTime time, BigDecimal value)
   :outertype: TimeSeries

data
^^^^

.. java:method:: @Override public Map<DateTime, BigDecimal> data()
   :outertype: TimeSeries

downSample
^^^^^^^^^^

.. java:method:: public TimeSeries downSample(Frequency frequency, SampleMethod sampleMethod)
   :outertype: TimeSeries

   Create a copy of the time series with a new frequency, summing all points into their proper buckets

   :param frequency: The frequency to take samples (i.e every 5 minutes, 10 seconds, etc.)
   :param sampleMethod: The mechanism to use when taking samples (sum, mean, etc.)
   :return: A new TimeSeries with the specified frequency

downSampleToTimeWindow
^^^^^^^^^^^^^^^^^^^^^^

.. java:method:: public TimeSeries downSampleToTimeWindow(DateTime start, DateTime end, Frequency frequency, SampleMethod sampleMethod)
   :outertype: TimeSeries

   Downsample the data while fitting to a specific time window. Non-existant data will be filled in as zero TODO: Make this use NaN instead of zero for empty areas

   :param start: The start of the time window desired
   :param end: The end of the time window desired, inclusive
   :param frequency: How often to take samples
   :param sampleMethod: How to interpolate the data
   :return: A new TimeSeries object with the downsampled data

expand
^^^^^^

.. java:method:: @Override public void expand(Index<DateTime> newIndex)
   :outertype: TimeSeries

index
^^^^^

.. java:method:: public Index<DateTime> index()
   :outertype: TimeSeries

lastValue
^^^^^^^^^

.. java:method:: @Override public BigDecimal lastValue()
   :outertype: TimeSeries

mean
^^^^

.. java:method:: public BigDecimal mean()
   :outertype: TimeSeries

   Compute the mean of all elements in the time-series

   :return: The mean of all elements

sum
^^^

.. java:method:: public BigDecimal sum()
   :outertype: TimeSeries

   Sum all the elements in the time-series (i.e downsample with sum across entire time slice)

   :return: The sum of all elements

toMap
^^^^^

.. java:method:: public Map<DateTime, BigDecimal> toMap()
   :outertype: TimeSeries

values
^^^^^^

.. java:method:: public List<BigDecimal> values()
   :outertype: TimeSeries

