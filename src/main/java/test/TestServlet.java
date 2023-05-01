package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.DatabaseUtil;

/*
 * Server -> context.xml
 *<Resource name="jdbc/myDB" auth="Container" type="javax.sql.DataSource"
               username="username" password="password" driverClassName="com.microsoft.sqlserver.jdbc.SQLServerDriver"
               url="jdbc:sqlserver://localhost:1433;databaseName=Servlet;TrustServerCertificate=true"/>
 */

public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		Connection connection = null;

		try {
			connection = DatabaseUtil.getConnection();
			Statement statement = connection.createStatement();
			String sql = "IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'users') AND type in (N'U')) "
					+ "CREATE TABLE test (id INT NOT NULL PRIMARY KEY, name VARCHAR(255) NOT NULL)";
			statement.executeUpdate(sql);
			out.println("<html><body><h1>Table created successfully!</h1></body></html>");
			connection.close();
		} catch (SQLException e) {
			out.println("<html><body><h1>Error creating table!</h1></body></html>");
			e.printStackTrace(out);
		}
	}
}
