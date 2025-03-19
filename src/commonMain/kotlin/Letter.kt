import korlibs.image.color.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import kotlin.random.*

class LetterTile (letter : Letter) : Container() {
    val uniqueId : String
    init {
        uniqueId = "" + Random.nextInt()
        roundRect(Size(128, 128), RectCorners(5.0), fill = Colors.WHITESMOKE)
        text(
            letter
                .letter.toString(), 64f, Colors.BLACK
        ) {
            centerBetween(0.0f, 0.0f, 128f, 128f)
        }
    }
}

enum class Letter (val letter : Char) {
    A('A'),
    B('B'),
    C('C'),
    D('D'),
    E('E'),
    F('F'),
    G('G'),
    H('H'),
    I('I'),
    J('J'),
    K('K'),
    L('L'),
    M('M'),
    N('N'),
    O('O'),
    P('P'),
    Q('Q'),
    R('R'),
    S('S'),
    T('T'),
    U('U'),
    V('V'),
    W('W'),
    X('X'),
    Y('Y'),
    Z('Z');


    companion object {
        fun nextLetter() : Letter{
            return Letter.values().random()
        }
    }

}


