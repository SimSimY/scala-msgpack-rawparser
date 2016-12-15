package info.simsimy.MsgPackParser

import java.nio.ByteBuffer

/**
  * Created by sim on 14/12/2016.
  */
class ByteReader(data: Array[Byte]) {
  var Position : Int = 0

  def getShort(): Short = {
    return (0.asInstanceOf[Short] | getByte()).asInstanceOf[Short]
  }

  def getByte(): Byte = {
    val res = data(Position)
    Position += 1
    return res
  }

  def getUShort(): Short = {
    //    return (0.asInstanceOf[Int] | getByte()).asInstanceOf[Short]
    (getByte() & 0xFF).asInstanceOf[Short]
  }

  def getInt(): Int = {
    return getBytes(2).foldLeft(0)((num, nextByte) => (num << 8) | nextByte).asInstanceOf[Int]
  }

  def getUInt(): Int = {
    return getBytes(2).foldLeft(0)((num, nextByte) => (num << 8) | (nextByte & 0xFF)).asInstanceOf[Int]
  }

  def getBytes(count: Long): Array[Byte] = {
    val startPosition = Position
    Position += count.asInstanceOf[Int]
    return data.slice(startPosition, Position)
  }

  def getLong(): Long = {
    return getBytes(4).foldLeft(0L)((num, nextByte) => (num << 8) | nextByte)
  }

  def getULong(): Long = {
    return getBytes(4).foldLeft(0L)((num, nextByte) => (num << 8) | (nextByte & 0xFF))
  }

  def getBigInt(): BigInt = {
    return BigInt(getBytes(8))
  }

  def getUBigInt(): BigInt = {
    val srcBytes = getBytes(8)
    return BigInt(Array(0x00.asInstanceOf[Byte]) ++ srcBytes)
  }

  def getString(wordLen: Long): String = {
    return new String(getBytes(wordLen))
  }

  def getFloat(): Float = {
    return ByteBuffer.wrap(getBytes(4)).getFloat
  }

  def getDouble(): Double = {
    return ByteBuffer.wrap(getBytes(8)).getDouble
  }
}
