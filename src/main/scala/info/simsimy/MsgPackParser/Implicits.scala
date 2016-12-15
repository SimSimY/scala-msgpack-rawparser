package info.simsimy.MsgPackParser

/**
  * Created by sim on 15/12/2016.
  */
object Implicits {
  implicit def MapNodeToMap(s: ValueNode[Map[Node, Node]]): Map[Node, Node] = {
    s.value
  }
}
