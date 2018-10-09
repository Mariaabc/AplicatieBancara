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


@WebServlet(name = "ClientMoneyTransfer", urlPatterns = {"/ClientMoneyTransfer"})
public class ClientMoneyTransfer extends HttpServlet {

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
 
            int iban1 = Integer.parseInt(request.getParameter("iban1"));  
            int iban2 = Integer.parseInt(request.getParameter("iban2"));
            int numerar = Integer.parseInt(request.getParameter("numerar"));
            String pin = request.getParameter("pin");
            
            Cookie[] ck = request.getCookies();
            String id1 = ck[0].getValue();
            String id2 = ck[1].getValue();
            String id3 = ck[2].getValue();
            String id = ck[3].getValue();
            
            Cookie ck1 = new Cookie( "user", id1); response.addCookie(ck1);
            Cookie ck2 = new Cookie( "pass", id2); response.addCookie(ck2);
            Cookie ck3 = new Cookie( "type", id3); response.addCookie(ck3);
            Cookie ck4 = new Cookie( "id", id); response.addCookie(ck4);
            
            
            int update = 0;
       
            rs = st.executeQuery("Select * from card where iban = " + iban1 + ";");
   
            while(rs.next()){
                System.out.println("ad"+rs.getInt("user")); 
                update =rs.getInt("numerar");
                update-=numerar;
                
                
                
                if ( Integer.parseInt(id) != rs.getInt("user") ){
                    rs.close();
                    st.close();
                    conn.close();
                    out.println("Acest card nu e asociat cu contul dumneavoastra!"); 
                }
            
                String tupdate = rs.getString("pin");
                //MD5 md5 = new MD5();
                //String ppin = md5.encryptPassword(pin);
            
                 //if ( tupdate.equals(ppin) ){
                if ( !tupdate.equals(pin) ) {
                    rs.close();
                    st.close();
                    conn.close();
                    out.println("Pin gresit!"); 
                }
            }
                st.execute("update card set numerar = "+ update +" where iban = "+iban1+";");

                rs = st.executeQuery("Select numerar from card where iban = " + iban2 + ";");
                while(rs.next()){
                    update = rs.getInt("numerar");
                    update+=numerar;
                }
            st.execute("update card set numerar = "+ update +" where iban = "+iban2+";");
            st.execute("insert into tranzactii (iban1,iban2,suma) values('"+ iban1 +"','"+ iban2 +"','"+ numerar +"');");

            rs.close();
            st.close();
            conn.close();
            out.println("S-a reusit transferul de bani!"); 
            //response.sendRedirect("ClientMoneyTransfer.html");
        } catch (Exception e) {
            out.println("Nu s-a reusit transferul de bani!"); 
        } //finally {out.close();}
    }catch( Exception e) {} 
    }
}