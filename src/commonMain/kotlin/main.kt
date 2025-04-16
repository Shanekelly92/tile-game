import Board.Companion.cellPosToXY
import Board.Companion.isWord
import korlibs.datastructure.*
import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.animate.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
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
    val board = Board();



    override suspend fun SContainer.sceneMain() {

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

        val turnButton = uiButton("Go!").size(Size(128, 128))
            .alignLeftToRightOf(tileRack)
            .alignTopToBottomOf(boardContainer)
        turnButton.textSize = 70f


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
        val tileSet = LinkedHashSet<LetterTile>(7);

        fun representTileRack() {
            for ((index, tile) in tileSet.withIndex()) {
                tile.positionX(128 * index)
                this.addChild(tile)
                tile.zIndex(1000f)
                tile.alignTopToTopOf(tileRack)
            }
        }

        fun createTile(letter: Letter) : LetterTile {
            val tile = LetterTile(letter)
            tile.mouse{
                tile.draggable {
                    val newPos = Board.xYToCellPos(it.viewNextXY)
                    val oldPos = Board.xYToCellPos(it.viewStartXY)
//                    if (it.start){
//                        println("starting drag" + tile.uniqueId)
//                    }
                    if (it.end){
                        println("setting piece from ${oldPos} to ${newPos}")
                        if (!board.updatePosition(oldPos, newPos, Cell.TileCell(tile)) )
                            tile.xy(it.viewStartXY
                            )
                        else {
                            tileSet.remove(tile)
                            tileSet.add(createTile(Letter.nextLetter()))
                            representTileRack()
                        }

                        val lettersLeft = board.crawlContiguous(newPos.x, newPos.y, -1, 0, arrayListOf(tile));
                        val lettersRight = board.crawlContiguous(newPos.x, newPos.y, +1, 0, ArrayList());
                        val lettersUp = board.crawlContiguous(newPos.x, newPos.y, 0, -1, arrayListOf(tile))
                        val lettersDown = board.crawlContiguous(newPos.x, newPos.y, 0, +1, ArrayList())


                        lettersLeft.addAll(lettersRight)
                        lettersUp.addAll(lettersDown)

                        if (isWord(lettersLeft)) {
                            println("it's a word!")
                        }
                        if (isWord(lettersUp)) println("it's a word!")

                        piecesContainer.removeChildren()
                        piecesContainer.representBoard()
                    }
                }
            }
            return tile;
        }



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


        representTileRack()


        fun createInitialBoardTiles(){
            var oTile = createTile(Letter.O)
            oTile.moveable = false;
            board.set(PointInt(4,4), Cell.TileCell(oTile))
        }

        createInitialBoardTiles()
        piecesContainer.representBoard()

    }
}

fun Board.crawlContiguous(x: Int, y:Int, xChange:Int, yChange: Int, list: ArrayList<LetterTile>) : ArrayList<LetterTile>{ // this doesn't really need integer change values as its always 1, maybe booleans or enum better?
    val nextX = x+ xChange
    val nextY = y+yChange
    if (nextX > 7 || nextY > 7) return list //todo setup proper variable board bound values, not hardcoded
    val cell = this.get(x+xChange, y+yChange)
    // need to add board boundary logic
    when (cell) {
        Cell.EmptyCell -> return list
        is Cell.TileCell -> {
            if (xChange < 0 || yChange < 0 ) list.add(0, cell.tile) else list.add(cell.tile)//todo this is pretty bad
            return crawlContiguous(x+xChange, y+yChange, xChange, yChange, list)
        }
    }
}
