# scala-msgpack-rawparser

A simple and light (No external dependencies) library for parsing MsgPack byte arrays into raw objects. Raw objects are representation of the MsgPack with boxed values (rather than mapping into target classes). 
 
 

***The project is not yet production ready!***
Currently the main issues are:
* Some types of msgpack data require `Long` amount of bytes to be read from the `ByteArray` or stored in `Byte[Array]` (most of the 32 data length types, such as `bin 32`) 
* The project is not formalized for maven repository (POM files and such)
* Currently the parser only works with `Array[Byte]` and should support any `Byte` Iterable

### Usage

#### NodeTypes
Every [spec][1] format is mapped to a `*Node` boxing type that extends the `Node` trait. When the format contains data (everything but `Nil`, `Unused` and in some parsers `true/false` as well ) then the matching boxing instance will have `.value` property containing the data, for Example, `StringNode(foo).value` is `foo`. 

#### Implicit conversions
While the `.value` property is always available, most of the time you won't need to use it as the practical parsers provide implicit conversions thus enabling interchangebility between the boxing and their respective underlying values.
As implicit conversions aren't everyone's cup of tee, their are provided by `Implicits` object in the parser class. Seperate import for implicts also allows more granular support for complex situations where you may want to use different parsers (and as a result different type conversions) in different functions of your code  

#### Parsers
Parser is object with a single method (`Parse(data: Array[Byte]):Node`). As this is raw implementation, you will need to cast the object to one of the implementing classes before you can start working with it.
*Currently there are 3 kinds of parsers:*
1. `RawParser` - Straight forward implementation of the spec. The Boxing type names are taken directly from the [spec][1]. There are now explicit convertions supported. This type should be used mainly for debugging and will be the basis of `Obj`=>`MsgPack` if/when that will be implemented 
2. `OptimalParser` - Parser that maps the spec types into their optimal (memory wise) matching Scala types. There are two directional implicit convertions between the boxed types and their underling types. While optimal in terms of memory working with implicits with this parser may require to to manually specify the numeric type (`Short`/`Int`/`Long`/`BigInt` and `Float`/`Double`) for the right implicit conversion to take place
3. `EasyParser` - This parser aims to reduce the code overhead presented by the `OptimalParser` by mapping all of the numeric types to `BigInt` and `Double` thus allowing easier work with numbers. 






[1]: https://github.com/msgpack/msgpack/blob/master/spec.md "MsgPack Spec"