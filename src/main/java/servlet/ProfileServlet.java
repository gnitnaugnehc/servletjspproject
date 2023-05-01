package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import entity.User;
import util.ProfilePictureUtil;

@WebServlet("/profile")
@MultipartConfig
public class ProfileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getSession().getAttribute("user") != null) {
			request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/signin");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/signin");
			return;
		}

		Part filePart = request.getPart("profilePicture");

		if (filePart == null) {
			request.getSession().setAttribute("error", "Please select a file to upload");
			response.sendRedirect(request.getContextPath() + "/profile");
			return;
		}

		byte[] profilePicture = filePart.getInputStream().readAllBytes();
		String profilePictureId = ProfilePictureUtil.updateProfilePicture(user.getId(), profilePicture);

		user.setProfilePictureId(profilePictureId);
		request.getSession().setAttribute("user", user);

		request.getSession().setAttribute("message", "Profile picture uploaded successfully");
		response.sendRedirect(request.getContextPath() + "/profile");
	}

}
