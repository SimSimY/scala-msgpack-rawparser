package info.simsimy.MsgPackParser

import info.simsimy.MsgPackParser.RawTypes.ExtData
import info.simsimy.MsgPackParser.ScalaTypes._

/**
  * Created by sim on 15/12/2016.
  */
object Implicits {
  implicit def MapNodeToMap(s: ValueNode[Map[Node, Node]]): Map[Node, Node] = {
    s.value
  }


  implicit def bArrToByteArrayNode(s: Array[Byte]) = ByteArrayNode(s)

  implicit def ByteArrayNodeToBArr(t: ByteArrayNode) = t.value


  implicit def NodeArrayToNode(s: Array[Node]) = NodeArrayNode(s)

  implicit def NodeToNodeArrayNode(t: NodeArrayNode) = t.value

  implicit def ExtDataToNode(s: ExtData) = ExtNode(s)

  implicit def NodeToExtNode(t: ExtNode) = t.value

  implicit def MapNodeToNode(s: Map[Node, Node]) = MapNode(s)

  implicit def NodeToMapNode(t: MapNode) = t.value


  implicit def ArrayNodeToNode(s: Array[Node]) = ArrayNode(s)

  implicit def NodeToArrayNode(t: ArrayNode) = t.value

  implicit def StringNodeToNode(s: String) = StringNode(s)

  implicit def NodeToStringNode(t: StringNode) = t.value


  implicit def BooleanNodeToNode(s: Boolean) = BooleanNode(s)

  implicit def NodeToBooleanNode(t: BooleanNode) = t.value

  implicit def NodeToNullNode(t: NullNode) = null


  implicit def ShortIntNodeToNode(s: Short) = ShortIntNode(s)

  implicit def NodeToShortIntNode(t: ShortIntNode) = t.value


  implicit def IntNodeToNode(s: Int) = IntNode(s)

  implicit def NodeToIntNode(t: IntNode) = t.value

  implicit def LongIntNodeToNode(s: Long) = LongIntNode(s)

  implicit def NodeToLongIntNode(t: LongIntNode) = t.value

  implicit def BigIntIntNodeToNode(s: BigInt) = BigIntNode(s)

  implicit def NodeToBigIntIntNode(t: BigIntNode) = t.value


  implicit def FloatNodeToNode(s: Float) = FloatNode(s)

  implicit def NodeToFloatNode(t: FloatNode) = t.value

  implicit def DoubleNodeToNode(s: Double) = DoubleNode(s)

  implicit def NodeToDoubleNode(t: DoubleNode) = t.value


  
}


