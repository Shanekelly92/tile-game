import korlibs.time.*
import korlibs.korge.input.*
import korlibs.korge.tests.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.image.color.*
import korlibs.math.geom.*
import kotlin.test.*

class MyTest : ViewsForTesting() {

    fun createCells( letters: String) : List<Cell>{
        var output = ArrayList<Cell.TileCell>()

        for (x in letters){
            output.add(Cell.TileCell(LetterTile(Letter.valueOf(x.toString()))))
        }
        return output
    }

    @Test
    fun testIsPotentialWord_basic_first_part_of_DOG_happy(){
        val tilesOnBoard = createCells("DO")
        val tilesInRack = createCells("GVC")
        val underTest = Board()

        val output =  underTest.isPotentialWord(tilesOnBoard, tilesInRack)
        assertTrue(output)
    }

    fun testIsPotentialWord_basic_middle_of_FOOD_happy(){
        val tilesOnBoard = createCells("OO")
        val tilesInRack = createCells("DCF")
        val underTest = Board()

        val output =  underTest.isPotentialWord(tilesOnBoard, tilesInRack)
        assertTrue(output)
    }

    @Test
    fun testIsPotentialWord_gaps_with_DOG_happy(){
        val tilesOnBoard = createCells("D_G")
        val tilesInRack = createCells("GVC")
        val underTest = Board()

        val output =  underTest.isPotentialWord(tilesOnBoard, tilesInRack)
        assertTrue(output)
    }

    @Test
    fun testCreatePotentialWordRegex(){
        val tilesOnBoard = createCells("DO")
        val tilesInRack = createCells("GVC")

        val underTest = Board()

        val output =  underTest.testCreatePotentialWordRegex(tilesOnBoard, tilesInRack)
        assertEquals("[GCV]*DO[GCV]*", output)
    }

//    fun testCreatePotentialWordRegex(){
//        val tilesOnBoard = ArrayList<LetterTile>()
//        val tilesInRack = arrayListOf(LetterTile(Letter.G), LetterTile(Letter.C), LetterTile(Letter.V))
//
//        val underTest = Board()
//
//        val output =  underTest.testCreatePotentialWordRegex(tilesOnBoard, tilesInRack)
//        assertEquals("[GCV]*DO[GCV]*", output)
//    }



    @Test
    fun test() = viewsTest {
        val log = arrayListOf<String>()
        val rect = solidRect(100, 100, Colors.RED)
        rect.onClick {
            log += "clicked"
        }
        assertEquals(1, views.stage.numChildren)
        rect.simulateClick()
        assertEquals(true, rect.isVisibleToUser())
        tween(rect::x[-102], time = 10.seconds)
        assertEquals(Rectangle(x=-102, y=0, width=100, height=100), rect.globalBounds)
        assertEquals(false, rect.isVisibleToUser())
        assertEquals(listOf("clicked"), log)
    }
}
