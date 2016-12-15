package info.simsimy.MsgPackParser

/**
  * Created by sim on 15/12/2016.
  */
object NodeTypes {


  def TrueNode = BooleanNode(true)

  def FalseNode = BooleanNode(false)

  case class ExtData(Type: Int, Data: Array[Byte])

  case class FixInt(value: Short) extends ValueNode[Short]

  case class FixMap(value: Map[Node, Node]) extends ValueNode[Map[Node, Node]]

  case class FixArray(value: Array[Node]) extends ValueNode[Array[Node]]

  case class FixStr(value: String) extends ValueNode[String]

  case class Null() extends Node

  case class Unused() extends Node

  case class False() extends Node

  case class True() extends Node

  case class Bin8(value: Array[Byte]) extends ValueNode[Array[Byte]]

  case class Bin16(value: Array[Byte]) extends ValueNode[Array[Byte]]

  case class Bin32(value: Array[Byte]) extends ValueNode[Array[Byte]]

  case class Ext8(value: ExtData) extends ValueNode[ExtData]

  case class Ext16(value: ExtData) extends ValueNode[ExtData]

  case class Ext32(value: ExtData) extends ValueNode[ExtData]

  case class Float32(value: Float) extends ValueNode[Float]

  case class Float64(value: Double) extends ValueNode[Double]

  case class Uint8(value: Short) extends ValueNode[Short]

  case class Uint16(value: Int) extends ValueNode[Int]

  case class Uint32(value: Long) extends ValueNode[Long]

  case class Uint64(value: BigInt) extends ValueNode[BigInt]

  case class Int8(value: Short) extends ValueNode[Short]

  case class Int16(value: Int) extends ValueNode[Int]

  case class Int32(value: Long) extends ValueNode[Long]

  case class Int64(value: BigInt) extends ValueNode[BigInt]

  case class FixExt1(value: ExtData) extends ValueNode[ExtData]

  case class FixExt2(value: ExtData) extends ValueNode[ExtData]

  case class FixExt4(value: ExtData) extends ValueNode[ExtData]

  case class FixExt8(value: ExtData) extends ValueNode[ExtData]

  case class FixExt16(value: ExtData) extends ValueNode[ExtData]

  case class Str8(value: String) extends ValueNode[String]

  case class Str16(value: String) extends ValueNode[String]

  case class Str32(value: String) extends ValueNode[String]

  case class Map16(value: Map[Node, Node]) extends ValueNode[Map[Node, Node]]

  case class Map32(value: Map[Node, Node]) extends ValueNode[Map[Node, Node]]

  case class Array16(value: Array[Node]) extends ValueNode[Array[Node]]

  case class Array32(value: Array[Node]) extends ValueNode[Array[Node]]

  case class NegInt(value: Short) extends ValueNode[Short]

  case class ByteArrayNode(value: Array[Byte]) extends ValueNode[Array[Byte]]

  case class NodeArrayNode(value: Array[Node]) extends ValueNode[Array[Node]]

  case class ExtNode(value: ExtData) extends ValueNode[ExtData]

  case class MapNode(value: Map[Node, Node]) extends ValueNode[Map[Node, Node]]

  case class ArrayNode(value: Array[Node]) extends ValueNode[Array[Node]]

  case class StringNode(value: String) extends ValueNode[String]

  case class BooleanNode(value: Boolean) extends ValueNode[Boolean]

  case class NullNode() extends Node

  case class UnusedNode() extends Node

  case class ShortIntNode(value: Short) extends ValueNode[Short]

  case class IntNode(value: Int) extends ValueNode[Int]

  case class LongIntNode(value: Long) extends ValueNode[Long]

  case class BigIntNode(value: BigInt) extends ValueNode[BigInt]

  case class FloatNode(value: Float) extends ValueNode[Float]

  case class DoubleNode(value: Double) extends ValueNode[Double]


}
