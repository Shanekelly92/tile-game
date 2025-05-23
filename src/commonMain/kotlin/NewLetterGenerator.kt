class NewLetterGenerator(val dictionary: Set<String>,  val maxTilesInRack: Int = 7) {

    fun findViableNextLetters(currentWord: String, currentTiles: List<Char>): List<Char>? {

        if(!dictionary.contains(currentWord)) {
            throw IllegalStateException("The current word should always be in the dictionary as it's a previously validated word!");
        }
        var subStrings : List<String>? = null
        for (word in dictionary){
            if (word.equals(currentWord)) continue
            if (word.contains(currentWord)) {
                subStrings = word.split(currentWord)

                val requiredLetters = run {
                    val letters = ArrayList<Char>()
                    for (substring in subStrings) {
                        letters.addAll(substring.toSet())
                    }
                    letters // for some reason no need for return statement, a bit like ruby in this context
                }
                requiredLetters.removeAll(requiredLetters.intersect(currentTiles))
                if (requiredLetters.size + currentTiles.size > maxTilesInRack) continue
                println("new possible word $word")
                println("required letters ${requiredLetters.toString()}")
                return requiredLetters
            }
        }
        return null // null means no possible new word can be made from currentWord //todo: not great to use null as return value
    }

}
