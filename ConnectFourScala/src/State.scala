import java.io.FileNotFoundException
import java.io.PrintWriter
import java.io.UnsupportedEncodingException
import State._
import scala.beans.BeanProperty

object State {

  val length0 = Array[State]()
}

class State(@BeanProperty var player: Player, @BeanProperty var board: Board, @BeanProperty var lastMove: Move)
  extends Comparable[Any] {

  @BeanProperty
  var children: Array[State] = length0

  @BeanProperty
  var value: Int = 0

  def initializeChildren() {
     // Recursively call initializeChildren for depth
     children = board.getPossibleMoves(player).map(m => {
       new State(player.opponent, new Board(board, m), m)
     })
  }

  def writeToFile() {
    try {
      var writer = new PrintWriter("output.txt", "UTF-8")
      writer.println(this)
      writer.close() // Added to fix bug
      java.awt.Toolkit.getDefaultToolkit.beep()
    } catch {
      case e @ (_: FileNotFoundException | _: UnsupportedEncodingException) => e.printStackTrace()
    }
  }

  override def toString(): String = {
    println("State.toString printing")
    toStringHelper(0, "")
  }

  private def toStringHelper(d: Int, ind: String): String = {
    var str = ind + player + " to play\n"
    str = str + ind + "Value: " + value + "\n"
    str = str + board.toString(ind) + "\n"
    if (children != null && children.length > 0) {
      str = str + ind + "Children at depth " + (d + 1) + ":\n" + ind +
        "----------------\n"
      for (s <- children) {
        str = str + s.toStringHelper(d + 1, ind + "   ")
      }
    }
    str
  }

  override def compareTo(ob: AnyRef): Int = 0
}

