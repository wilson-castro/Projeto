package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.beans.Local;
import model.beans.Show;
import model.dao.LocalDAO;
import model.dao.ShowDAO;


@WebServlet(urlPatterns = { "/ControllerShows", "/shows","/shows/delete","/shows/insert",
		"/shows/select","/shows/update" })
public class ControllerShows extends HttpServlet {
	private static final long serialVersionUID = 1L;
    ShowDAO dao = new ShowDAO();

    public ControllerShows() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();

		if (action.equals("/shows")) {
			shows(request, response);
		}else if (action.equals("/shows/insert")) {
			novoShow(request, response);
		}else if(action.equals("/shows/delete")) {
			removerShow(request, response);
		}else if(action.equals("/shows/update")) {
			editarShow(request, response);
		}else {
			response.sendRedirect("index.jsp");
		}
	}
	
	protected void shows(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RequestDispatcher rd = request.getRequestDispatcher("/ListarShows");
		rd.forward(request, response);

	}
	
	protected void novoShow(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Show show = new Show();

		String data =  request.getParameter("data");
		int idLocal = Integer.parseInt(request.getParameter("selectLocais"));
		
		
		show.setIdLocal(idLocal);
		show.setData(data);
		
		if (request.getParameterValues("List_BandaIDs") == null ) {
			dao.adicionarShow(show, null);
						
		}else {
			String[] checkboxIdsList = request.getParameterValues("List_BandaIDs");
			int size = checkboxIdsList.length;
						
			int[] idsList = new int[size];
			
			for (int i = 0; i < size; i++) {
			    idsList[i] = Integer.parseInt(checkboxIdsList[i]);
			}
			dao.adicionarShow(show, idsList);
		}
		
		response.sendRedirect("/projeto/shows");
	}
	protected void editarShow(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int idShow = Integer.parseInt(request.getParameter("idShow"));
		Show show = new Show();
		
		show.setIdShow(idShow);
		
		String data =  request.getParameter("data");
		int idLocal = Integer.parseInt(request.getParameter("selectLocais"));
				
		show.setIdLocal(idLocal);
		show.setData(data);

		if (request.getParameterValues("List_BandaIDs") == null ) {
			dao.alterarShow(show, null);
						
		}else {
			String[] checkboxIdsList = request.getParameterValues("List_BandaIDs");
			int size = checkboxIdsList.length;
						
			int[] idsList = new int[size];
			
			for (int i = 0; i < size; i++) {
			    idsList[i] = Integer.parseInt(checkboxIdsList[i]);
			}
			dao.alterarShow(show, idsList);
		}
		
		response.sendRedirect("/projeto/shows");
	}
	
	protected void removerShow(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int idShow = Integer.parseInt(request.getParameter("idShow"));
		Show show = new Show();

		show.setIdShow(idShow);
		
		dao.deletarShow(show);
			
		response.sendRedirect("/projeto/shows");

	}

}