package excp;

import java.util.*;

/**
 * Assuming word would be passed.
 * For searching a word TRIE algorithm need to implement
 * <p>
 * For searching synonym hashing I will be using but with Long as key
 */
public class TestDictionary {

    private Map<Long, WordMeaning> wordMappings;

    public static void main(String[] args) {
        TestDictionary dictionary = new TestDictionary();
        dictionary.init();
    }

    public TestDictionary() {
        wordMappings = new HashMap<>();
    }

    private void init() {

        Properties words = readWords ();
        createWordMapping (words);
        System.out.println("wordMappings = " + wordMappings);
        displayMeaning ("gift");
        displayMeaning ("present");
        displayMeaning ("parent");
        displayMeaning ("mom");

        /*generateHash("DDCCBBAA");
        generateHash("AABBCCDD");
        generateHash("AACCBBDD");
        generateHash("ADADCCBB");
        generateHash("AADDCCBB");
        generateHash("AADCDCBB");
        generateHash("ADBADCCB");
        generateHash("ADBACDCB");
        generateHash("ADBACDBC");*/
    }

    private void displayMeaning(String word) {
        Long hash = generateHash(word);
        System.out.println(hash+"........"+word);
        WordMeaning meaning = wordMappings.get(hash);
        if (meaning==null) {
            System.out.println("Meaning not found.");
        } else {
            List<Long> hashes = meaning.getSynonymsHashes();
            hashes.forEach(v -> System.out.println(wordMappings.get(v).getWord()));
        }
    }

    private void createWordMapping(Properties words) {
        words.forEach(this::getMapping);
    }

    private void getMapping(Object key, Object value) {

        String k = (String) key;
        String v = (String) value;

        String[] arr = v.split(",");
        long keyHash = generateHash(k);
        for (String s : arr) {
            long valHash = generateHash(s);
            if (wordMappings.containsKey(keyHash)) {
                WordMeaning wordMeaning = wordMappings.get(keyHash);
                List<Long> hashes = wordMeaning.getSynonymsHashes();
                if (!hashes.contains(valHash)) {
                    hashes.add(valHash);
                }
            } else {
                List<Long> hashes = new ArrayList<>();
                hashes.add(valHash);
                WordMeaning wordMeaning = new WordMeaning(k, hashes);
                wordMappings.put(keyHash, wordMeaning);
            }
            if (wordMappings.containsKey(valHash)) {
                WordMeaning wordMeaning = wordMappings.get(valHash);
                List<Long> hashes = wordMeaning.getSynonymsHashes();
                if (!hashes.contains(keyHash)) {
                    hashes.add(keyHash);
                }
            } else {
                List<Long> hashes = new ArrayList<>();
                hashes.add(keyHash);
                WordMeaning wordMeaning = new WordMeaning(s, hashes);
                wordMappings.put(valHash, wordMeaning);
            }
        }

    }

    private Properties readWords() {
        Properties properties = new Properties();

        properties.put("gift", "present,donation,natural-gift");
        properties.put("present", "donation,natural-gift");
        properties.put("mother", "parent");
        properties.put("birth", "genesis");

        return properties;
    }

    private static long generateHash(String str) {
        long hash = str.hashCode();
        hash *= (hash > 0) ? hash : hash*-1;

        return hash;
    }

    static class WordMeaning {
        String word;
        List<Long> synonymsHashes;

        public WordMeaning (String word, List<Long> synonymsHashes) {
            this.synonymsHashes = synonymsHashes;
            this.word = word;
        }

        public String getWord() {
            return word;
        }

        public List<Long> getSynonymsHashes() {
            return synonymsHashes;
        }

        public String toString () {
            return word + ", synonyms count = " + synonymsHashes.size();
        }
    }

}
