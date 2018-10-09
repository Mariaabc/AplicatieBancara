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

@WebServlet(name = "AdaugaBani", urlPatterns = {"/AdaugaBani"})
public class AdaugaBani extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = null;
        Connection conn = null;
        Statement st = null; 
        ResultSet rs = null;
    try {
        out = response.getWriter();
        int iban = Integer.parseInt(request.getParameter("iban"));  
        int numerar = Integer.parseInt(request.getParameter("numerar"));  
        System.out.println("da");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( "jdbc:mysql://localhost/test" , "root", "tatu");
            st = conn.createStatement();

            rs = st.executeQuery("Select numerar from card where iban = " + iban + ";");

            while(rs.next()){
                int update = rs.getInt("numerar");
                numerar+=update;
            }
            st.execute("update card set numerar = "+ numerar +" where iban = "+iban+";");

            //out.println("alert('S-a reusit adaugarea banilor!')");
            response.sendRedirect("AdminAddMoney.html");
        } catch (Exception e) {
            out.println("Nu s-a reusit adaugarea de bani in contul cerut!");
        } finally {
            rs.close();
            st.close();
            conn.close();
        }
    }catch( Exception e) {}
    finally {
        out.close();
    }
    }

   
}