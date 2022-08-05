![Github Actions Build](https://img.shields.io/github/workflow/status/AlexBroadbent/json-dsl/Build)
![Jackson DSL Maven Central](https://img.shields.io/maven-central/v/com.abroadbent/jackson-dsl?label=Jackson-DSL%20in%20Maven%20Central)
![Gson DSL Maven Central](https://img.shields.io/maven-central/v/com.abroadbent/gson-dsl?label=Gson-DSL%20in%20Maven%20Central)
[![codecov](https://codecov.io/gh/AlexBroadbent/json-dsl/branch/main/graph/badge.svg?token=gi94zPKqgd)](https://codecov.io/gh/AlexBroadbent/json-dsl)

---

# JSON DSL

A type-safe builder wrapped around the [Jackson JSON library](https://github.com/FasterXML/jackson)
and [Gson JSON library](https://github.com/google/gson).

The aim of this project is to reduce the amount of boilerplate code required to instantiate JSON objects in code.

## Type Mappings

While this is just an implementation detail, the following kotlin data types are mapped into the Jackson/Gson
equivalents:

| Kotlin Type Parameter | Jackson Type Equivalent | Gson Type Equivalent |
|-----------------------|-------------------------|----------------------|
| String                | TextNode                | JsonPrimitive        |
| Int                   | TextNode                | JsonPrimitive        |
| Long                  | LongNode                | JsonPrimitive        |
| Double                | DoubleNode              | JsonPrimitive        |
| Boolean               | BooleanNode             | JsonPrimitive        |
| Object                | ObjectNode              | JsonObject           |
| Array                 | ArrayNode               | JsonArray            |

The functions are used in objects via the `put` method and in arrays via the `add` method.

---

## Adding the Dependency

Gradle:
```gradle
implementation("com.abroadbent:jackson-dsl:0.2.0")
implementation("com.abroadbent:gson-dsl:0.2.0")
```
 
Maven:
```pom
<dependency>
  <groupId>com.abroadbent</groupId>
  <artifactId>jackson-dsl</artifactId>
  <version>0.2.0</version>
</dependency>

<dependency>
  <groupId>com.abroadbent</groupId>
  <artifactId>gson-dsl</artifactId>
  <version>0.2.0</version>
</dependency>
```

---

## Examples

### Primitives

The following methods will create a root level JSON primitive, with the equivalent from the above table, which can be
used to add to an array or object.

| Method  | Example                   |
|---------|---------------------------|
| string  | string("abc")             |
| int     | int(1701)                 |
| long    | long(3478921748921748921) |
| double  | double(1.3242)            |
| boolean | boolean(false)            |

An example of adding in one of these primitives can be found in the unit test [PrimitiveBuilderSpec], but they are split
between declaration and operation.

```kotlin
val bar = string("bar")
val pi = double(3.14)

val json = `object` {
    put("foo", primitive)
    put("pi", pi)
}
```

which produces the object:

```json
{ 
  "foo": "bar",
  "pi": 3.14
}
```

### Objects

The `object` (or `obj` as an alias) function provides a wrapper for the Jackson Type `ObjectNode` or Gson
Type `JsonObject`, which takes a `key` and a `value` where the `value` is any primitive, object or array.

_Note that the `object` function is wrapped with backticks (\`) as "object" is a keyword in Kotlin._

The function:

```kotlin
val json = `object` {
    put("one", "two")
    put("three", 4)
}
```

produces the object:

```json
{
  "one": "two",
  "three": 4
}
```

which is equivalent to:

```kotlin
val jackson = mapper.createObjectNode()
    .put("one", "two")
    .put("three", 4)

val gson = JsonObject()
    .apply { putProperty("one", "two") }
    .apply { putProperty("three", 4) }
```

---

### Arrays

The `array` function (or `arr` as an alias) provides a wrapper for the Jackson Type `ArrayNode` or Gson Type `JsonArray`
, which takes a list of any primitives, objects and arrays.

The function:

```kotlin
val json = array {
    add(67214621784621)
    add(true)
}
```

produces the array:

```json
[
  67214621784621,
  true
]
```

which is equivalent to:

```kotlin
val jackson = mapper.createArrayNode()
    .add(67214621784621)
    .add(true)

val gson = JsonArray()
    .apply { add(67214621784621) }
    .apply { add(true) }
```

---

### Nesting

Objects and Arrays can be nested to any supported depth by the Jackson/Gson JSON library.

For example:

```kotlin
val json = array {
    add("foo")
    array {
        add("bar")
        `object` {
            put("two", "three")
            `object`("four") {
                put("five", 6)
            }
        }
    }
}
```

produces the JSON array:

```json5
[
  "foo",
  [
    "bar",
    {
      "two": "three",
      "four": {
        "five": 6
      }
    }
  ]
]
```

which is equivalent to:

```kotlin
val nested2 = mapper.createObjectNode()
    .put("two", "three")
    .set<ObjectNode>(
        "four", mapper.createObjectNode()
            .put("five", 6)
    )
val nested1 = mapper.createArrayNode()
    .add("bar")
    .add(nested2)
val jackson = mapper.createArrayNode()
    .add("foo")
    .add(nested1)

// OR

val gson = JsonArray()
    .apply { add("foo") }
    .apply {
        add(
            JsonArray()
                .apply { add("bar") }
                .apply {
                    add(
                        JsonObject().apply { addProperty("two", "three") }
                    )
                }
        )
    }
```
