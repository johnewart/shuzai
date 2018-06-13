Index
=====

.. java:package:: net.johnewart.shuzai
   :noindex:

.. java:type:: public class Index<T> implements Iterable<T>

Constructors
------------
Index
^^^^^

.. java:constructor:: public Index()
   :outertype: Index

Index
^^^^^

.. java:constructor:: public Index(List<T> values)
   :outertype: Index

Methods
-------
add
^^^

.. java:method:: public void add(T value)
   :outertype: Index

addAll
^^^^^^

.. java:method:: public void addAll(Collection<T> values)
   :outertype: Index

asList
^^^^^^

.. java:method:: public List<T> asList()
   :outertype: Index

first
^^^^^

.. java:method:: public T first()
   :outertype: Index

iterator
^^^^^^^^

.. java:method:: @Override public Iterator<T> iterator()
   :outertype: Index

last
^^^^

.. java:method:: public T last()
   :outertype: Index

merge
^^^^^

.. java:method:: public boolean merge(Index<T> otherIndex)
   :outertype: Index

   Merge two indices.

   :param otherIndex:
   :return: true if index modified, false otherwise

of
^^

.. java:method:: public static <T> Index<T> of(T... things)
   :outertype: Index

subSet
^^^^^^

.. java:method:: public SortedSet<T> subSet(T from, T to, boolean includeEnd)
   :outertype: Index

   Return a subset [from -> to

   :param from:
   :param to:
   :return: A sorted subset

