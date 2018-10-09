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

@WebServlet(name = "ClientTransactionHistory", urlPatterns = {"/ClientTransactionHistory"})
public class ClientTransactionHistory extends HttpServlet {
    
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

    try (PrintWriter out = response.getWriter()) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( "jdbc:mysql://localhost/test" , "root", "tatu");
            st = conn.createStatement();
            int id = Integer.parseInt(request.getParameter("iban"));  
            rs = st.executeQuery("Select * from card where iban = " + id + ";");
            
            while(rs.next()){
                if ( rs.getInt("user") != Integer.parseInt(id4) ) {
                    out.println("Acest card nu este asociat contului dumneavoastra!"); 
                    rs.close();
                    st.close();
                    conn.close();
                    out.close();
                }
            }
            
            String pin = request.getParameter("pin");
            rs = st.executeQuery("Select pin from card where iban = " + id + ";");
            
            while(rs.next()){
                
                //MD5 md5 = new MD5();
                //String ppin= md5.encryptPassword(pin);
                //if ( rs.getString("pin").equals(ppin) ){

                if ( !pin.equals(rs.getString("pin")) ) {
                    out.println("Pin incorect!"); 
                    rs.close();
                    st.close();
                    conn.close();
                    out.close();
                }
            }
            
            String uid=null;
            
            
            rs = st.executeQuery("Select * from tranzactii where iban1 = " + id + " or iban2 = " + id + ";");

           while(rs.next()){
                int tid, tiban1,tiban2,tsuma;

                tid = rs.getInt("id");
                tiban1 = rs.getInt("iban1");
                tiban2 = rs.getInt("iban2");
                tsuma = rs.getInt("suma");

                out.println( tid +". De pe cardul " + tiban1 + " s-au transferat " + tsuma + " pe cardul " + tiban2  );
            }
            
            //response.sendRedirect("IstoricTranzactiiAdmin.html");
        } catch (Exception e) {
            out.println("Nu s-a reusit transferul de bani!"); 
        } finally {
            rs.close();
            st.close();
            conn.close();
            out.close();
        }
    }catch( Exception e) {} 
    }
    
}
