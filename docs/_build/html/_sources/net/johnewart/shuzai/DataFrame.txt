.. java:import:: java.io PrintStream

DataFrame
=========

.. java:package:: net.johnewart.shuzai
   :noindex:

.. java:type:: public class DataFrame<I, V>

Constructors
------------
DataFrame
^^^^^^^^^

.. java:constructor:: public DataFrame()
   :outertype: DataFrame

DataFrame
^^^^^^^^^

.. java:constructor:: public DataFrame(List<I> indexValues)
   :outertype: DataFrame

DataFrame
^^^^^^^^^

.. java:constructor:: public DataFrame(List<I> index, Map<String, Series<I, V>> values)
   :outertype: DataFrame

Methods
-------
add
^^^

.. java:method:: public void add(String key, Series<I, V> series)
   :outertype: DataFrame

add
^^^

.. java:method:: public void add(Map<String, Series<I, V>> values)
   :outertype: DataFrame

backFill
^^^^^^^^

.. java:method:: public void backFill()
   :outertype: DataFrame

forwardFill
^^^^^^^^^^^

.. java:method:: public void forwardFill()
   :outertype: DataFrame

getIndex
^^^^^^^^

.. java:method:: public Index<I> getIndex()
   :outertype: DataFrame

getValuesMap
^^^^^^^^^^^^

.. java:method:: public Map<String, Map<I, V>> getValuesMap()
   :outertype: DataFrame

print
^^^^^

.. java:method:: public void print()
   :outertype: DataFrame

