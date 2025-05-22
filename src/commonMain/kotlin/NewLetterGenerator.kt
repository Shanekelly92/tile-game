class NewLetterGenerator(val dictionary: Set<String>,  maxTilesInRack: Int = 7) {

    fun findViableNextLetters(currentWord: String, currentTiles: Set<Char>): Set<Char>? {

        if(!dictionary.contains(currentWord)) {
            throw IllegalStateException("The current word should always be in the dictionary as it's a previsously validated word!");
        }

        var subStrings : List<String>? = null
        for (word in dictionary){
            if (word.contains(currentWord)) {
                subStrings = word.split(currentWord)
                break
            }
        }

        subStrings ?: return null

        val requiredLetters = run {
            val letters = HashSet<Char>()
            for (substring in subStrings) {
                letters.addAll(substring.toSet())
            }
            letters // for some reason no need for return statement, a bit like ruby in this context
        }
        //TODO ooooooohhhhh I just realised the fact I am using Set means duplicate letters are not considered!

        requiredLetters.removeAll(requiredLetters.intersect(currentTiles)) // I think this should return all the letters that aren't in the currentTiles

        return requiredLetters
    }

}
