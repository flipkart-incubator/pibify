Support for

1.
    - [x] Native Java Types
2.
    - [x] Arrays of Native Java Types
3.
    - [x] Basic Pojo
4.
    - [x] Relational Pojo
5.
    - [x] Tertiary Relational pojo with missing annotations on tertiary refs
6.
    - [x] Array of Objects
7.
    - [x] 1 level SubClass Hierarchy
8.
    - [x] N level subclass hierarchy
9.
    - [x] Collections of java natives
10.
    - [x] Collections of Java Objects
11.
    - [x] Collections of Collections
12.
    - [ ] Iterable
13.
    - [x] Enums as fields
14.
    - [x] Enums in Collections and arrays
15.
    - [x] Enums in maps (key/value)
16.
    - [x] Null reference members
17.
    - [x] Null containers (collection,map, array) members
18.
    - [x] Empty containers
19.
    - [x] Duplicate index in pibify annotation
20.
    - [x] Inner classes
21.
    - [x] Non container classes with generic type parameter

Jackson

1.
    - [x] @JsonIgnore
2.
    - [ ] @JsonProperty on arbitrary methods
3.
    - [ ] @JsonProperty with renaming fields (should be noOp)
4.
    - [X] @JsonProperty in constructor
5.
    - [x] Explicit @JsonSubtype
6.
    - [X] @JsonCreator

Additional Test cases

1.
    - [ ] Changes in order of enum will cause mis-serde due to implicit change in ordinal
2.
     - [x] Parsing unknown fields
3.
     - [x] Parsing unknown enums
4.
    - [ ] Permutation of @JsonProperty, @JsonIgnore and Lombok

