package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.beans.Local;
import model.dao.LocalDAO;
import model.dao.ShowDAO;


@WebServlet(urlPatterns = { "/ControllerLocais", "/locais","/locais/delete","/locais/insert",
		"/locais/select","/locais/update" })
public class ControllerLocais extends HttpServlet {
	private static final long serialVersionUID = 1L;
       LocalDAO dao = new LocalDAO();
   
    public ControllerLocais() {
        super();
    }

	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();

		if (action.equals("/locais")) {
			locais(request, response);
		}else if (action.equals("/locais/insert")) {
			novoLocal(request, response);
		}else if(action.equals("/locais/update")) {
			editarLocal(request, response);
		}else if(action.equals("/locais/delete")) {
			removerLocal(request, response);
		}else {
			response.sendRedirect("index.jsp");
		}
	}

	protected void locais(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		RequestDispatcher rd = request.getRequestDispatcher("/ListarLocais");
		rd.forward(request, response);

	}
	
	protected void novoLocal(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Local local = new Local();
		
		String nome =  request.getParameter("nome");
		int capacidade = Integer.parseInt(request.getParameter("capacidade"));
		
		local.setNome(nome);
		local.setCapacidade(capacidade);

		if (request.getParameterValues("List_ShowsIDs") == null ) {
			dao.adicionarLocal(local, null);
						
		}else {
			String[] checkboxIdsList = request.getParameterValues("List_ShowsIDs");
			int size = checkboxIdsList.length;
						
			int[] idsList = new int[size];
			
			for (int i = 0; i < size; i++) {
			    idsList[i] = Integer.parseInt(checkboxIdsList[i]);
			}
			dao.adicionarLocal(local, idsList);
		}
		
		response.sendRedirect("/projeto/locais");
	}
	
	protected void editarLocal(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Local local = new Local();
		
		String nome =  request.getParameter("nome");
		int capacidade = Integer.parseInt(request.getParameter("capacidade"));
		int idLocal = Integer.parseInt(request.getParameter("idLocal"));

		local.setIdLocal(idLocal);
		local.setNome(nome);
		local.setCapacidade(capacidade);
		
		if (request.getParameterValues("List_ShowsIDs") == null ) {
			dao.alterarLocal(local, null);
						
		}else {
			String[] checkboxIdsList = request.getParameterValues("List_ShowsIDs");
			int size = checkboxIdsList.length;
						
			int[] idsList = new int[size];
			
			for (int i = 0; i < size; i++) {
			    idsList[i] = Integer.parseInt(checkboxIdsList[i]);
			}
			dao.alterarLocal(local, idsList);
		}
		
		response.sendRedirect("/projeto/locais");
	}
	
	protected void removerLocal(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int idLocal = Integer.parseInt(request.getParameter("idLocal"));
		
		Local local = new Local();
		local.setIdLocal(idLocal);
		
		dao.deletarLocal(local);
			
		response.sendRedirect("/projeto/locais");

	}
 
}