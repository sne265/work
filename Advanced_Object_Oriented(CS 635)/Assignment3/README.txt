
Null Object pattern, Visitor pattern, Startegy pattern is used.


1. Implements a binary search tree with addition.The nodes in the tree contain strings.

2. Using the Null Object pattern null nodes are added to tree to eliminate the need to check for null references or pointers in tree.

3. Implements a Visitor pattern to produce multiple representations of a search tree. For Prefix notation, each node maps to (Value (Left Subtree)(Right Subtree)).
So the representation of a tree would be like (5 (3 () (4 () ()))(10 () ())).

4. Uses the Strategy pattern so that clients can provide an ordering that the tree will use to order its elements.
Implements two orderings. The first ordering is the normal lexicographic (or alphabetic) ordering
for strings. The second ordering compares strings by first reversing the string and then comparing the
strings lexicographically. In the second ordering "az" would come after "bb".