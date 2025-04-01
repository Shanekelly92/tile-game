import Board.Companion.cellPosToXY
import Board.Companion.isWord
import korlibs.datastructure.*
import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.geom.slice.*
import korlibs.memory.*

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {

    val sceneContainer = sceneContainer()
    sceneContainer.changeTo({ MyScene() })

}

class MyScene(): PixelatedScene(128 *8, 128 * 9, sceneSmoothing = true){

    override suspend fun SContainer.sceneMain() {

        val board = Board();
        val placeHolderContainer = container{}
        val piecesContainer = container{}.zIndex(10000f)
        val boardContainer = container{}.zIndex(-1f)

        for (y in 0 until 8) {
            for (x in 0 until 8 ){
                boardContainer.roundRect(Size(128, 128), RectCorners(5.0), fill = Colors.TAN).xy(128 * x, 128 * y).zIndex(-3f)
            }
        }
        val tileRack = roundRect(Size(128 * 7, 128), RectCorners(5.0), fill = Colors["#913812"]) {
            alignTopToBottomOf(boardContainer)
            alignLeftToLeftOf(boardContainer)
                .zIndex(-1f)
        }

        fun Container.representBoard() {
            for (y in 0 until 8) {
                for (x in 0 until 8){
                    val cell = board.get(x, y)
                    when (cell){
                        Cell.EmptyCell -> Unit
                        is Cell.TileCell -> {
                            piecesContainer.addChild(cell.tile.xy( cellPosToXY(x,y) ))
                        }
                    }
                }
            }
        }

        println("going to setup tiles now")

        fun createTile(letter: Letter) : LetterTile {
            val tile = LetterTile(letter)
            tile.mouse{
                tile.draggable {
                    val newPos = Board.xYToCellPos(it.viewNextXY)
                    val oldPos = Board.xYToCellPos(it.viewStartXY)
                    if (it.start){
                        println("starting drag" + tile.uniqueId)
                    }
                    if (it.end){
                        println("setting piece from ${oldPos} to ${newPos}")
                        if (!board.updatePosition(oldPos, newPos, Cell.TileCell(tile)) ) tile.xy(it.viewStartXY)


                        fun crawlContiguous(x: Int, y:Int, xChange:Int, yChange: Int, str: String) : String{
                            val cell = board.get(x+xChange, y+yChange)
                            when (cell) {
                                Cell.EmptyCell -> return str
                                is Cell.TileCell -> {
                                    var newStr = str;
                                    val letter = cell.tile.letter
                                    if (xChange < 0) newStr = "" + letter + str else newStr = str + letter //todo bad
                                    return crawlContiguous(x+xChange, y+yChange, xChange, yChange, newStr)
                                }
                            }

                        }


                        val lettersLeft = crawlContiguous(newPos.x, newPos.y, -1, 0,"" + tile.letter.letter);
                        val lettersRight = crawlContiguous(newPos.x, newPos.y, +1, 0,"");
                        if (isWord(lettersLeft + lettersRight)) println("it's a word!")

                        println("lettersLeft $lettersLeft")
                        println("lettersRight $lettersRight")

                        piecesContainer.removeChildren()
                        piecesContainer.representBoard()
                    }
                }
            }
            return tile;
        }



        val tileSet = ArrayList<LetterTile>(7);
        tileSet.run{
//            for (i in 1 until 6){
//                add (createTile(Letter.nextLetter()))
//            }
            add (createTile(Letter.D))
            add (createTile(Letter.G))
            add (createTile(Letter.F))
            add (createTile(Letter.O))
            add (createTile(Letter.D))
        }
        for ((index, tile) in tileSet.withIndex()){
            tile.positionX(128 * index)
            this.addChild(tile)
            tile.zIndex(1000f)
            tile.alignTopToTopOf(tileRack)
        }


        fun createInitialBoardTiles(){
            var oTile = createTile(Letter.O)
            board.set(PointInt(4,4), Cell.TileCell(oTile))
        }

        createInitialBoardTiles()
        piecesContainer.representBoard()

    }




}



//fun crawlContiguous(x : Int, y: Int, str: String ) : String{
//    var nextStr = str
//    if (str.isEmpty()) {
//        val cell = board.get(x, y)
//        when (cell){
//            is Cell.TileCell -> return crawlContiguous(x -1, y, str)
//            is Cell.EmptyCell -> {
//                var prevCell =  board.get(x +1, y ) as Cell.TileCell
//                nextStr = nextStr + prevCell.tile.letter.letter // i really need to sort out my type heirarchy
//                return crawlContiguous(x+1, y, nextStr)
//            }
//        }
//
//    } else {
//        val cell = board.get(x, y)
//        var nextStr = str
//        when (cell){
//            is Cell.EmptyCell -> return str
//            is Cell.TileCell -> {
//                println("we're here with $nextStr");
//                nextStr = nextStr + cell.tile.letter.letter
//                return crawlContiguous(x+1, y, nextStr)
//            }
//        }
//    }
//
//}

