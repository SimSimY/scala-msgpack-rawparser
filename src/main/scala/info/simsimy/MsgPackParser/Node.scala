package info.simsimy.MsgPackParser

/**
  * Created by sim on 15/12/2016.
  */
trait Node {
  implicit def aToB[A1](a: A1)(implicit f: A1 => Node): Node = a.asInstanceOf[Node]

  def n() = true
}
