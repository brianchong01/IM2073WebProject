
// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*; // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/queryorder") // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class OrderServlet extends HttpServlet {

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

                // Step 2: Allocate a 'Statement' object in the Connection
                Statement stmt = conn.createStatement();) {
            // Step 3: Execute a SQL SELECT query
            String sqlStr1 = "select * from product where id = "
                    + "'" + request.getParameter("productId") + "'"; // Single-quote SQL string

            // out.println("<p>Your SQL statement is: " + sqlStr1 + "</p>"); // Echo for
            // debugging
            ResultSet rset = stmt.executeQuery(sqlStr1); // Send the query to the server
            String brand = "", model = "", quantity = "", customerId = "", username="", message = "";
            int stockQuantity = 0;

            while (rset.next()) {
                brand = rset.getString("brand");
                model = rset.getString("model");
                quantity = request.getParameter("quantity");
                customerId = request.getParameter("customerId");
                username = request.getParameter("username");
                stockQuantity = rset.getInt("quantity");
                if (quantity.isEmpty()) {
                    quantity = "1";
                }
            }
            // Check if product is in stock
            if (stockQuantity >= Integer.parseInt(quantity)) {
                String sqlStr2 = "INSERT INTO order_records (customerId, brand, model, quantity) VALUES (" + customerId
                        + ",'"
                        + brand + "', '" + model + "', "
                        + quantity + ")";
                int count = stmt.executeUpdate(sqlStr2);
                // Echo for debugging
                // out.print("quantity: " + quantity + " cid: " + customerId);
                // out.println("<p>Your SQL statement is: " + sqlStr2 + "," + count + "record
                // inserted.</p>");

                String sqlStr3 = "UPDATE product SET quantity = quantity-" + quantity + " WHERE id ="
                        + request.getParameter("productId");
                int count2 = stmt.executeUpdate(sqlStr3);
                // out.println("<p>Your SQL statement is: " + sqlStr3 + "," + count2 + "record
                // updated.</p>");

                // Construct success message
                message = "Order received. Thank you for choosing Huat Brothers!";
            } else {
                // Construct failure message
                message = model + " is out of stock! Check back again later!";
                //out.println("<p>" + rset.getString("model") + " is out of stock!</p>");
            }
            // Send message back to LoginServlet
            request.setAttribute("message", message);
            request.setAttribute("returnCustomerId", customerId);
            request.setAttribute("username", username);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/querylogin");
            dispatcher.forward(request, response);
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