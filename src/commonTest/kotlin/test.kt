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
        "free",
        "id",
        "idea",
        "ideas",
        "freedom",
        "doorway",
        "door",
        "do",
        "undo",
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
        val currentTiles = listOf('x','x', 'v', 'a', 'b', 'x')
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertNull(output)
    }

    @Test
    fun testGetNewViableTile_wordExtendPOSTfix_currentTilesEnough_noNewTiles(){
        val currentWord = "door"
        val currentTiles = listOf('y','a', 'w') //w a y already present to make doorway
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(0, output?.size)
    }

    @Test
    fun testGetNewViableTile_wordExtendPOSTfix_needTwoAdditionalTiles_twoNewTiles(){
        val currentWord = "door"
        val currentTiles = listOf('y','b', 'z') // requires w and a to make doorway
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(2, output?.size)
        assertTrue(output!!.contains('w'))
        assertTrue(output!!.contains('a'))
    }


    @Test
    fun testGetNewViableTile_wordExtendSUFFix_currentTilesEnough_noNewTiles(){
        val currentWord = "id" //lets try to make idea
        val currentTiles = listOf('n','a', 'u', 'e', 't') // e and a present
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(0, output?.size)
    }

    @Test
    fun testGetNewViableTile_wordExtendSUFFix_needsNewTile_noSpace_null(){
        val currentWord = "idea" //lets try to make ideas
        val currentTiles = listOf('n','a', 'u', 'e', 't', 'x', 'x') // e and a present
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertNull(output)
    }

    @Test
    fun testGetNewViableTile_wordExtendSUFFix_needTwoAdditionalTiles_twoNewTiles(){
        val currentWord = "id" // lets try to make idea
        val currentTiles = listOf('b','x', 'u', 't', 't') //
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(2, output?.size)
        assertTrue(output!!.contains('e'))
        assertTrue(output!!.contains('a'))
    }

    @Test
    fun testGetNewViableTile_wordExtendBOTHWAYS_currentTilesEnough_noNewTiles(){
        val currentWord = "do"
        val currentTiles = listOf('f','r', 'e', 'e', 'm', 'x') // tiles can make 'freedom' Not enough space to make 'do' or 'doorway'
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(0, output?.size)
    }

    @Test
    fun testGetNewViableTile_wordExtendBOTHWAYS_needThreeAdditionalTiles_threeNewTiles(){
        val currentWord = "free"
        val currentTiles = listOf('f','x', 'x', 'x') // tiles can make 'freedom' Not enough space to make 'do' or 'doorway'
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertEquals(3, output?.size)
        assertTrue(output!!.contains('d'))
        assertTrue(output!!.contains('o'))
        assertTrue(output!!.contains('m'))
    }

    @Test
    fun testGetNewViableTile_wordExtendBOTHWAYS_needThreeAdditionalTiles_NOT_ENOUGH_SPACE_Null(){
        val currentWord = "do"
        val currentTiles = listOf('f','x', 'x', 'x', 'm', 'x') // tiles can make 'freedom' Not enough space to make 'do' or 'doorway'
        val output = underTest.findViableNextLetters(currentWord, currentTiles)
        assertNull(output)

    }

    @Test
    fun testGetNewViableTile_wordNotInWordsDictionary_throwException() {
        val currentWord = "sfsdfvsdfvsfvsvf"
        val currentTiles = listOf('f','x', 'x', 'x', 'm', 'x') // tiles can make 'freedom' Not enough space to make 'do' or 'doorway'
        assertFailsWith<IllegalStateException> { // todo: make better exception
            underTest.findViableNextLetters(currentWord, currentTiles)
        }
    }
}
