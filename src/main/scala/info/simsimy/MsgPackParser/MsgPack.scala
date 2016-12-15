package info.simsimy.MsgPackParser

import info.simsimy.MsgPackParser.RawTypes._
import info.simsimy.MsgPackParser.ScalaTypes._

import scala.runtime.RichLong

/**
  * Created by sim on 14/12/2016.
  */
object MsgPack {


  private val RawParser = new ParserTools(getNodeRaw)
  private val ScalaParser = new ParserTools(getNode)

  def ParseRaw(data: Array[Byte]): Node = {
    return getNodeRaw(new ByteReader(data))
  }

  private def getNodeRaw(data: ByteReader): Node = {

    val inp = data.getByte()
    val b = (inp & 0xFF)

    return b match {
      case 0xc0 => NullNode()
      case 0xc1 => Unused()
      case 0xc2 => False()
      case 0xc3 => True()
      case 0xc4 => Bin8(RawParser.getBinArray(data, 1))
      case 0xc5 => Bin16(RawParser.getBinArray(data, 2))
      case 0xc6 => Bin32(RawParser.getBinArray(data, 4))
      case 0xc7 => Ext8(RawParser.readExtData(data, 1))
      case 0xc8 => Ext16(RawParser.readExtData(data, 2))
      case 0xc9 => Ext32(RawParser.readExtData(data, 4))
      case 0xca => Float32(data.getFloat())
      case 0xcb => Float64(data.getDouble())
      case 0xcc => Uint8(data.getUShort())
      case 0xcd => Uint16(data.getUInt())
      case 0xce => Uint32(data.getULong())
      case 0xcf => Uint64(data.getUBigInt())
      case 0xd0 => Int8(data.getShort())
      case 0xd1 => Int16(data.getInt())
      case 0xd2 => Int32(data.getLong())
      case 0xd3 => Int64(data.getBigInt())
      case 0xd4 => FixExt1(ExtData(data.getShort(), data.getBytes(1)))
      case 0xd5 => FixExt2(ExtData(data.getShort(), data.getBytes(2)))
      case 0xd6 => FixExt4(ExtData(data.getShort(), data.getBytes(4)))
      case 0xd7 => FixExt8(ExtData(data.getShort(), data.getBytes(8)))
      case 0xd8 => FixExt8(ExtData(data.getShort(), data.getBytes(16)))
      case 0xd9 => Str8(RawParser.getStrng(data, 1))
      case 0xda => Str16(RawParser.getStrng(data, 2))
      case 0xdb => Str32(RawParser.getStrng(data, 32))
      case 0xdc => Array16(RawParser.getArray(data, 2))
      case 0xdd => Array16(RawParser.getArray(data, 4))
      case 0xde => Map16(RawParser.getMap(data, 2))
      case 0xdf => Map32(RawParser.getMap(data, 4))
      case i if (b <= 0x7f) => FixInt((i & 0xFF).asInstanceOf[Short])
      case i if (b >= 0x80 && b <= 0x8f) => FixMap(RawParser.getFixMap(data, (i.asInstanceOf[Short] - 0x80.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0x90 && b <= 0x9f) => FixArray(RawParser.getFixArray(data, (i.asInstanceOf[Short] - 0x90.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0xa0 && b <= 0xbf) => FixStr(RawParser.getFixString(data, (i.asInstanceOf[Short] - 0xa0.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0xe0 && b <= 0xff) => NegInt((i.asInstanceOf[Short] - (0xFF).asInstanceOf[Short] - 1).asInstanceOf[Short])
    }
  }

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
      case 0xc2 => ScalaTypes.FalseNode
      case 0xc3 => ScalaTypes.TrueNode
      case 0xc4 => ByteArrayNode(ScalaParser.getBinArray(data, 1))
      case 0xc5 => ByteArrayNode(ScalaParser.getBinArray(data, 2))
      case 0xc6 => ByteArrayNode(ScalaParser.getBinArray(data, 4))
      case 0xc7 => ExtNode(ScalaParser.readExtData(data, 1))
      case 0xc8 => ExtNode(ScalaParser.readExtData(data, 2))
      case 0xc9 => ExtNode(ScalaParser.readExtData(data, 4))
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
      case 0xd9 => StringNode(ScalaParser.getStrng(data, 1))
      case 0xda => StringNode(ScalaParser.getStrng(data, 2))
      case 0xdb => StringNode(ScalaParser.getStrng(data, 32))
      case 0xdc => NodeArrayNode(ScalaParser.getArray(data, 2))
      case 0xdd => NodeArrayNode(ScalaParser.getArray(data, 4))
      case 0xde => MapNode(ScalaParser.getMap(data, 2))
      case 0xdf => MapNode(ScalaParser.getMap(data, 4))
      case i if (b <= 0x7f) => ShortIntNode((i & 0xFF).asInstanceOf[Short])
      case i if (b >= 0x80 && b <= 0x8f) => MapNode(ScalaParser.getFixMap(data, (i.asInstanceOf[Short] - 0x80.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0x90 && b <= 0x9f) => NodeArrayNode(ScalaParser.getFixArray(data, (i.asInstanceOf[Short] - 0x90.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0xa0 && b <= 0xbf) => StringNode(ScalaParser.getFixString(data, (i.asInstanceOf[Short] - 0xa0.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0xe0 && b <= 0xff) => ShortIntNode((i.asInstanceOf[Short] - (0xFF).asInstanceOf[Short] - 1).asInstanceOf[Short])
    }
  }

  private class ParserTools(parser: ByteReader => Node) {
    def readExtData(data: ByteReader, dataLenWordLen: Short): ExtData = {
      val dataLen = dataLenWordLen match {
        case 1 => data.getUShort()
        case 2 => data.getUInt()
        case 4 => data.getULong()
      }
      val typeNum = data.getShort()
      return ExtData(typeNum, data.getBytes(dataLen))
    }

    def getStrng(data: ByteReader, dataLenWordLen: Short): String = {
      val dataLen = dataLenWordLen match {
        case 1 => data.getUShort()
        case 2 => data.getUInt()
        case 4 => data.getULong()
      }
      return getFixString(data, dataLen)
    }

    def getFixString(data: ByteReader, dataLen: Long): String = {
      return data.getString(dataLen)
    }


    def getBinArray(data: ByteReader, dataLenWordLen: Short): Array[Byte] = {
      val dataLen = dataLenWordLen match {
        case 1 => data.getUShort()
        case 2 => data.getUInt()
        case 4 => data.getULong()
      }
      return getFixBinArray(data, dataLen)
    }

    def getFixBinArray(data: ByteReader, dataLen: Long): Array[Byte] = {

      return data.getBytes(dataLen)
    }

    def getArray(data: ByteReader, dataLenWordLen: Short): Array[Node] = {
      val dataLen = dataLenWordLen match {
        case 1 => data.getUShort()
        case 2 => data.getUInt()
        case 4 => data.getULong()
      }
      return getFixArray(data, dataLen)
    }

    def getFixArray(data: ByteReader, dataLen: Long): Array[Node] = {

      return (for (_ <- 1 to dataLen.asInstanceOf[Int]) yield (parser(data))).toArray
    }

    def getMap(data: ByteReader, dataLenWordLen: Short): Map[Node, Node] = {
      val dataLen = dataLenWordLen match {
        case 1 => data.getUShort()
        case 2 => data.getUInt()
        case 4 => data.getULong()
      }

      return (for (_ <- 1 to dataLen.asInstanceOf[Int]) yield (parser(data), parser(data))).toMap[Node, Node]
    }

    def getFixMap(data: ByteReader, tupleCount: Short): Map[Node, Node] = {
      return (for (_ <- 1 to tupleCount) yield (parser(data), parser(data))).toMap[Node, Node]
    }

  }
}
