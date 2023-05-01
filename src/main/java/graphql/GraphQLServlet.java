package graphql;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import graphql.schema.GraphQLSchema;

@WebServlet("/graphql")
public class GraphQLServlet extends HttpServlet {
    
	private static final long serialVersionUID = 1L;
	private GraphQLSchema schema;

    @Override
    public void init() throws ServletException {
        super.init();
        schema = (GraphQLSchema) getServletContext().getAttribute("graphql.schema");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GraphQL graphQL = GraphQL.newGraphQL(schema).build();
        String query = request.getParameter("query");
        Object result = graphQL.execute(query).getData();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result.toString());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.getRequestDispatcher("WEB-INF/views/graphql.jsp").forward(request, response);
    }
}
