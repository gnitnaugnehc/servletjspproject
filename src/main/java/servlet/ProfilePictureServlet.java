package servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.ProfilePictureUtil;

@WebServlet("/profile_picture")
public class ProfilePictureServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String profilePictureId = request.getParameter("id");
		byte[] profilePicture = null;

		if (profilePictureId != null) {
			try {
				profilePicture = ProfilePictureUtil.getProfilePicture(profilePictureId);
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
		}

		if (profilePicture == null) {
			String path = getServletContext().getRealPath("/default.png");
			File file = new File(path);

			try (FileInputStream fileInputStream = new FileInputStream(file);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

				byte[] buffer = new byte[4096];
				int length;
				while ((length = fileInputStream.read(buffer)) > 0) {
					byteArrayOutputStream.write(buffer, 0, length);
				}

				profilePicture = byteArrayOutputStream.toByteArray();
			}
		}

		response.getOutputStream().write(profilePicture);
	}

}
