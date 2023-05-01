package init;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import util.DatabaseUtil;

@WebListener
public class Initializer implements ServletContextListener {

	private static final String CREATE_USERS_TABLE_QUERY = "CREATE TABLE users (" 
		    + "id BIGINT PRIMARY KEY IDENTITY(1,1),"
		    + "username VARCHAR(255) NOT NULL," 
		    + "password VARCHAR(255) NOT NULL," 
		    + "email VARCHAR(255) NOT NULL,"
		    + "profile_picture_id VARCHAR(255) NULL,"
		    + "created_at DATETIME NOT NULL," 
		    + "last_login DATETIME NOT NULL"
		    + ");";
		    
	private static final String CREATE_PROFILE_PICTURES_TABLE_QUERY = "CREATE TABLE profile_pictures (" 
		    + "id VARCHAR(255) PRIMARY KEY,"
		    + "user_id BIGINT NOT NULL,"
		    + "image VARBINARY(MAX) NOT NULL,"
		    + "updated_at DATETIME NOT NULL,"
		    + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
		    + ");";

	private static final String CREATE_TASK_TABLE_QUERY = "CREATE TABLE task (" 
			+ "id BIGINT PRIMARY KEY IDENTITY(1,1),"
			+ "title VARCHAR(255) NOT NULL," 
			+ "description TEXT NOT NULL," 
			+ "deadline DATETIME NOT NULL,"
			+ "completed BIT NOT NULL," 
			+ "created_at DATETIME NOT NULL," 
			+ "updated_at DATETIME NOT NULL" 
			+ ");";
	
	private static final String CREATE_USER_TASK_TABLE_QUERY = "CREATE TABLE user_task (" 
	        + "id BIGINT PRIMARY KEY IDENTITY(1,1),"
	        + "user_id BIGINT NOT NULL,"
	        + "task_id BIGINT NOT NULL,"
	        + "created_at DATETIME NOT NULL," 
	        + "updated_at DATETIME NOT NULL," 
	        + "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
	        + ");";
	
	private void createTableIfNotExists(String tableName, String createQuery) throws SQLException {
	    try (Connection conn = DatabaseUtil.getConnection()) {
	        DatabaseMetaData metaData = conn.getMetaData();
	        ResultSet rs = metaData.getTables(null, null, tableName.toUpperCase(), null);
	        if (!rs.next()) {
	            try (Statement stmt = conn.createStatement()) {
	                stmt.executeUpdate(createQuery);
	            }
	        }
	    }
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		
	    try {
	        createTableIfNotExists("USERS", CREATE_USERS_TABLE_QUERY);
	    	createTableIfNotExists("PROFILE_PICTURES", CREATE_PROFILE_PICTURES_TABLE_QUERY);
	        createTableIfNotExists("TASK", CREATE_TASK_TABLE_QUERY);
	        createTableIfNotExists("USER_TASK", CREATE_USER_TASK_TABLE_QUERY);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to create tables", e);
	    }
		
	    ServletContext context = sce.getServletContext();
	    String contextPath = context.getContextPath();
	    context.setAttribute("rootDirectory", contextPath);
	    
	    InputStream is = context.getResourceAsStream("/WEB-INF/schema/schema.graphql");
        SchemaParser parser = new SchemaParser();
        TypeDefinitionRegistry registry = parser.parse(new InputStreamReader(is));

        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("Hello, world!")))
                .build();

        SchemaGenerator generator = new SchemaGenerator();
        GraphQLSchema schema = generator.makeExecutableSchema(registry, wiring);
        context.setAttribute("graphql.schema", schema);
	}

}
