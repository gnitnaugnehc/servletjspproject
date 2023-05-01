package util;

import java.sql.SQLException;

import dao.UserDao;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import entity.User;

public class AuthUtil {

	private static final int HASH_ITERATIONS = 10;
	private static final int HASH_MEMORY = 65536;
	private static final int HASH_PARALLELISM = 1;

	public static User authenticateUserByEmail(String email, String password) throws SQLException {
		UserDao userDao = new UserDao();
		User user = userDao.findByEmail(email);

		if (user != null) {
			String hashedPassword = user.getPassword();

			Argon2 argon2 = Argon2Factory.create();

			if (argon2.verify(hashedPassword, password.toCharArray())) {
				argon2.wipeArray(password.toCharArray());
			    user.setPassword(null);
			    return user;
			}
		}

		return null;
	}

	public static User authenticateUserByUsername(String username, String password) throws SQLException {
		UserDao userDao = new UserDao();
		User user = userDao.findByUsername(username);

		if (user != null) {
			String hashedPassword = user.getPassword();

			Argon2 argon2 = Argon2Factory.create();

			if (argon2.verify(hashedPassword, password.toCharArray())) {
				argon2.wipeArray(password.toCharArray());
			    user.setPassword(null);
			    return user;
			}
		}

		return null;
	}

	public static boolean checkUsernameExists(String username) throws SQLException {
		UserDao userDao = new UserDao();
		User user = userDao.findByUsername(username);
		return user != null;
	}

	public static boolean checkEmailExists(String email) throws SQLException {
		UserDao userDao = new UserDao();
		User user = userDao.findByEmail(email);
		return user != null;
	}

	public static long createUser(String username, String password, String email) throws SQLException {
		String hashedPassword = hashPassword(password);
		UserDao userDao = new UserDao();
		return userDao.createUser(new User(username, hashedPassword, email));
	}

	public static String hashPassword(String password) {
		Argon2 argon2 = Argon2Factory.create();
		return argon2.hash(HASH_ITERATIONS, HASH_MEMORY, HASH_PARALLELISM, password.toCharArray());
	}
}
