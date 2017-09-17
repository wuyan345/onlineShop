package com.shop.utils;

import java.sql.*;

public class Database {
	
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://43.224.33.64:3306/shop?characterEncoding=utf-8";
    // Database credentials
    static final String USER = "wy";
    static final String PASS = "123456";
    static final int N = 8;
    
	public void DBWriteData(String name, String gender, String birthday, String address, 
						String email, String cellphone, String description){
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
										ResultSet.CONCUR_UPDATABLE);
			String sql = "SELECT * FROM customers";
			ResultSet response = stmt.executeQuery(sql);
			
			response.moveToInsertRow();
			response.updateString("name", name);
			response.updateString("gender", gender);
			response.updateString("address", address);
			response.updateString("birthday", birthday);
			response.updateString("cellphone", cellphone);
			response.updateString("email", email);
			response.updateString("description", description);
			response.insertRow();
			response.moveToCurrentRow();
			
			//response.absolute(1);
			//response.deleteRow();
		}catch(ClassNotFoundException ce){
			ce.printStackTrace();
		}catch(SQLException se){
			se.printStackTrace();
		}finally{
			try{
				//STEP 6: Clean-up environment
				stmt.close();
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		System.out.println("Operation done");
	}
	
	public static void main(String[] args) {
		new Database().test();
	}
	
	public void test(){
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
										ResultSet.CONCUR_UPDATABLE);
//			String sql = "SELECT * FROM user";
			String sql = "insert into role(`user_id`,`role_no`) values(123, 123)";
			boolean response = stmt.execute(sql);
//			ResultSet response = stmt.executeQuery(sql);
//			
//			while(response.next()){
//				if(response.getString("id").equals(id))
//					break;
//			}
//			int row = response.getRow();
//			//System.out.println(row);
//			response.absolute(row);
//			response.deleteRow();
			
		}catch(ClassNotFoundException ce){
			ce.printStackTrace();
		}catch(SQLException se){
			se.printStackTrace();
		}finally{
			try{
				//STEP 6: Clean-up environment
				stmt.close();
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
	
	public String[][] DBReadData(){
		Connection conn = null;
		Statement stmt = null;
		String[][] data = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//STEP 3: Open a connection
			//System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//STEP 4: Execute a query
			//System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
										ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM customers";
			ResultSet response = stmt.executeQuery(sql);
			
			int count = 0;
			if(response.last()){
				count = response.getRow();
				data = new String[count][N];
				response.beforeFirst();
				for(int i=0; i<count; i++){
					if(response.next()){
						for(int j=0; j<N; j++)
							data[i][j] = response.getString(j+1);
					}
				}
			}
			
			//System.out.println(count);
			
		}catch(ClassNotFoundException ce){
			ce.printStackTrace();
		}catch(SQLException se){
			se.printStackTrace();
		}finally{
			try{
				//STEP 6: Clean-up environment
				stmt.close();
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return data;
	}
	
	public void DBDelete(String id){
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
										ResultSet.CONCUR_UPDATABLE);
			String sql = "SELECT * FROM customers";
			ResultSet response = stmt.executeQuery(sql);
			
			while(response.next()){
				if(response.getString("id").equals(id))
					break;
			}
			int row = response.getRow();
			//System.out.println(row);
			response.absolute(row);
			response.deleteRow();
			
		}catch(ClassNotFoundException ce){
			ce.printStackTrace();
		}catch(SQLException se){
			se.printStackTrace();
		}finally{
			try{
				//STEP 6: Clean-up environment
				stmt.close();
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
	
	public void DBUpdateData(String id, String[] data){
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
										ResultSet.CONCUR_UPDATABLE);
			String sql = "SELECT * FROM customers";
			ResultSet response = stmt.executeQuery(sql);
			
			while(response.next()){
				if(response.getString("id").equals(id))
					break;
			}
			int row = response.getRow();
			response.absolute(row);
			response.updateString("name", data[0]);
			response.updateString("gender", data[1]);
			response.updateString("birthday", data[2]);
			response.updateString("cellphone", data[3]);
			response.updateString("address", data[4]);
			response.updateString("email", data[5]);
			response.updateString("description", data[6]);
			response.updateRow();
			
		}catch(ClassNotFoundException ce){
			ce.printStackTrace();
		}catch(SQLException se){
			se.printStackTrace();
		}finally{
			try{
				//STEP 6: Clean-up environment
				stmt.close();
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
	
	public String[] DBSearch(String name, String cellphone){
		Connection conn = null;
		Statement stmt = null;
		String[] data = new String[N];
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			
			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
										ResultSet.CONCUR_UPDATABLE);
			String sql = "SELECT * FROM customers";
			ResultSet response = stmt.executeQuery(sql);
			
			while(response.next()){
				if(response.getString("name").equals(name)){
					if(response.getString("cellphone").equals(cellphone))
						break;
				}
			}
			int row = response.getRow();
			//System.out.println("row: " + row);
			response.absolute(row);
			for(int i=0; i<N; i++)
				data[i] = response.getString(i+1);
			
		}catch(ClassNotFoundException ce){
			ce.printStackTrace();
		}catch(SQLException se){
			se.printStackTrace();
		}finally{
			try{
				//STEP 6: Clean-up environment
				stmt.close();
				conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return data;
	}
}

