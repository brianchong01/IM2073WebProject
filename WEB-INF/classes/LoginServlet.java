
// To save as "ebookshop\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import jakarta.servlet.*; // Tomcat 10
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
//import javax.servlet.*;            // Tomcat 9
//import javax.servlet.http.*;
//import javax.servlet.annotation.*;

@WebServlet("/querylogin") // Configure the request URL for this servlet (Tomcat 7/Servlet 3.0 upwards)
public class LoginServlet extends HttpServlet {

        // The doGet() runs once per HTTP GET request to this servlet.
        @Override
        public void doGet(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
                // Set the MIME type for the response message
                response.setContentType("text/html");
                // Get a output writer to write the response message into the network socket
                PrintWriter out = response.getWriter();
                String customerId = "", username = "", message = "";
                // Print an HTML page as the output of the query
                out.println("<html>");
                out.println("<head>");
                out.println("<meta charset=\"UTF-8\">");
                out.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
                out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
                // out.println("<script src=\"assets/jquery-3.5.1.min.js\"></script>");
                // out.println("<script
                // src=\"assets/bootstrap-5.0.2-dist/bootstrap-5.0.2-dist/js/bootstrap.bundle.min.js\"></script>");
                // out.println(
                // "<link rel=\"stylesheet\"
                // href=\"assets/bootstrap-5.0.2-dist/bootstrap-5.0.2-dist/css/bootstrap.min.css\">");
                out.println("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">");
                out.println("<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>");
                out.println("<script src=\"assets/sweetalert2.all.min.js\"></script>");
                out.println("<link rel=\"stylesheet\" href=\"assets/styles.css\">");
                out.println("<title>Huat Brothers</title>");
                out.println("</head>");

                out.println("<body>");

                try (
                                // Step 1: Allocate a database 'Connection' object
                                Connection conn = DriverManager.getConnection(
                                                "jdbc:mysql://localhost:3306/phoneshop?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                                                "myuser", "xxxx"); // For MySQL

                                // Step 2: Allocate a 'Statement' object in the Connection
                                Statement stmt = conn.createStatement();) {

                        // Step 3: Execute a SQL SELECT query
                        String sqlStr1 = "SELECT * from customer WHERE username = ? AND password = ?"; // Single-quote                                                                                // SQL string
                        PreparedStatement preparedStatement = conn.prepareStatement(sqlStr1);

                        // Set parameters for the prepared statement
                        preparedStatement.setString(1, request.getParameter("username"));
                        preparedStatement.setString(2, request.getParameter("password"));
                        ResultSet rset = preparedStatement.executeQuery(); // Send the query to the server

                        // Returning from OrderServlet
                        customerId = (String) request.getAttribute("returnCustomerId");
                        message = (String) request.getAttribute("message");
                        username = (String) request.getAttribute("username");

                        // Check if user exists and login info is correct before showing main page
                        if (rset.isBeforeFirst() || customerId != null) {
                                if (rset.next()) {
                                        customerId = rset.getString("id");
                                        username = rset.getString("username");
                                } else {
                                        if (message.contains("Order received")) {
                                                out.println("<script type=\"text/javascript\">");
                                                out.println("Swal.fire({\r\n" + //
                                                                "  icon: \"success\",\r\n" + //
                                                                "  title: \"Order received!\",\r\n" + //
                                                                "  showConfirmButton: false,\r\n" + //
                                                                "  timer: 3000\r\n" + //
                                                                "});");
                                                out.println("</script>");
                                        } else if (message.contains("out of stock")) {
                                                out.println("<script type=\"text/javascript\">");
                                                out.println("Swal.fire({\r\n" + //
                                                                "  icon: \"error\",\r\n" + //
                                                                "  title: \"Out of stock!\",\r\n" + //
                                                                "  showConfirmButton: false,\r\n" + //
                                                                "  timer: 3000\r\n" + //
                                                                "});");
                                                out.println("</script>");
                                        } else {
                                                // Do nothing
                                        }
                                }
                                String sqlStr2 = "SELECT brand, model, SUM(quantity) AS quantity from order_records WHERE customerId = "
                                                + customerId + " GROUP BY model, brand";
                                ResultSet rset2 = stmt.executeQuery(sqlStr2);

                                // out.println("<p>Your SQL statement is: " + sqlStr2 + "</p>"); // Echo for
                                // debugging

                                // Load page content
                                // Nav bar
                                out.println("<nav id=\"desktopNav\" class=\"sticky-top\">\r\n" + //
                                                "        <ul id=\"defaultNav\" class=\"navList\">\r\n" + //
                                                "            <li id=\"navBrand\"><a href=\"#top\"><img src=\"assets/images/Huat.jpg\" /></a></li>\r\n"
                                                + //
                                                "            <li class=\"defaultLink\"><a href=\"#productH1\">Products</a></li>\r\n"
                                                + //
                                                "            <li class=\"defaultLink\"><a href=\"#ourStoryH1\">Our Story</a></li>\r\n"
                                                + //
                                                "            <li class=\"defaultLink\"><a href=\"#contactH1\">Contact us</a></li>\r\n"
                                                + "<li class=\"dropdownList\"><div class=\"dropdown\">\r\n" + //
                                                "  <button id=\"dropdownBtn\" class=\"btn btn-secondary dropdown-toggle\" type=\"button\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\">\r\n"
                                                + //
                                                "Hi " + username + //
                                                "  </button>\r\n" + //
                                                "  <ul class=\"dropdown-menu\">\r\n" + //
                                                "    <li><a class=\"dropdown-item\" href=\"index.html\">Log out</a></li>\r\n"
                                                + //
                                                "  </ul>\r\n" + //
                                                "</div></li>"
                                                + //
                                                "       <li id=\"cart\"><a data-bs-toggle=\"modal\" data-bs-target=\"#orderModal\"><img src=\"assets/images/cartIcon.png\" /></a></li>\r\n"
                                                +
                                                "   </ul>\r\n" + //
                                                "</nav>");
                                // Carousel start
                                out.println("<div id=\"demo\" class=\"carousel slide\" data-bs-ride=\"carousel\">\r\n" + //
                                                "        <!-- Indicators/dots -->\r\n" + //
                                                "        <div class=\"carousel-indicators\">\r\n" + //
                                                "            <button type=\"button\" data-bs-target=\"#demo\" data-bs-slide-to=\"0\" class=\"active\"></button>\r\n"
                                                + //
                                                "            <button type=\"button\" data-bs-target=\"#demo\" data-bs-slide-to=\"1\"></button>\r\n"
                                                + //
                                                "        </div>\r\n" + //
                                                "\r\n" + //
                                                "        <!-- The slideshow/carousel -->\r\n" + //
                                                "        <div class=\"carousel-inner\">\r\n" + //
                                                "            <div class=\"carousel-item active\">\r\n" + //
                                                "                <img class=\"carouselImg\" src=\"assets/images/HuatBrothersBanner2.png\" alt=\"slide1\" class=\"d-block\" style=\"width:100%\">\r\n"
                                                + //
                                                "            </div>\r\n" + //
                                                "            <div class=\"carousel-item\">\r\n" + //
                                                "                <img class=\"carouselImg\" src=\"assets/images/HuatBrothersBanner3.png\" alt=\"slide2\" class=\"d-block\" style=\"width:100%\">\r\n"
                                                + //
                                                "            </div>\r\n" + //
                                                "            " + //
                                                "        </div>\r\n" + //
                                                "    </div>");
                                // Carousel end

                                out.println("<p id=\"awardHeader\">EXPERIENCE AWARD WINNING SERVICES</p>\r\n" + //
                                                "<div class=\"awardRow\">\r\n" + //
                                                "    <img id=\"award1\" class=\"award\" src=\"assets/images/design award.svg\" alt=\"award\">\r\n"
                                                + //
                                                "    <img id=\"award2\" class=\"award\" src=\"assets/images/good design.svg\" alt=\"award\">\r\n"
                                                + //
                                                "    <img id=\"award3\" class=\"award\" src=\"assets/images/red dot.svg\" alt=\"award\">\r\n"
                                                + //
                                                "</div>\r\n" + //
                                                "\r\n" + //
                                                "<div id=\"productContainer\">\r\n" +
                                                "<div class=\"productH1Section centerH1\">\r\n" + //
                                                "    <p id=\"productH1\">PRODUCTS</p>\r\n" + //
                                                "    <p>Featuring the latest high-end phones. <strong id=\"saleText\">ON SALE NOW!</strong></p>\r\n"
                                                + //
                                                "</div>");

                                // Modal
                                out.println("<div class=\"modal fade\" id=\"orderModal\" tabindex=\"-1\" aria-labelledby=\"orderModalLabel\" aria-hidden=\"true\">\r\n"
                                                + //
                                                "  <div class=\"modal-dialog\">\r\n" + //
                                                "    <div class=\"modal-content\">\r\n" + //
                                                "      <div class=\"modal-header\">\r\n" + //
                                                "        <h1 class=\"modal-title fs-5\" id=\"orderModalLabel\">Order Summary</h1>\r\n"
                                                + //
                                                "        <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\r\n"
                                                + //
                                                "      </div>\r\n" + //
                                                "      <div class=\"modal-body\">\r\n" + //
                                                "        <table class=\"table\">" + //
                                                "        <thead>" + //
                                                "        <tr>" + //
                                                "        <th scope=\"col\">Brand</th>" + //
                                                "        <th scope=\"col\">Model</th>" + //
                                                "        <th scope=\"col\">Quantity</th>" +
                                                "        </tr>" + //
                                                "        </thead>" + //
                                                "        <tbody>" //
                                );
                                while (rset2.next()) {
                                        out.println("<tr>");
                                        // out.println("<tr>test1</tr>");
                                        // out.println("<tr>test2</tr>");
                                        // out.println("<tr>test3</tr>");
                                        out.println("<td>" + rset2.getString("brand") + "</td>");
                                        out.println("<td>" + rset2.getString("model") + "</td>");
                                        out.println("<td>" + rset2.getString("quantity") + "</td>");
                                        out.println("</tr>");
                                }
                                out.println(
                                                "        </tbody>" + //
                                                                "        </table>" + //
                                                                "      </div>\r\n" + //
                                                                "      <div class=\"modal-footer\">\r\n" + //
                                                                "        <button type=\"button\" class=\"btn btn-secondary\" data-bs-dismiss=\"modal\">Close</button>\r\n"
                                                                + //
                                                                "      </div>\r\n" + //
                                                                "    </div>\r\n" + //
                                                                "  </div>\r\n" + //
                                                                "</div>");

                                out.println("<p id=\"itemCat1\" class=\"itemCat\">Apple</p>");
                                out.println("<div id=\"appleItems\">");
                                String sqlStr3 = "SELECT * from product WHERE brand = 'Apple'";
                                ResultSet rset3 = stmt.executeQuery(sqlStr3);
                                while (rset3.next()) {
                                        out.println("<div class=\"oneProduct\">");
                                        out.println("<img id=\"" + rset3.getString("id") + "Img\" src=\""
                                                        + rset3.getString("filepath")
                                                        + "\" alt=\"productImg\" class=\"hover\">");
                                        out.println("<p class=\"productName\">" + rset3.getString("model") + "</p>");
                                        out.println("<form class=\"quantityForm\" method=\"post\" action=\"queryorder\">");
                                        out.println("<input id=\"" + rset3.getString("id")
                                                        + "Qty\" class=\"quantityInp\" type=\"number\" placeholder=\"1\" min=\"1\" max=\"5\" name=\"quantity\">");
                                        out.println("<button id=\"" + rset3.getString("id")
                                                        + "Btn\" class=\"buyBtn btn\" type=\"submit\">Buy</button>");
                                        out.println("<input id=\"prod" + rset3.getString("id") + "\" value=\""
                                                        + rset3.getString("id")
                                                        + "\" type=\"hidden\" name=\"productId\">");
                                        out.println("<input id=\"cust" + customerId + "\" value=\""
                                                        + customerId
                                                        + "\" type=\"hidden\" name=\"customerId\">");
                                        out.println("<input id=\"cust" + username + "\" value=\""
                                                        + username
                                                        + "\" type=\"hidden\" name=\"username\">");
                                        out.println("</form>");
                                        out.println("</div>"); // One product
                                }
                                out.println("</div>");

                                out.println("<p id=\"itemCat2\" class=\"itemCat\">Samsung</p>");
                                out.println("<div id=\"samsungItems\">");
                                String sqlStr4 = "SELECT * from product WHERE brand = 'Samsung'";
                                ResultSet rset4 = stmt.executeQuery(sqlStr4);
                                while (rset4.next()) {
                                        out.println("<div class=\"oneProduct\">");
                                        out.println("<img id=\"" + rset4.getString("id") + "Img\" src=\""
                                                        + rset4.getString("filepath")
                                                        + "\" alt=\"productImg\" class=\"hover\">");
                                        out.println("<p class=\"productName\">" + rset4.getString("model") + "</p>");
                                        out.println("<form class=\"quantityForm\" method=\"post\" action=\"queryorder\">");
                                        out.println("<input id=\"" + rset4.getString("id")
                                                        + "Qty\" class=\"quantityInp\" type=\"number\" placeholder=\"1\" min=\"1\" max=\"5\" name=\"quantity\">");
                                        out.println("<button id=\"" + rset4.getString("id")
                                                        + "Btn\" class=\"buyBtn btn\" type=\"submit\">Buy</button>");
                                        out.println("<input id=\"prod" + rset4.getString("id") + "\" value=\""
                                                        + rset4.getString("id")
                                                        + "\" type=\"hidden\" name=\"productId\">");
                                        out.println("<input id=\"cust" + customerId + "\" value=\""
                                                        + customerId
                                                        + "\" type=\"hidden\" name=\"customerId\">");
                                        out.println("<input id=\"cust" + username + "\" value=\""
                                                        + username
                                                        + "\" type=\"hidden\" name=\"username\">");
                                        out.println("</form>");
                                        out.println("</div>"); // One product
                                }
                                out.println("</div>");

                                // About
                                out.println("<div id=\"ourStoryContainer\">" +
                                                "<div class=\"ourStoryH1Section centerH1\">"
                                                + "<p id=\"ourStoryH1\">Our Story</p>" + //
                                                "<p>In the chaotic world of online phone selling, every sale is a victory, and every \"huat\" is just a click away.</p>"
                                                +
                                                "<p>Support us on our journey and let us all huat together!</p>" +
                                                "</div>" +
                                                "</div>");

                                // Contact us
                                out.println("<div id=\"contactContainer\">" +
                                                "<div class=\"contactH1Section centerH1\">"
                                                + "<p id=\"contactH1\">Contact us</p>" + //
                                                "<p>HP: 126-969-3690</p>"
                                                +
                                                "<p>Email: HuatBrothers@huat.com</p>"
                                                +
                                                "</div>" +
                                                "</div>");
                        } else {
                                message = "Incorrect username or password";
                                out.println("<script type=\"text/javascript\">");
                                out.println("Swal.fire({\r\n" + //
                                                "  title: \"Error\",\r\n" + //
                                                "  text: \"" + message + "\",\r\n" + //
                                                "  allowOutsideClick: false,\r\n" + //
                                                "  allowEscapeKey: false,\r\n" + //
                                                "  allowEnterKey: false,\r\n" + //
                                                "  icon: \"error\",\r\n" + //
                                                "}).then((result) => {\r\n" + //
                                                "  if (result.isConfirmed) {\r\n" + //
                                                " location = 'index.html';" +
                                                "  }\r\n" + //
                                                "});");
                                out.println("</script>");
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