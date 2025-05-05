import korlibs.datastructure.*
import korlibs.math.geom.*


class Board (val array: Array2<Cell> = Array2<Cell>(8, 8){Cell.EmptyCell}) {
    fun get(x: Int, y: Int): Cell {
        return array[x, y]
    }

    fun set(xy: PointInt, cell: Cell) : Boolean{
        if (xy.x < array.width && xy.y < array.height) {
             array [xy.x, xy.y] = cell
            return true
        }
        return false
    }

    fun updatePosition (oldPos : PointInt, newPos : PointInt, cell : Cell.TileCell) : Boolean{
        if (!cell.tile.moveable)  return false
        if (newPos.x > array.width-1 ||newPos.y > array.height-1) return false
        if (array[newPos.x, newPos.y] is Cell.EmptyCell){
            set(newPos, cell)
            set(oldPos, Cell.EmptyCell) // we already know it's in bounds
            cell.tile.moveable = false;
            return true
        }
        return false;
    }

    fun getCompleteWordIfExists(tile: LetterTile?): ArrayList<LetterTile>? {
        tile ?: return null
        val clusters = getClusters(tile) // clusters around that tile, up and down
        var allClusters = HashSet<ArrayList<LetterTile>>() // all clusters adjacent to tiles in initial clusters
        for (cluster in clusters) {
            for (tile in cluster){
                allClusters.addAll(getClusters(tile));
            }
        }

        for (cluster in allClusters){
            if(!isWord(cluster)) return null
        }

        return clusters.first() // maybe return all in cluster and 'animate' those
    }

    fun getClusters( tile : LetterTile) : Set<ArrayList<LetterTile>>{
        val posx = tile.boardPos.x
        val posy = tile.boardPos.y // todo: pos should really be pointInt, need to get straight
        val lettersLeft = crawlContiguous(posx, posy, -1, 0, arrayListOf(tile));
        val lettersRight = crawlContiguous(posx, posy, +1, 0, ArrayList());
        val lettersUp = crawlContiguous(posx, posy, 0, -1, arrayListOf(tile))
        val lettersDown = crawlContiguous(posx, posy, 0, +1, ArrayList())

        lettersLeft.addAll(lettersRight)
        lettersUp.addAll(lettersDown)
        return setOf(lettersLeft,lettersUp)

    }

    fun crawlContiguous(x: Int, y:Int, xChange:Int, yChange: Int, list: ArrayList<LetterTile>) : ArrayList<LetterTile>{ // this doesn't really need integer change values as its always 1, maybe booleans or enum better?
        val nextX = x + xChange
        val nextY = y + yChange
        if (nextX > 7 || nextY > 7) return list //todo setup proper variable board bound values, not hardcoded
        val cell = get(x+xChange, y+yChange)
        // need to add board boundary logic
        when (cell) {
            Cell.EmptyCell -> return list
            is Cell.TileCell -> {
                if (xChange < 0 || yChange < 0 ) list.add(0, cell.tile) else list.add(cell.tile)//todo this is pretty bad
                return crawlContiguous(x+xChange, y+yChange, xChange, yChange, list)
            }
        }
    }

    fun isPotentialWord(tilesOnBoard: List<Cell>, tilesInRack: List<Cell>): Boolean {
        return false;
    }

    fun testCreatePotentialWordRegex(tilesOnBoard: List<Cell>, tilesInRack: List<Cell>): String {
        return ""
    }

    fun isNewWordAndLegal(lastPositionPlaced : PointInt?) : Boolean {
        lastPositionPlaced ?: return false; //todo this shouldn't really be neccessary why is lastPositionPlaced nullable
        return false
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
            words.add("FOOD")
        }

        fun isWord(list: List<LetterTile>) : Boolean{
            val str = StringBuilder()
            for (tile in list){
                str.append(tile.letter.letter)
            }
            println("the cluster is: ${str.toString()}")
            if( words.contains(str.toString().uppercase())){
                for (tile in list) {
//                    val animator = tile.simpleAnimator
//                    animator.sequence {
//                        alpha(tile, 0.7)
//                        alpha(tile, 1)
//                    }
//                    tile.rect.color=Colors.BEIGE
                }
                return true
            }
            return false;
        }

    }
}


sealed interface Cell {
    object EmptyCell : Cell {
    }

    data class TileCell(val tile: LetterTile) : Cell
}




