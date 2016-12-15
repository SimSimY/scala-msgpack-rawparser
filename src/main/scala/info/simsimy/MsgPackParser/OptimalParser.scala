package info.simsimy.MsgPackParser

import info.simsimy.MsgPackParser.NodeTypes._

import scala.runtime.RichLong

/**
  * Created by sim on 15/12/2016.
  */
object OptimalParser {

  private val Parser = new ParserTools(getNode)

  def Parse(data: Array[Byte]): Node = {
    return getNode(new ByteReader(data))
  }

  private def getNode(data: ByteReader): Node = {

    val inp = data.getByte()
    val b = (inp & 0xFF)

    val b_hex = new RichLong(b).toHexString
    val b_bin = new RichLong(b).toBinaryString

    return b match {
      case 0xc0 => NullNode()
      case 0xc1 => UnusedNode()
      case 0xc2 => FalseNode
      case 0xc3 => TrueNode
      case 0xc4 => ByteArrayNode(Parser.getBinArray(data, 1))
      case 0xc5 => ByteArrayNode(Parser.getBinArray(data, 2))
      case 0xc6 => ByteArrayNode(Parser.getBinArray(data, 4))
      case 0xc7 => ExtNode(Parser.readExtData(data, 1))
      case 0xc8 => ExtNode(Parser.readExtData(data, 2))
      case 0xc9 => ExtNode(Parser.readExtData(data, 4))
      case 0xca => FloatNode(data.getFloat())
      case 0xcb => DoubleNode(data.getDouble())
      case 0xcc => ShortIntNode(data.getUShort())
      case 0xcd => IntNode(data.getUInt())
      case 0xce => LongIntNode(data.getULong())
      case 0xcf => BigIntNode(data.getUBigInt())
      case 0xd0 => ShortIntNode(data.getShort())
      case 0xd1 => IntNode(data.getInt())
      case 0xd2 => LongIntNode(data.getLong())
      case 0xd3 => BigIntNode(data.getBigInt())
      case 0xd4 => ExtNode(ExtData(data.getShort(), data.getBytes(1)))
      case 0xd5 => ExtNode(ExtData(data.getShort(), data.getBytes(2)))
      case 0xd6 => ExtNode(ExtData(data.getShort(), data.getBytes(4)))
      case 0xd7 => ExtNode(ExtData(data.getShort(), data.getBytes(8)))
      case 0xd8 => ExtNode(ExtData(data.getShort(), data.getBytes(16)))
      case 0xd9 => StringNode(Parser.getStrng(data, 1))
      case 0xda => StringNode(Parser.getStrng(data, 2))
      case 0xdb => StringNode(Parser.getStrng(data, 32))
      case 0xdc => NodeArrayNode(Parser.getArray(data, 2))
      case 0xdd => NodeArrayNode(Parser.getArray(data, 4))
      case 0xde => MapNode(Parser.getMap(data, 2))
      case 0xdf => MapNode(Parser.getMap(data, 4))
      case i if (b <= 0x7f) => ShortIntNode((i & 0xFF).asInstanceOf[Short])
      case i if (b >= 0x80 && b <= 0x8f) => MapNode(Parser.getFixMap(data, (i.asInstanceOf[Short] - 0x80.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0x90 && b <= 0x9f) => NodeArrayNode(Parser.getFixArray(data, (i.asInstanceOf[Short] - 0x90.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0xa0 && b <= 0xbf) => StringNode(Parser.getFixString(data, (i.asInstanceOf[Short] - 0xa0.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0xe0 && b <= 0xff) => ShortIntNode((i.asInstanceOf[Short] - (0xFF).asInstanceOf[Short] - 1).asInstanceOf[Short])
    }
  }

  object Implicits {
    implicit def MapNodeToMap(s: ValueNode[Map[Node, Node]]): Map[Node, Node] = s.value

    implicit def MapToMapNode(s: Map[Node, Node]) = MapNode(s)


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

}
