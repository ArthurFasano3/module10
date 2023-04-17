package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.platform.commons.annotation.Testable;


class Word implements Comparable<Word>{
    
    public String word;
    public int frequency;
    
    public Word(String word, int frequency){
        this.word = word;
        this.frequency = frequency;
    }
    
    @Override
    public int compareTo(Word w){
        if(w.frequency > this.frequency) return 1;
        
        else if (w.frequency < this.frequency) return -1;
        
        return 0;
    }
}

/**
 * 
 * @author Arthur Fasano
 * @version 4/10/2023
 *
 */
public class wordOccur {
	/**
	 * 
	 * @param args for main method
	 * @throws Exception 
	 * @throws SQLException 
	 */
    public static void main(String args[]) throws Exception {
    	
    	// Jsoup to get HTML DOC
    	
		Document document = Jsoup.connect("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm").get();
		Element poem = document.select(".chapter").first();
		String[] words = poem.text().split(" ");
		
    	// testing
    	// String[] words = new String[] {"foo", "baz", "bar", "foo", "foo", "baz", "baz", "baz", "bar", "bar", "bar"};
        getOccurrences(words, 20).forEach(w -> System.out.printf("%s %s\n", w.word, w.frequency));
    }
    
	 public static Connection getConnection() throws Exception{
		  try{
		   String driver = "com.mysql.jdbc.Driver";
		   String url = "jdbc:mysql://127.0.0.1:3306/wordOccurrences";
		   String username = "root";
		   String password = "password";
		   Class.forName(driver);
		   
		   Connection conn = DriverManager.getConnection(url,username,password);
		   System.out.println("Connected");
		   return conn;
		  } catch(Exception e){System.out.println(e);}
		  
		  
		  return null;
		 }
	 
    /**
     * 
     * @param words for the poem or the words from a string array
     * @param limit for the number of words to count
     * @return a sorted list to print out
     * @throws Exception 
     * @throws SQLException 
     */
    public static List<Word> getOccurrences(String[] words, int limit) throws Exception {
    	Connection con = getConnection();
 
		StringBuilder sql = new StringBuilder("INSERT INTO ").append("word").append(" (");
		StringBuilder placeholders = new StringBuilder();
    	
        Map<String, Integer> map = new HashMap<String, Integer>();
        
        for(String word : words){
        	String fixedWords = word.toLowerCase().replaceAll("[^A-Za-z]+", "");
            if(map.containsKey(fixedWords)){
                map.put(fixedWords, map.get(fixedWords) + 1);
            }
            else{
                map.put(fixedWords, 1);
            }
        }
		
        
        List<Word> wordList = new ArrayList<Word>();
        
        map.entrySet().forEach(e -> wordList.add(new Word(e.getKey(),e.getValue())));
        Collections.sort(wordList);
        
		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
			sql.append(iter.next());
			placeholders.append("?");
			
			if (iter.hasNext()) {
				sql.append(",");
				placeholders.append(",");
			}
		}
		
		sql.append(") VALUES (").append(placeholders).append(")");
		PreparedStatement posted = con.prepareStatement(sql.toString());
		int i = 0;
		
		for (Integer value : map.values()) {
			posted.setObject(i++, value);
		}
		
		posted.executeUpdate();

        return wordList.stream().limit(limit).toList();
    }
}