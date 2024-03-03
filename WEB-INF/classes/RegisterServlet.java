
// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*; // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/queryregister") // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class RegisterServlet extends HttpServlet {

    // The doGet() runs once per HTTP GET request to this servlet.
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set the MIME type for the response message
        response.setContentType("text/html");
        // Get a output writer to write the response message into the network socket
        PrintWriter out = response.getWriter();
        // Print an HTML page as the output of the query
        out.println("<html>");
        out.println("<head><title>Query Response</title></head>");
        out.println("<body>");

        try (
                // Step 1: Allocate a database 'Connection' object
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/phoneshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        "myuser", "xxxx"); // For MySQL
                // The format is: "jdbc:mysql://hostname:port/databaseName", "username",
                // "password"

                // Step 2: Allocate a 'Statement' object in the Connection
                Statement stmt = conn.createStatement();) {
            // Step 3: Execute a SQL SELECT query
            String sqlStr1 = "select * from customer where username = "
                    + "'" + request.getParameter("username") + "'"; // Single-quote SQL string

            out.println("<p>Your SQL statement is: " + sqlStr1 + "</p>"); // Echo for debugging
            ResultSet rset = stmt.executeQuery(sqlStr1); // Send the query to the server

            // Check if new user
            if (!rset.isBeforeFirst()) {
                String sqlStr2 = "INSERT INTO customer (username, password) VALUES ('"
                        + request.getParameter("username") + "', '" + request.getParameter("password") + "')";
                int count = stmt.executeUpdate(sqlStr2);
                out.println("<p>Your SQL statement is: " + sqlStr2 + "," + count + "record inserted.</p>"); // Echo for debugging
            } else {
                out.println("<p>" + request.getParameter("username") + " already exists!</p>");
            }
        } catch (Exception ex) {
            out.println("<p>Error: " + ex.getMessage() + "</p>");
            out.println("<p>Check Tomcat console for details.</p>");
            ex.printStackTrace();
        } // Step 5: Close conn and stmt - Done automatically by try-with-resources (JDK
          // 7)

        out.println("</body></html>");
        out.close();
    }

    // The new doPost() runs the doGet() too
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Re-direct POST request to doGet()
    }
}