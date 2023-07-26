package project.controller;

import project.model.ErrorMessage;
import project.model.PasswordEncryption;
import project.model.RegisterDetail;

import java.io.File;
import com.oreilly.servlet.MultipartRequest;
import java.io.IOException;
import java.net.PasswordAuthentication;

import com.EmployeeManagementSystem.model.Employee_Detail;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.EmployeeManagementSystem.dao.EmployeeDao;
import project.model.register_detail;

/**
 * Servlet implementation class EmployeeMainServlet
 */
@WebServlet("/EmployeeMainServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 10, // 10MB
		maxRequestSize = 1024 * 1024 * 50)
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String INSERT = "rgister.jsp";
	private static String LIST_USER = "view/listEmployee.jsp";
	private static String UPDATE = "view/update.jsp";
	private static String loginEmployee = "view/EmployeeLogin.jsp";
	private static String employee_Profile = "view/EmployeeDetail.jsp";
	private static String login_error = "view/loginError";
	private project.doa.RegisterDoa dao;
	private static final String SAVE_DIR = "xampp\\tomcat\\webapps\\Image";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	private String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}

	public RegisterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		// ServletContext context=config.getServletContext();
		dao = new project.doa.RegisterDoa();
		// context.setAttribute("employeeDao", employeeDao);

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getParameter("parm");

		String forward;
		if (path.equalsIgnoreCase("addUser")) {
			forward = INSERT;
			RequestDispatcher patcher = request.getRequestDispatcher(forward);
			patcher.include(request, response);

		}
		if (path.equalsIgnoreCase("viewEmployee")) {
			forward = LIST_USER;
			RequestDispatcher patcher = request.getRequestDispatcher(forward);

			request.setAttribute("employeeList", dao.getAllUsers());
			patcher.forward(request, response);
			// request.setAttribute("user", dao.)
		}
		if (path.equalsIgnoreCase("edit")) {
			forward = UPDATE;
			String email = request.getParameter("email");
			RegisterDetail customer = dao.getUserById(email);
			request.getSession().setAttribute("email", email);
			request.setAttribute("customer", customer);
			RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
			dispatcher.forward(request, response);
		}
		if (path.equalsIgnoreCase("delete")) {

			String email = request.getParameter("email");
			dao.deleteById(email);
			RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
			request.setAttribute("customerList", dao.getAllUsers());
			view.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getParameter("submit");
		if (url.equalsIgnoreCase("insert")) {
			String employee_Name = request.getParameter("emp_name");
			long citizen_Number = Long.valueOf(request.getParameter("num"));
			String address = request.getParameter("address");
			long contact_Number = Long.valueOf(request.getParameter("txtFName1"));
			String job_Location = request.getParameter("JobLocation");
			String designation = request.getParameter("Designation");
			String email = request.getParameter("email_id");
			String passWord;
			try {
				passWord = PasswordEncryption.encrypt(email,request.getParameter("password"));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String savePath = "C:" + File.separator + SAVE_DIR;
			File fileSaveDir = new File(savePath);
			if (!fileSaveDir.exists()) {
				fileSaveDir.mkdir();
			}

			Part part = request.getPart("image");
			String fileName = getFileName(part);
			part.write(savePath + File.separator + fileName);
			String filePath = savePath + File.separator + fileName;

			RegisterDetail customer = new RegisterDetail(employee_Name, citizen_Number, address, contact_Number,
					job_Location, designation, email, passWord, fileName);
			try {
				dao.insertEmployeeToDatabase(customer);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			RequestDispatcher login = request.getRequestDispatcher(loginEmployee);
			request.setAttribute("employeeList", dao.getAllUsers());
			login.forward(request, response);

		}
		if (url.equalsIgnoreCase("update")) {

	String id=request.getSession(false).getAttribute("londonmet_id").toString();

			String employee_Name = request.getParameter("emp_name");
			long citizen_Number = Long.valueOf(request.getParameter("num"));
			String address = request.getParameter("address");
			long contact_Number = Long.valueOf(request.getParameter("txtFName1"));

			String email = request.getParameter("email_id");

			RegisterDetail employee = new RegisterDetail();
			employee.setEmployee_Name(employee_Name);
			employee.setCitizen_Number(citizen_Number);
			employee.setAddress(address);
			employee.setContact_Number(contact_Number);
			employee.setEmail(email);
			employee.setId(id);
			dao.updateEmployee(employee);

			RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
			request.setAttribute("employeeList", dao.getAllUsers());
			view.forward(request, response);

		}
		if (url.equalsIgnoreCase("login")) {

			String username = request.getParameter("email_id");

			String password = request.getParameter("Password");
			String login_value = "";
			try {
				login_value = dao.getLogin(username, password);
				if (login_value.isEmpty()) {

					ErrorMessage message = new ErrorMessage("Pls check your username and password", "login_type",
							"alert-danger");
					request.getSession(false).setAttribute("errorMessage", message);
					RequestDispatcher dispatcherLogin = request.getRequestDispatcher(loginEmployee);
					dispatcherLogin.forward(request, response);
				} else {
					RegisterDetail employee = dao.getUserById(login_value);

					request.getSession(false).setAttribute("employee_detail", employee);
					RequestDispatcher dispatcherLogin = request.getRequestDispatcher(employee_Profile);
					dispatcherLogin.forward(request, response);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
