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

@WebServlet(name = "AdminTransactionHistory", urlPatterns = {"/AdminTransactionHistory"})
public class AdminTransactionHistory extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
    try (PrintWriter out = response.getWriter()) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( "jdbc:mysql://localhost/test" , "root", "tatu");
            st = conn.createStatement();
            
            int id = Integer.parseInt(request.getParameter("id"));  
            rs = st.executeQuery("Select * from tranzactii where iban1 = " + id + " or iban2 = " + id + ";");
            
            while(rs.next()){
                int tid, tiban1,tiban2,tsuma;
                
                tid = rs.getInt("id");
                tiban1 = rs.getInt("iban1");
                tiban2 = rs.getInt("iban2");
                tsuma = rs.getInt("suma");
                
                out.println( tid +". De pe cardul " + tiban1 + " s-au transferat " + tsuma + " pe cardul " + tiban2 + "<br>" );
            }
            rs.close();
            st.close();
            conn.close();
            out.close();
            //response.sendRedirect("IstoricTranzactiiAdmin.html");
        } catch (Exception e) {
            out.println("Nu s-a reusit transferul de bani!"); 
        } finally {out.close();}
    }catch( Exception e) {} 
    }
    
}
