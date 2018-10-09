package Servleti;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ClientMyAccount", urlPatterns = {"/ClientMyAccount"})
public class ClientMyAccount extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        
        Cookie[] ck = request.getCookies();
        String id1 = ck[0].getValue();
        String id2 = ck[1].getValue();
        String id3 = ck[2].getValue();
        String id4 = ck[3].getValue();
        
        Cookie ck1 = new Cookie( "user", id1); response.addCookie(ck1);
        Cookie ck2 = new Cookie( "pass", id2); response.addCookie(ck2);
        Cookie ck3 = new Cookie( "type", id3); response.addCookie(ck3);
        Cookie ck4 = new Cookie( "id", id4); response.addCookie(ck4);
        
        try {
            PrintWriter out = response.getWriter();
            Class.forName("com.mysql.jdbc.Driver");
            
            conn = DriverManager.getConnection( "jdbc:mysql://localhost/test" , "root", "tatu");
            st = conn.createStatement();
            int ttt = Integer.parseInt(id4);

            
            String pin = request.getParameter("pin");
            rs = st.executeQuery("Select * from card where user =" + ttt + ";");
            while(rs.next()){ 
                out.println( "Iban: " + rs.getInt("iban") + " Numerar:" + rs.getInt("numerar")); 
            }
            rs.close();
            st.close();
            conn.close();
            out.close();
        } catch (Exception e) {
            System.out.println("Nu s-a reusit accesarea bazei de date!");
        }
        
    }

}
