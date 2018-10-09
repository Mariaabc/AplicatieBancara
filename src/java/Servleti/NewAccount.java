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

@WebServlet(name = "NewAccount", urlPatterns = {"/NewAccount"})
public class NewAccount extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( "jdbc:mysql://localhost/test" , "root", "tatu");
            st = conn.createStatement();
            
            //int id = Integer.parseInt(request.getParameter("id"));  
            String nume = request.getParameter("user");
            MD5 md5 = new MD5();
            //String pass = md5.encryptPassword(request.getParameter("pass"));
            String pass = request.getParameter("pass");
            String tip = request.getParameter("type");
            
            st.execute("insert into user (tip,nume,password) values('"+ tip +"','"+ nume +"','"+ pass +"');");
            
            st.close();
            conn.close();
            
            response.sendRedirect("AdminNewAccount.html");
        } catch (Exception e) {
            System.out.println("Nu s-a reusit adaugarea unui nou cont in baza de date!");
        }
        
    }

   
}
