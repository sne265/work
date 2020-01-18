
Flyweight pattern

In this assignment we will investigate the flyweight pattern. We will look at a flyweight for characters.

The point of the Flyweight pattern is to save space. The question is how much space will we
save using the pattern. We have two ways to store sample text of 356 characters. 
The first way is to store 356 character objects with all their state (intrinsic and extrinsic). So we could store the 356 characters in an array. 
The other way is to use the flyweights. But the flyweight pattern uses additional
data structures which require space. So the goal is to compute the
space required to store the document the normal way (array of 356 of non-flyweight objects)
verses using the flyweights.