package Servleti;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AdminDeleteAccount", urlPatterns = {"/AdminDeleteAccount"})
public class AdminDeleteAccount extends HttpServlet {
@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        Statement st = null; 
        
        int id = Integer.parseInt(request.getParameter("id"));  
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( "jdbc:mysql://localhost/test" , "root", "tatu");
            st = conn.createStatement();
            st.execute("delete from user where id = "+ id +";");
            st.execute("delete from card where user = "+ id +";");
            st.close();
            conn.close();
          
            try (PrintWriter out = response.getWriter()) {
                out.println( "S-a reusit stergerea contului cu id-ul " + id + " si a cardurilor asociate acestuia!" ); 
            }catch( Exception e) {}
        } catch (Exception e) {
            System.out.println("Nu s-a reusit stergerea contului cu id-ul " + id + "!");
        }
        
    }

   
}
