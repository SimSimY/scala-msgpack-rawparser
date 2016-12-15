package info.simsimy.MsgPackParser

import info.simsimy.MsgPackParser.NodeTypes._

/**
  * Created by sim on 15/12/2016.
  */
protected class ParserTools(parser: ByteReader => Node) {
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