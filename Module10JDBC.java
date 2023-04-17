package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Module10JDBC {

	public static void main(String[] args) throws Exception {
		getConnection();
		truncate();
		post();
		get();
		count();
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
	 public static ArrayList<String> get() throws Exception{
	        try{
	            Connection con = getConnection();
	            PreparedStatement statement = con.prepareStatement("SELECT word FROM word ORDER BY word");
	            
	            ResultSet result = statement.executeQuery();
	            
	            ArrayList<String> array = new ArrayList<String>();
	            while(result.next()){
	                System.out.println(result.getString("word"));
	               // System.out.print(" ");
	              //  System.out.println(result.getString("last"));
	                
	               // array.add(result.getString("last"));
	            }
	            System.out.println("All records have been selected!");
	            return array;
	            
	        }catch(Exception e){System.out.println(e);}
	        return null;
	        
	    }
	 
	    public static void post() throws Exception{
	        final String var1 = "the";
	        final String var2 = "test";
	        final String var3 = "raven";
	        final String var4 = "a";
	        final String var5 = "this";
	        
	        try{
	            Connection con = getConnection();
	            PreparedStatement posted = con.prepareStatement("INSERT INTO word (word) VALUES ('"+var1+"', '"+var2+"', '"+var3+"', '"+var4+"', '"+var5+"')");
	            
	            posted.executeUpdate();
	        } catch(Exception e){System.out.println(e);}
	        finally {
	            System.out.println("Insert Completed.");
	        }
	    }
	    
	    public static void truncate() throws Exception{
	        try{
	            Connection con = getConnection();
	            PreparedStatement truncate = con.prepareStatement("Truncate table word");
	            
	            truncate.executeUpdate();
	        } catch(Exception e){System.out.println(e);}
	        finally {
	            System.out.println("Truncate complete.");
	        }
	    }
	    
	    public static ArrayList<String> count() throws Exception{
	        try{
	            Connection con = getConnection();
	            PreparedStatement statement = con.prepareStatement("SELECT word,count(word) as Occurrence FROM word where word='test';");
	            
	            statement.executeQuery();
	            
	            System.out.println("Counting occurrences.");
	            
	            
	            
	        }catch(Exception e){System.out.println(e);}
	        return null;
	        
	    }
	    
	    
	  
	    /*
	    public static void createTable() throws Exception{
	        try{
	            Connection con = getConnection();
	            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS iamalive(id int NOT NULL AUTO_INCREMENT, first varchar(255), last varchar(255), PRIMARY KEY(id))");
	            create.executeUpdate();         
	        }catch(Exception e){System.out.println(e);}
	        finally{
	            System.out.println("Function Complete.");
	            };
	        
	    }
		*/
}
