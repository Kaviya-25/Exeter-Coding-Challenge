package translateWordEnglishToFrench.java;

import java.io.*;
import java.util.*;

public class EnglishToFrenchWordTranslator {
     static String FindWord_File_Path = "find_words.txt";
     static String ShakespeareText_FILE_Path = "t8.shakespeare.txt";
     static String Dictionary_File_Path ="french_dictionary.csv";


    public static void main(String[] args) {
        // Load the FindWord ,ShakespeareText and dictionary word mappings
        Map<String, String> wordMap = loadWordMap();

        // Read the find_words.txt from the folder
        List<String> findWordsList = readWordsFromFile(FindWord_File_Path);
        
        // Read the t8.shakespeare.txt from the folder
        String shakespeareText = readFileContent(ShakespeareText_FILE_Path);
        
        // Read the french_dictionary.csv from the folder
        Map<String,String> dictionaryfile = readDictionaryFromFile(Dictionary_File_Path);
        
        // Translate words in shakespeareText to French
         String translatedText= translateText(shakespeareText,findWordsList, dictionaryfile);
        
        //Write translated text to t8.shakespeare.translated.txt
         writeToFile("t8.shakespeare.translated.txt", translatedText);


        // Translate the English words to French and calculate their frequency
        Map<String, Integer> frequencyMap = new HashMap<>();
        long startTime = System.currentTimeMillis();
        
        for (String word : findWordsList) {
            String translatedWord = wordMap.getOrDefault(word, "");
            int frequency = frequencyMap.getOrDefault(translatedWord, 0);
            frequencyMap.put(translatedWord, frequency + 1);
        }
        long endTime = System.currentTimeMillis();
        // Print the frequency of translated French words
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
  
        // Print the processing time
        System.out.println("Processing time: " + (endTime - startTime) + " milliseconds");
        }
        private static void writeToFile(String filepath, String content) {
    	
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
	
		
	}

	private static String translateText(String text, List<String> findWordsList,
			Map<String,String> dictionaryfile) {
		    String[] words = text.split("\\s+");
            StringBuilder translatedText = new StringBuilder();
       for (String word : words) {
            String translatedWord = dictionaryfile.getOrDefault(word.toLowerCase(), word);
            translatedText.append(translatedWord).append(" ");
        }
       return translatedText.toString();

	}


	private static Map<String, String> loadWordMap() {
        Map<String, String> wordMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ShakespeareText_FILE_Path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String englishWord = parts[0].trim();
                    String frenchWord = parts[1].trim();
                    wordMap.put(englishWord, frenchWord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordMap;
       }

       private static String readFileContent(String folderPath) {
         StringBuilder content = new StringBuilder();
         File folder = new File(folderPath);
         File[] files = folder.listFiles();
         if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileWords = readFileContent(file.getPath());
                    content.append(fileWords);
                }
            }
        }
        return content.toString();
       }

       private static List<String> readWordsFromFile(String filePath) {
          List<String> words = new ArrayList<>();
          try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
             String line;
             while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                words.addAll(Arrays.asList(parts));
              }
           } catch (IOException e) {
            e.printStackTrace();
         }
         return words;
       }
       private static Map<String,String> readDictionaryFromFile(String filePath) {
    	 Map<String,String> dictionary = new HashMap<>();

         try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    dictionary.put(parts[0], parts[1]);
                }
            }
          } catch (IOException e) {
            e.printStackTrace();
         }
        return dictionary;
       }
}