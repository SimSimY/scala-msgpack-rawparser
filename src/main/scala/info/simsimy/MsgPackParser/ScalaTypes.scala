package info.simsimy.MsgPackParser

import info.simsimy.MsgPackParser.RawTypes.ExtData

/**
  * Created by sim on 15/12/2016.
  */
object ScalaTypes {
  def TrueNode = BooleanNode(true)

  def FalseNode = BooleanNode(false)

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
