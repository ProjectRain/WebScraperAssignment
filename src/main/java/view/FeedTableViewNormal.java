package view;

import logic.FeedLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.Feed;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author roble
 */
@WebServlet(name = "FeedTable", urlPatterns = {"/FeedTable"})
public class FeedTableViewNormal extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>FeedViewNormal</title>");            
            out.println("</head>");
            out.println("<body>");
            
            FeedLogic logic = new FeedLogic();
            List<Feed> entity = logic.getAll();
            out.println("<table align=\"center\" border=\"1\">");
            out.println("<caption>Feed</caption>");
            //this is an example for your other table use getColumnNames from logic to 
            //create headers in a loop.
            out.println("<tr>");
            for (String columnName : logic.getColumnNames()){
               out.println("<th>"+columnName+"</th>"); 
            }
            out.println("</tr>");
            for (Feed f : entity) {
                //for other tables replace the code bellow and use extractDataAsList
                //in a loop to fill the data.
//                logic.extractDataAsList(e);
                out.println("<tr>");
                out.println("<td>" + f.getId() + "</td>");
                out.println("<td>" + f.getPath() + "</td>");
                out.println("<td>" + f.getType() + "</td>");
                out.println("<td>" + f.getName() + "</td>");
                out.println("<td>" + f.getHostid() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.printf("<div style=\"text-align: center;\"><pre>%s</pre></div>", toStringMap(request.getParameterMap()));
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> m) {
        StringBuilder builder = new StringBuilder();
        for(String k: m.keySet()) {
            builder.append("Key=").append(k)
                    .append(", ")
                    .append("Value/s=").append(Arrays.toString(m.get(k)))
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Sample of Feed View Normal";
    }

    private static final boolean DEBUG = true;

    public void log( String msg) {
        if(DEBUG){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log( message);
        }
    }

    public void log( String msg, Throwable t) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log( message, t);
    }
}
