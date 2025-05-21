import korlibs.time.*
import korlibs.korge.input.*
import korlibs.korge.tests.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.image.color.*
import korlibs.math.geom.*
import kotlin.test.*

class MyTest : ViewsForTesting() {


    val testWords = setOf(

        "doorway",
        "do",
        "undo",
        "freedom",
        "cat",
        "upside",
        "blah",
        "up"
        )

    val underTest = NewLetterGenerator(testWords, 7)

    //assumes max seven tiles in tray

    @Test
    /* cases where there are no possible additional tiles that can help make a word (ie, no-win) are distinguished
    from cases where there is no need for additional tiles (current tiles are enough) by returning null rather than
    empty set.
    TODO: returning null as a result seems...iffy plus it means we have to use nullable type. Maybe there is a better way?
     */
    fun testGetNewViableTile_noPossibleNewTile_null(){
        val currentWord = "door"
        val currentTiles = setOf('x','y', 'v', 'a', 'b', 'x')
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertNull(output)
    }

    @Test
    fun testGetNewViableTile_wordExtendPOSTfix_currentTilesEnough_noNewTiles(){
        val currentWord = "door"
        val currentTiles = setOf('y','a', 'w') //w a y already present to make doorway
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(0, output?.size)
    }

    @Test
    fun testGetNewViableTile_wordExtendPOSTfix_needTwoAdditionalTiles_twoNewTiles(){
        val currentWord = "door"
        val currentTiles = setOf('y','b', 'z') // requires w and a to make doorway
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(2, output?.size)
        assertTrue(output!!.contains('w'))
        assertTrue(output!!.contains('a'))
    }


    @Test
    fun testGetNewViableTile_wordExtendSUFFix_currentTilesEnough_noNewTiles(){
        val currentWord = "do"
        val currentTiles = setOf('n','f', 'u', 't', 't') // u n already present to make undo. Not enough space to make doorway
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(0, output?.size)
    }

    @Test
    fun testGetNewViableTile_wordExtendSUFFix_needTwoAdditionalTiles_twoNewTiles(){
        val currentWord = "do"
        val currentTiles = setOf('b','f', 'u', 't', 't') // u n already present to make undo. Not enough space to make doorway
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(2, output?.size)
        assertTrue(output!!.contains('n'))
        assertTrue(output!!.contains('u'))
    }

    @Test
    fun testGetNewViableTile_wordExtendBOTHWAYS_currentTilesEnough_noNewTiles(){
        val currentWord = "do"
        val currentTiles = setOf('f','r', 'e', 'e', 'm', 'x') // tiles can make 'freedom' Not enough space to make 'do' or 'doorway'
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(0, output?.size)
    }

    @Test
    fun testGetNewViableTile_wordExtendBOTHWAYS_needThreeAdditionalTiles_threeNewTiles(){
        val currentWord = "do"
        val currentTiles = setOf('f','x', 'm', 'x') // tiles can make 'freedom' Not enough space to make 'do' or 'doorway'
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(3, output?.size)
        assertTrue(output!!.contains('r'))
        assertTrue(output!!.contains('e'))
        assertTrue(output!!.contains('e'))
    }

    @Test
    fun testGetNewViableTile_wordExtendBOTHWAYS_needThreeAdditionalTiles_NOT_ENOUGH_SPACE_Null(){
        val currentWord = "do"
        val currentTiles = setOf('f','x', 'x', 'x', 'm', 'x') // tiles can make 'freedom' Not enough space to make 'do' or 'doorway'
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertNull(output)

    }
}
