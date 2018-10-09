package Servleti;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //    response.sendRedirect("MainAdmin.html");
        
        Connection conn = null;
        Statement st = null; 
        ResultSet rs = null;
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        
        System.out.println( user ); 
        try {
           
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( "jdbc:mysql://localhost/test" , "root", "tatu");
            st = conn.createStatement();

            rs = st.executeQuery("Select * from user where nume like '" + user + "';");
            int id;
            String userType=null;
            String tid=null;
            
            while(rs.next()){
                //MD5 md5 = new MD5();
                //String ppin= md5.encryptPassword(pin);
                //if ( rs.getString("pin").equals(ppin) ){

                String update = rs.getString("password");
                if ( !update.equals(pass) ) {
                    rs.close();
                    st.close();
                    conn.close();
                    response.sendRedirect("LoginPage.html");
                }
                userType = rs.getString("tip");
                tid = rs.getString("id");
            }
            
            Cookie ck1 = new Cookie( "user", user); response.addCookie(ck1);
            Cookie ck2 = new Cookie( "pass", pass); response.addCookie(ck2);
            Cookie ck3 = new Cookie( "type", userType); response.addCookie(ck3);
            Cookie ck4 = new Cookie( "id", tid); response.addCookie(ck4);
            
            if ( userType.equals("user") )
                response.sendRedirect("ClientMyAccount.html");
            if ( userType.equals("admin") )
                response.sendRedirect("AdminMyAccount.html");
            rs.close();
            st.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("eroare");
        }
        

    }

 
}
