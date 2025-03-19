import korlibs.datastructure.*
import korlibs.math.geom.*


class Board (val array: Array2<Cell> = Array2<Cell>(8, 8){Cell.EmptyCell}) {
    fun get(x: Int, y: Int): Cell {
        return array[x, y]
    }

    fun set(xy: PointInt, cell: Cell) : Boolean{
        if (xy.x < array.width && xy.y < array.height) {
            array[xy.x, xy.y] = cell
            return true
        }
        return false
    }

    fun updatePosition (oldPos : PointInt, newPos : PointInt, cell : Cell.TileCell){
        if (set(newPos, cell)) set(oldPos, Cell.EmptyCell)

    }


    companion object {
        fun cellPosToXY(x: Int, y: Int): Point {
            return Point(x * 128, y * 128)
        }

        fun xYToCellPos(xy : Point): PointInt {
            // adding 64 so that x+y represent center of tile
            return PointInt( ( (xy.x+64)/128).toInt(), ((xy.y+64)/128).toInt())
        }
    }
}


sealed interface Cell {
    object EmptyCell : Cell {
    }

    data class TileCell(val tile: LetterTile) : Cell
}



