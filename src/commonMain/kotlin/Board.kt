import korlibs.datastructure.*
import korlibs.math.geom.*


class Board (val array: Array2<Cell> = Array2<Cell>(8, 8){Cell.EmptyCell}) {
    fun get(x: Int, y: Int): Cell {
        return array[x, y]
    }

    fun set(xy: PointInt, cell: Cell) : Boolean{
        if (xy.x < array.width && xy.y < array.height) {
//            if (array[xy.x, xy.y] is Cell.EmptyCell) array [xy.x, xy.y] = cell
             array [xy.x, xy.y] = cell
            return true
        }
        return false
    }

    fun updatePosition (oldPos : PointInt, newPos : PointInt, cell : Cell.TileCell) : Boolean{
        if (array[newPos.x, newPos.y] is Cell.EmptyCell){
            if (set(newPos, cell)) return set(oldPos, Cell.EmptyCell)
        }
        return false;
    }


    companion object {
        fun cellPosToXY(x: Int, y: Int): Point {
            return Point(x * 128, y * 128)
        }

        fun xYToCellPos(xy : Point): PointInt {
            // adding 64 so that x+y represent center of tile
            return PointInt( ( (xy.x+64)/128).toInt(), ((xy.y+64)/128).toInt())
        }
        var words = HashSet<String>()
        init {
            words.add("DOG")
        }

        fun isWord(str: String) : Boolean{
            return words.contains(str.uppercase())
        }
    }
}


sealed interface Cell {
    object EmptyCell : Cell {
    }

    data class TileCell(val tile: LetterTile) : Cell
}



