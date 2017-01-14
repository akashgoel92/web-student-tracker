package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private StudentDbUtil studentDbUtil;
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		//Create an instance of student db util
		
		try{
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch(Exception ex){
			throw new ServletException(ex);
		}
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		try{
			//read the command parameter
			String theCommand = request.getParameter("command");
			
			//route to the appropriate method
			if(theCommand == null)
				theCommand = "LIST";
			
			switch(theCommand){
				case "LIST":
					listStudents(request, response);
					break;
					
				case "ADD":
					addStudents(request, response);
					break;
					
				case "LOAD":
					loadStudent(request, response);
					break;
					
				case "UPDATE":
					updateStudent(request, response);
					break;
					
				case "DELETE":
					deleteStudent(request, response);
					break;
					
				default:
					listStudents(request, response);
					
			}
			
		//list the students in MVC fashion
			listStudents(request, response);
		}
		catch(Exception ex){
			throw new ServletException(ex);
		}
		
	}



	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
			
		String theStudentId = request.getParameter("studentId");
		
		studentDbUtil.deleteStudent(theStudentId);
		
		listStudents(request, response);
		
	}



	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// TODO Auto-generated method stub
		
		//read student info
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		
		//create a new student object
		Student theStudent = new Student(id, firstName, lastName, email);
		
		//perform update on database
		studentDbUtil.updateStudent(theStudent);
		
		//send them back to the "list students" page
		listStudents(request, response);
	}



	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		//read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		//get student from database
		Student theStudent = studentDbUtil.getStudents(theStudentId);
		
		//place student in request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		//send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		
		dispatcher.forward(request, response);
	}



	private void addStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		//read student info
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		
		//create new student
		Student theStudent = new Student(firstName, lastName, email);
		
		//add the student to the database
		studentDbUtil.addStudent(theStudent);
		
		//send back to main page
		listStudents(request, response);
	}



	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// TODO Auto-generated method stub
		//get Student from db util
		List<Student> students = studentDbUtil.getStudents();
		
		//add students to the request 
		request.setAttribute("STUDENT_LIST", students);
		
		//send to jsp page(view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}



}
