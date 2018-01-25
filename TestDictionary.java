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

    private void createWordMapping(Properties words) {
        words.forEach(this::getMapping);
    }

    private void getMapping(Object key, Object value) {

        String k = (String) key;
        String v = (String) value;

        String[] arr = v.split(",");
        long keyHash = generateHash(k);
        boolean keyPresent = wordMappings.containsKey(keyHash);
        for (String s : arr) {
            long valHash = generateHash(s);
            if (keyPresent) {
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

        System.out.println("hash = " + hash);
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
    }

}