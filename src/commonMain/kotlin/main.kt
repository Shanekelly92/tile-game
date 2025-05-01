import Board.Companion.cellPosToXY
import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.animate.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
    val sceneContainer = sceneContainer()
    sceneContainer.changeTo({ MyScene() })
}

class MyScene(): PixelatedScene(128 *8, 128 * 9, sceneSmoothing = true){
    val board = Board()


    override suspend fun SContainer.sceneMain() {

        val piecesContainer = container{}.zIndex(10000f)
        val boardContainer = container{}.zIndex(-1f)
        var lastTilePlaced : LetterTile? = null;
        val tilesInTurn = ArrayList<LetterTile>()
        val posInTurn = ArrayList<PointInt>()

        for (y in 0 until 8) {
            for (x in 0 until 8 ){
                boardContainer.roundRect(Size(128, 128), RectCorners(5.0), fill = Colors.TAN).xy(128 * x, 128 * y).zIndex(-3f)
            }
        }

        val tileSet = LinkedHashSet<LetterTile>(7);
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




        fun createTile(letter: Letter) : LetterTile {
            val tile = LetterTile(letter)
            tile.mouse{
                tile.draggable {
                    val newPos = Board.xYToCellPos(it.viewNextXY)
                    val oldPos = Board.xYToCellPos(it.viewStartXY)
                    if (it.end){
                        println("setting piece from ${oldPos} to ${newPos}")
                        if (!board.updatePosition(oldPos, newPos, Cell.TileCell(tile)) )
                            tile.xy(it.viewStartXY
                            )
                        else {
                            tileSet.remove(tile)
//                            tileSet.add(createTile(Letter.nextLetter()))
                            lastTilePlaced = tile;
                            tile.boardPos = newPos
                            tilesInTurn.add(tile)
                            posInTurn.add(newPos)
//                            representTileRack()
                        }

                        val completeWord = board.getCompleteWordIfExists(tile)

                        piecesContainer.removeChildren()
                        piecesContainer.representBoard()
                    }
                }
            }
            return tile;
        }

        fun representTileRack() {
            while (tileSet.size < 5){
                tileSet.add(createTile(Letter.nextLetter()))
            }
            for ((index, tile) in tileSet.withIndex()) {
                tile.positionX(128 * index)
                this.addChild(tile)
                tile.zIndex(1000f)
                tile.alignTopToTopOf(tileRack)
            }
        }

        tileSet.run{
            add (createTile(Letter.D))
            add (createTile(Letter.G))
            add (createTile(Letter.F))
            add (createTile(Letter.O))
            add (createTile(Letter.D))
        }
        representTileRack()


        var oTile = createTile(Letter.O)
        board.set(PointInt(4,4), Cell.TileCell(oTile))

        piecesContainer.representBoard()

        val turnButton = uiButton("Go!").size(Size(128, 128))
            .alignLeftToRightOf(tileRack)
            .alignTopToBottomOf(boardContainer)
        turnButton.textSize = 70f

        turnButton.onPress{
            var completeWord = board.getCompleteWordIfExists(lastTilePlaced);
            if (completeWord == null) {
                for (tile in tilesInTurn){
                    tileSet.add(tile)
                    tile.moveable = true
                    // todo: really the returning of the tile to the rack should be handled by the tile in a method, with all the logic around that, rather than setting things in different places
                }
                for (pos in posInTurn){
                    board.set(pos, Cell.EmptyCell)
                }
            }
            completeWord?.forEach {
                val animator = it.simpleAnimator
                animator.sequence {
                    alpha(it, 0.7)
                    alpha(it, 1)
                }
                it.rect.color=Colors.BEIGE
            }
            tilesInTurn.clear()
            posInTurn.clear()
            representBoard()
            representTileRack()
        }
    }
}


