# scala-msgpack-rawparser

A simple and light (No external dependencies) library for parsing MsgPack byte arrays into raw objects. Raw objects are representation of the MsgPack with boxed values (rather than mapping into target classes). 
 
 

***The project is not yet production ready!***
Currently the main issues are:
* Some types of msgpack data require `Long` amount of bytes to be read from the `ByteArray` or stored in `Byte[Array]` (most of the 32 data length types, such as `bin 32`) 
* The project is not formalized for maven repository (POM files and such)
* Currently the parser only works with `Array[Byte]` and should support any `Byte` Iterable


### Compilation and Linking
As the project is not yet publishing artifacts, you can just clone it and run `sbt package`
 
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

#### Examples
TODO: Add better examples

The following example provide a simple usage example as well as glance at the difference between the parsers. The code is avaiable as [part of this project][2]
```scala
object Demo {
  val msg = Array(135, 163, 105, 110, 116, 1, 165, 102, 108, 111, 97, 116, 203, 63, 224, 0, 0, 0, 0, 0, 0, 167, 98, 111, 111, 108, 101, 97, 110, 195, 164, 110, 117, 108, 108, 192, 166, 115, 116, 114, 105, 110, 103, 167, 102, 111, 111, 32, 98, 97, 114, 165, 97, 114, 114, 97, 121, 146, 163, 102, 111, 111, 163, 98, 97, 114, 166, 111, 98, 106, 101, 99, 116, 130, 163, 102, 111, 111, 1, 163, 98, 97, 122, 203, 63, 224, 0, 0, 0, 0, 0, 0).map(_.asInstanceOf[Byte])

  def main(args: Array[String]): Unit = {
    rawParse()
    # FixMap(Map(FixStr(array) -> FixArray([Linfo.simsimy.MsgPackParser.Node;@4c70fda8), FixStr(object) -> FixMap(Map(FixStr(foo) -> FixInt(1), FixStr(baz) -> Float64(0.5))), FixStr(int) -> FixInt(1), FixStr(float) -> Float64(0.5), FixStr(string) -> FixStr(foo bar), FixStr(null) -> NullNode(), FixStr(boolean) -> True()))
    optimalParse()
    # MapNode(Map(StringNode(array) -> NodeArrayNode([Linfo.simsimy.MsgPackParser.Node;@4501b7af), StringNode(object) -> MapNode(Map(StringNode(foo) -> ShortIntNode(1), StringNode(baz) -> DoubleNode(0.5))), StringNode(int) -> ShortIntNode(1), StringNode(float) -> DoubleNode(0.5), StringNode(string) -> StringNode(foo bar), StringNode(null) -> NullNode(), StringNode(boolean) -> BooleanNode(true)))
    easyParse()
    # MapNode(Map(StringNode(array) -> NodeArrayNode([Linfo.simsimy.MsgPackParser.Node;@4501b7af), StringNode(object) -> MapNode(Map(StringNode(foo) -> ShortIntNode(1), StringNode(baz) -> DoubleNode(0.5))), StringNode(int) -> ShortIntNode(1), StringNode(float) -> DoubleNode(0.5), StringNode(string) -> StringNode(foo bar), StringNode(null) -> NullNode(), StringNode(boolean) -> BooleanNode(true)))

  }

  def rawParse() = {
    import info.simsimy.MsgPackParser.RawParser
    println(RawParser.Parse(msg))
  }

  def easyParse() = {
    import info.simsimy.MsgPackParser.EasyParser
    println(EasyParser.Parse(msg))
  }

  def optimalParse() = {
    import info.simsimy.MsgPackParser.OptimalParser
    println(OptimalParser.Parse(msg))
  }
}
```

As in the example above there is no explicit difference between the ouput of `EasyParser` and `OptimalParser`, Here is the code that we user [@Gryphonet][3] for reading the MsgPack messages generated by [secor's][4] backups 

```scala
  def GetOffsetAndKey(data: Array[Byte]): (Long, String) = {
    import info.simsimy.MsgPackParser.OptimalParser
    import info.simsimy.MsgPackParser.OptimalParser.Implicits._
    val res = OptimalParser.Parse(data)
    val res_map = res.asInstanceOf[MapNode]
    val msg_key_string = res_map(2.asInstanceOf[Short]).asInstanceOf[ByteArrayNode]
    val msg_offset = res_map(1.asInstanceOf[Short]).asInstanceOf[LongIntNode]
    (msg_offset, new String(msg_key_string))
  }


  def GetOffsetAndKeyEasy(data: Array[Byte]): (Long, String) = {
    import info.simsimy.MsgPackParser.EasyParser
    import info.simsimy.MsgPackParser.EasyParser.Implicits._

    val res = EasyParser.Parse(data)
    val res_map = res.asInstanceOf[MapNode]
    val msg_key_string = res_map(2).asInstanceOf[ByteArrayNode]
    val msg_offset = res_map(1).asInstanceOf[LongIntNode]
    (msg_offset, new String(msg_key_string))
  }

```
In this example can notice that in `GetOffsetAndKey` there are `asInstanceOf[Short]` casts (as the underlying Nodes are `Short` and without the cast the compiler will convert them into `IntNode` and not `ShortIntNode`. 
In `GetOffsetAndKeyEasy` the previously `ShortIntNodes` are now `BigIntNodes` and any **N** number is converted into `BinIntNode`
[1]: https://github.com/msgpack/msgpack/blob/master/spec.md "MsgPack Spec"
[2]: https://github.com/SimSimY/scala-msgpack-rawparser/blob/master/src/main/scala/examples/Demo.scala "Parser examples"
[3]: http://www.gryphonet.com "Gryhonet"
[4]: https://github.com/pinterest/secor "Secor"