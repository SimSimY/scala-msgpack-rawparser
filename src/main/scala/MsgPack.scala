/**
  * Created by sim on 14/12/2016.
  */
object MsgPack {


  trait Node {
  }

  trait ValueNode[T] extends Node {
    def value: T

  }

  object FixStr {
    implicit def toFixStr(n: String): FixStr = new FixStr(n)
  }

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

  def Parse(data: Array[Byte]): Node = {
    return getNode(new ByteReader(data))
  }

  private def readExtData(data: ByteReader, dataLenWordLen: Short): ExtData = {
    val dataLen = dataLenWordLen match {
      case 1 => data.getUShort()
      case 2 => data.getUInt()
      case 4 => data.getULong()
    }
    val typeNum = data.getShort()
    return ExtData(typeNum, data.getBytes(dataLen))
  }

  private def getStrng(data: ByteReader, dataLenWordLen: Short): String = {
    val dataLen = dataLenWordLen match {
      case 1 => data.getUShort()
      case 2 => data.getUInt()
      case 4 => data.getULong()
    }
    return getFixString(data,dataLen)
  }
  private def getFixString(data: ByteReader, dataLen : Long) : String={
    return data.getString(dataLen)
  }


  private def getArray(data: ByteReader, dataLenWordLen: Short): Array[Node] = {
    val dataLen = dataLenWordLen match {
      case 1 => data.getUShort()
      case 2 => data.getUInt()
      case 4 => data.getULong()
    }
    return getFixArray(data, dataLen)
  }

  private def getFixArray(data: ByteReader, dataLen: Long): Array[Node] = {

    return (for (_ <- 1 to dataLen.asInstanceOf[Int]) yield (getNode(data))).toArray
  }

  private def getMap(data: ByteReader, dataLenWordLen: Short): Map[Node, Node] = {
    val dataLen = dataLenWordLen match {
      case 1 => data.getUShort()
      case 2 => data.getUInt()
      case 4 => data.getULong()
    }

    return (for (_ <- 1 to dataLen.asInstanceOf[Int]) yield (getNode(data), getNode(data))).toMap[Node, Node]
  }

  private def getFixMap(data: ByteReader, tupleCount: Short): Map[Node, Node] = {
    return (for (_ <- 1 to tupleCount) yield (getNode(data), getNode(data))).toMap[Node, Node]
  }

  private def getNode(data: ByteReader): Node = {

    val inp=data.getByte()
    val b = (inp & 0xFF)

    return b match {
      case 0xc0 => Null()
      case 0xc1 => Unused()
      case 0xc2 => False()
      case 0xc3 => True()
      case 0xc4 => Bin8(data.getBytes(1))
      case 0xc5 => Bin16(data.getBytes(2))
      case 0xc6 => Bin32(data.getBytes(4))
      case 0xc7 => Ext8(readExtData(data, 1))
      case 0xc8 => Ext16(readExtData(data, 2))
      case 0xc9 => Ext32(readExtData(data, 4))
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
      case 0xd9 => Str8(getStrng(data, 1))
      case 0xda => Str16(getStrng(data, 2))
      case 0xdb => Str32(getStrng(data, 32))
      case 0xdc => Array16(getArray(data, 2))
      case 0xdd => Array16(getArray(data, 4))
      case 0xde => Map16(getMap(data, 2))
      case 0xdf => Map32(getMap(data, 4))
      case i if (b <= 0x7f) => FixInt((i & 0xFF).asInstanceOf[Short])
      case i if (b >= 0x80 && b <= 0x8f) => FixMap(getFixMap(data, (i.asInstanceOf[Short] - 0x80.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0x90 && b <= 0x9f) => FixArray(getFixArray(data, (i.asInstanceOf[Short] - 0x90.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0xa0 && b <= 0xbf) => FixStr(getFixString(data,(i.asInstanceOf[Short] - 0xa0.asInstanceOf[Short]).asInstanceOf[Short]))
      case i if (b >= 0xe0 && b <= 0xff) => NegInt((i.asInstanceOf[Short] - (0xFF).asInstanceOf[Short] - 1).asInstanceOf[Short])
    }
  }
}