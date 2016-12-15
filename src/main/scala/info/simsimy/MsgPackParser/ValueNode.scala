package info.simsimy.MsgPackParser

/**
  * Created by sim on 15/12/2016.
  */
trait ValueNode[T] extends Node {
  def value: T
}