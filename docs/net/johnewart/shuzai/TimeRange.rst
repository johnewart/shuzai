.. java:import:: org.joda.time DateTime

.. java:import:: org.joda.time Period

.. java:import:: java.util ArrayList

.. java:import:: java.util List

TimeRange
=========

.. java:package:: net.johnewart.shuzai
   :noindex:

.. java:type:: public class TimeRange

Fields
------
startTime
^^^^^^^^^

.. java:field:: public final DateTime startTime
   :outertype: TimeRange

Constructors
------------
TimeRange
^^^^^^^^^

.. java:constructor:: public TimeRange(DateTime startTime, DateTime endTime, Frequency frequency)
   :outertype: TimeRange

TimeRange
^^^^^^^^^

.. java:constructor:: public TimeRange(List<DateTime> timeIndices)
   :outertype: TimeRange

Methods
-------
computeIntervals
^^^^^^^^^^^^^^^^

.. java:method:: public static List<DateTime> computeIntervals(DateTime startTime, DateTime endTime, Frequency frequency)
   :outertype: TimeRange

get
^^^

.. java:method:: public DateTime get(int i)
   :outertype: TimeRange

size
^^^^

.. java:method:: public int size()
   :outertype: TimeRange

