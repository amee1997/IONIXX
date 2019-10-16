package ionixx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) throws NumberFormatException, IOException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");  
		   Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/ionixx","root","");  
			
			if (con != null)
			{
				System.out.println("Connected");
			}
			else
			{
				System.out.println("not Connected");
			}
			
			Statement statement = con.createStatement();
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		do {
		System.out.println("Menu\n1.Admin Login");
		System.out.println("2.Agent Login");
		System.out.println("3.Exit");
		int n;
		int qavail;
		n=Integer.parseInt(br.readLine());
		boolean flag=false;
		
		switch(n) {
		case 1:
			  String uname,pass;
			  System.out.println("Enter the Username");
			  uname=br.readLine();
			  System.out.println("Enter the Password");
			  pass=br.readLine();
			  ResultSet res = statement.executeQuery("select username,password from alogin where username='"+uname+"' AND password='"+pass+"'");
			  if(res.next()) {
				  System.out.println("Welcome"+" "+uname);
				  System.out.println("Menu\n1.Add product\n2.Display Inventory Details\n3.Logout");
			  }
			  
				  int m;
					m=Integer.parseInt(br.readLine());
					switch(m) {
					
					case 1:
						do {
						 System.out.println("Enter the Product Id");
						 int pid,pq,price;String pname;
						 pid=Integer.parseInt(br.readLine());
						 System.out.println("Enter the Product Name");
						 pname=br.readLine();
						 System.out.println("Enter the MinSellQuantity");
						 pq=Integer.parseInt(br.readLine());
						 System.out.println("Enter the Price");
						 price=Integer.parseInt(br.readLine());
                         statement.executeUpdate("INSERT INTO addproduct(product_id,product_name,min_sell_quantity,price) VALUES('"+pid+"','"+pname+"','"+pq+"','"+price+"')");
                         System.out.println("Do you want to add any other product(Yes/No)");
                         String more=br.readLine();
                         if(more.equalsIgnoreCase("yes"))
                         {
                        	 flag=true;
                         }
                         else
                         {
                        	 flag=false;
                         }
					        }while(flag);
					         break;
					case 2:
					{    
						 System.out.println("Enter the product Id");
					     int pid,Price= 0;
					     String pname;
					     pid=Integer.parseInt(br.readLine());
					     System.out.println("Enter the Product Name");
						 pname=br.readLine();
						 System.out.println("Enter the quantity available");  
						 
						 qavail=Integer.parseInt(br.readLine());
						 statement.executeUpdate("update addproduct set quantity_available='"+qavail+"' where product_id='"+pid+"' and product_name='"+pname+"'");
						 String q="Select * from addproduct";
						 ResultSet rl=statement.executeQuery(q);
						 System.out.println("**********************Inventory Details are:***********************");
							if(rl.next()){ 
								do{
								    
									
									System.out.println(rl.getInt(1)+","+rl.getString(2)+","+rl.getInt(3)+","+rl.getInt(4)+","+rl.getInt(5)+","+rl.getInt(6));
									System.out.println("----------------------------------------------------------------------------------------------");
								}while(rl.next());
							}
							else{
								System.out.println("Record Not Found...");
							}
						 }
					
					     break;
					case 3:
					{
						System.out.println("Successfully Logged Out");
						break;
					}
					}
					break;
		case 2:
		{
			  String agname,agpass;
			  System.out.println("Enter the Agentname");
			  agname=br.readLine();
			  
			  System.out.println("Enter the Password");
			  agpass=br.readLine();
			  
			  ResultSet ru = statement.executeQuery("select username,password from aglogin where username='"+agname+"' AND password='"+agpass+"'");
			  if(ru.next()) {
				  System.out.println("Welcome"+" "+agname);
				  
				  System.out.println("Menu\n1.Buy/Sell\n2.Show History\n3.Logout");
			  }
			  
			  int a;
				a=Integer.parseInt(br.readLine());
				switch(a) {
				
				case 1:
					
					 System.out.println("Enter the Product Id");
					 int pid,Price= 0;
					 pid=Integer.parseInt(br.readLine());
					
					 ResultSet rt = statement.executeQuery("select price,product_name from addproduct where product_id='"+pid+"'");
					 if(rt.next())
					 {
						 int aprice = rt.getInt("price");
					     String aproduct = rt.getString("product_name");
					     System.out.println("Price is:"+aprice);
					     System.out.println("Product Name is:"+aproduct);
					 }
					 String tr;
				     System.out.println("Enter the date");
				     String date=br.readLine();
				     System.out.println("Transaction(Buy/Sell)");
					 tr=br.readLine();
					 if(tr.equalsIgnoreCase("BUY")) {
				     System.out.println("Enter the quantity");  
					 int quant;
					 quant=Integer.parseInt(br.readLine());
					 ResultSet rc = statement.executeQuery("select min_sell_quantity from addproduct where min_sell_quantity='"+quant+"'");
                   if(rc.next())
                   {
                	   System.out.println("The quantity is available");
                	   
                   }
                   ResultSet qa = statement.executeQuery("select quantity_available from addproduct where product_id='"+pid+"'");
                   int blnc = 0;
                   if(qa.next())
                   {   
                	  
                	   blnc = qa.getInt("quantity_available");
                	   
                   }
                   
                   
				int newq=blnc-quant;
				
				statement.executeUpdate("update addproduct set quantity_available='"+newq+"' where product_id='"+pid+"' ");
					 ResultSet rs = statement.executeQuery("select price from addproduct where product_id='"+pid+"' ");
					 while(rs.next())
					 {
						 Price = rs.getInt("price");
					 }
					 int tcost=Price*newq;
					 statement.executeUpdate("update addproduct set total_cost='"+tcost+"' where product_id='"+pid+"' ");
					 }
					 else
					 {
						 if(tr.equalsIgnoreCase("sell")) {
							 
		                   ResultSet qa = statement.executeQuery("select quantity_available from addproduct where product_id='"+pid+"'");
		                   int blnc = 0;
		                   if(qa.next())
		                   {   
		                	  
		                	   blnc = qa.getInt("quantity_available");
		                	   System.out.println("Already available quantity is:"+blnc);
		                   }
		                  
		                   System.out.println("Enter the quantity");  
							 int quant;
							 quant=Integer.parseInt(br.readLine()); 
						int newq=blnc+quant;
						
						statement.executeUpdate("update addproduct set quantity_available='"+newq+"' where product_id='"+pid+"' ");
							 ResultSet rs = statement.executeQuery("select price from addproduct where product_id='"+pid+"' ");
							 while(rs.next())
							 {
								 Price = rs.getInt("price");
							 }
							 int tcost=Price*newq;
							 statement.executeUpdate("update addproduct set total_cost='"+tcost+"' where product_id='"+pid+"' ");
							 }
						 statement.executeUpdate("INSERT INTO history (username,transaction,date) VALUES ('"+agname+"','"+tr+"','"+date+"')"); 
					 }
					 
				         break;
			  case 2:
			    {
			    	
			    	String q=("Select * from history where username='"+agname+"'");
					 ResultSet rl=statement.executeQuery(q);
					 System.out.println("**********************Your details are:***********************");
						if(rl.next()){ 
							do{
							    
								
								System.out.println(rl.getString(3)+"  ,  "+rl.getString(2));
								System.out.println("----------------------------------------------------------------------------------------------");
							}while(rl.next());
						}
						else{
							System.out.println("Record Not Found...");
						}
			
				}
			    break;
			  case 3:
			  {
				  System.out.println("Successfully Logged Out");
					break;
			  }
			  
            }
	     }
      }
   }while(true);
 }
}

				
			  
		
	


