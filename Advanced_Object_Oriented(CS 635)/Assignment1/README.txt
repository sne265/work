1.Implements a circular doubly linked queue. Queue has to support adding new elements,
return the first element and remove the first element. Adding an
element and removing the element with the highest element should be at most O(1) where
N is the size of the queue. The queue should start with a capacity greater that 1. When the
queue is full and another element is added the queue capacity should be doubled. If additions
and deletions are intermixed it is possible that a large number additions can be done
without increasing the capacity.
2. Queue needs to be able to hold process objects. A process has a name (String), an
owner (String), PID (int), number of threads, percent of CPU currently being used and total
amount of CPU time used.
3. Programs that use this queue will want to display and print out the elements in the queue.
They will need to display/print out the queue ordered by name, PID, CPU time used, percent
of CPU time, total CPU time or by owner.