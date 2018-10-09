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

@WebServlet(name = "ClientATM", urlPatterns = {"/ClientATM"})
public class ClientATM extends HttpServlet {

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
    try (PrintWriter out = response.getWriter()) {
        Connection conn = null;
        Statement st = null; 
        ResultSet rs = null;
        
        Cookie[] ck = request.getCookies();
        String id1 = ck[0].getValue();
        String id2 = ck[1].getValue();
        String id3 = ck[2].getValue();
        String id = ck[3].getValue();
            
        Cookie ck1 = new Cookie( "user", id1); response.addCookie(ck1);
        Cookie ck2 = new Cookie( "pass", id2); response.addCookie(ck2);
        Cookie ck3 = new Cookie( "type", id3); response.addCookie(ck3);
        Cookie ck4 = new Cookie( "id", id); response.addCookie(ck4);
        
        int iban = Integer.parseInt(request.getParameter("iban"));  
        int numerar = Integer.parseInt(request.getParameter("numerar"));  
        String pin = request.getParameter("pin");  
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( "jdbc:mysql://localhost/test" , "root", "tatu");
            st = conn.createStatement();

            rs = st.executeQuery("Select * from card where iban = " + iban + ";");
            String pass=null;
            while(rs.next()){
                
                if( !id.equals(rs.getString("user")) ) {
                    out.println("Cardul NU apartine contului");
                    rs.close();
                    st.close();
                    conn.close();
                    out.close();
                }
                
                pass = rs.getString("pin");
                int update = rs.getInt("numerar");//nr bani din cont
                numerar = update-numerar;
                
                if ( numerar<0 ) {
                    out.println("Nu se poate face tranzactia");
                    rs.close();
                    st.close();
                    conn.close();
                    out.close();
                }
            }
            //MD5 md5 = new MD5();
            //String ppass = md5.encryptPassword(request.getParameter("pass"));
            
             //if ( ppass.equals(pin) ){
            if ( Integer.parseInt(pass) == Integer.parseInt(pin) ) {
                st.execute("update card set numerar = "+ numerar +" where iban = "+iban+";");
                out.println("S-a reusit tranzactia!");
                //response.sendRedirect("ClientATM.html");
            } else {
                out.println("Pin sau iban incorect!");
            }
            
        } catch (Exception e) {
            out.println("Nu s-a reusit adaugarea de bani in contul cerut!");
        }finally {
            rs.close();
            st.close();
            conn.close();
            out.close();
        }
    }catch( Exception e) {}
    }

   
}