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

@WebServlet(name = "CreateCard", urlPatterns = {"/CreateCard"})
public class CreateCard extends HttpServlet {

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
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( "jdbc:mysql://localhost/test" , "root", "tatu");
            st = conn.createStatement();  
            
            int id = Integer.parseInt(request.getParameter("id"));
            
            MD5 md5 = new MD5();
            //String pin = md5.encryptPassword(request.getParameter("pin"));
            String pin =request.getParameter("pin");
            int numerar = Integer.parseInt(request.getParameter("numerar"));
            
            st.execute("insert into card (user,pin,numerar,iban) values('"+ id +"','"+ pin +"','"+ numerar +"',0);");
            
            rs = st.executeQuery("Select id from card where iban = 0;");
            
            while(rs.next()){
                int iid = rs.getInt("id");
                st.execute("update card set iban="+iid+" where id="+iid+";");
                break;
            }
            
            rs.close();
            st.close();
            conn.close();

            response.sendRedirect("AdminCreateCard.html");
        } catch (Exception e) {
            System.out.println("Nu s-a reusit adaugarea unui nou cont in baza de date!");
        }finally {
            try {
                st.close();
                conn.close();
            }catch (Exception e) {}
        }
        
    }

}
