package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource){
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception{
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try{
		//get Connection
			myConn = dataSource.getConnection();
		
		//sql statement
			String sql = "select * from student order by last_name";
		
			myStmt = myConn.createStatement();
		//execute query
			myRs = myStmt.executeQuery(sql);
		
		//process result set
			while(myRs.next()){
				//retrive data from resultset
					int id = myRs.getInt("id");
					String firstName = myRs.getString("first_name");
					String lastName = myRs.getString("last_name");
					String email = myRs.getString("email");
				//create new student
					Student tempStudent = new Student(id, firstName, lastName, email);
					
				//add it to the list of students
					students.add(tempStudent);
			}
		
			return students;
		}
		finally{
			//close JDBC objects
			close(myConn, myStmt, myRs);
		}
		
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		// TODO Auto-generated method stub
		try{
			if(myRs != null) myRs.close();
			if(myStmt != null) myStmt.close();
			if(myConn != null) myConn.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void addStudent(Student theStudent) throws Exception{
		// TODO Auto-generated method stub
		
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try{
			myConn=dataSource.getConnection();
			
		//create sql for insert
			String sql = "insert into student "
				    + "(first_name, last_name, email) "
				    + "values(?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);
			
		//set the param values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());

		//execute sql insert
			myStmt.execute();
		}
		finally{
			//clean up jdbc objects
			close(myConn, myStmt, null);
		}
	}

	public Student getStudents(String theStudentId) throws Exception {
		// TODO Auto-generated method stub
		
		Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		int studentId;
		
		try{
			//convert studentId to int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to db
			myConn = dataSource.getConnection();
			
			String sql = "select * from student where id=?";
			
			//create prepared statement
			myStmt = myConn.prepareStatement(sql);
			
			//set param
			myStmt.setInt(1, studentId);
			
			//execute statement
			myRs = myStmt.executeQuery();
			
			//retrieve data
			if(myRs.next()){
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//use the studentId during construction
				theStudent = new Student(studentId, firstName, lastName, email);
			}
			else{
				throw new Exception("Could not find student id: "+ studentId);
			}
			
			return theStudent;
		}
		finally{
			close(myConn, myStmt, myRs);
			
		}
		
	}

	public void updateStudent(Student theStudent) throws Exception {
		// TODO Auto-generated method stub
		
		//Student theStudent = null;
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try{
		//get db connection
			myConn = dataSource.getConnection();
		
		//create sql update statement
			String sql = "update student "
					+ "set first_name=?, last_name=?, email=? "
					+ "where id=?";
		
		//prepare statement
			myStmt = myConn.prepareStatement(sql);
		
		//set params
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
		//execute sql statement
			myStmt.execute();
		
		
		}
		finally{
			close(myConn, myStmt, null);
		}
	}

	public void deleteStudent(String theStudentId) throws Exception{
		// TODO Auto-generated method stub
		
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try{
			int studentId = Integer.parseInt(theStudentId);
			
			myConn = dataSource.getConnection();
			
			String sql = "delete from student where id=?";
			
			myStmt = myConn.prepareStatement(sql);
			
			myStmt.setInt(1, studentId);
			
			myStmt.execute();
		}
		finally{
			close(myConn, myStmt, null);
		}
		
	}
}
