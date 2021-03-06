
  
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import jdbc.ConnectionFactory;
import model.beans.Local;
import model.beans.Show;
import model.beans.ShowsLocal;

public class LocalDAO {
	private Connection connection;

	public LocalDAO() {
		this.connection = new ConnectionFactory().getConnection();
	}
		
	public void adicionarLocal(Local local,int showsIds[]) {
		ShowsLocal sl = new ShowsLocal();
		ShowsLocalDAO dao = new ShowsLocalDAO();
		
		String sql = "INSERT INTO locais(nome_local,capacidade) VALUES(?,?)";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, local.getNome());
			stmt.setInt(2, local.getCapacidade());
			
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			
			if (rs.next()) {
				sl.setLocal_Id(rs.getInt(1));
			}

			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		if(showsIds != null) {
			for(int i=0; i< showsIds.length; i++) {
				sl.setShow_Id(showsIds[i]);
				dao.adicionar(sl);
			}
		}
	}
	
	public ArrayList<Local> listarLocais() {
		String sql = "select * from locais order by id_local";
		ShowsLocalDAO dao = new ShowsLocalDAO();
		
        try {
        	ArrayList<Local> locais = new ArrayList<Local>();

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

            	Local local = new Local();
            	
            	local.setIdLocal(rs.getInt("id_local"));
            	local.setNome(rs.getString("nome_local"));
            	local.setCapacidade(rs.getInt("capacidade"));
                		
            	int numShows = 0;
            	numShows = dao.countShowPorLocais(local.getIdLocal());
    
                local.setNumShows(numShows);
                		
                locais.add(local);

            }
            
            rs.close();
            stmt.close();
            
            return locais;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
	
	public ArrayList<Local> listarLocaisComFiltro(String nomeLugar) {
		String sql = "select * from locais where nome_local LIKE ? order by id_local";
		ShowsLocalDAO dao = new ShowsLocalDAO();

        try {
        	ArrayList<Local> locais = new ArrayList<Local>();

            PreparedStatement stmt = connection.prepareStatement(sql);
            
            stmt.setString(1, "%"+nomeLugar+"%");
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

            	Local local = new Local();
            	
            	local.setIdLocal(rs.getInt("id_local"));
            	local.setNome(rs.getString("nome_local"));
            	local.setCapacidade(rs.getInt("capacidade"));
                	
            	int numShows = 0;
            	numShows = dao.countShowPorLocais(local.getIdLocal());
    
                local.setNumShows(numShows);
                		
                locais.add(local);

            }
            
            rs.close();
            stmt.close();
            
            return locais;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
	
	public Local selecionarLocal(Local local) {
        String sql = "select * from locais where id_local=?";
		ShowsLocalDAO dao = new ShowsLocalDAO();

        try {        	
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, local.getIdLocal());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
            	
            	local.setIdLocal(rs.getInt("id_local"));
                local.setNome(rs.getString("nome_local"));
                local.setCapacidade(rs.getInt("capacidade"));
                
				int numShows = 0;
        	numShows = dao.countShowPorLocais(local.getIdLocal());

            local.setNumShows(numShows);
            		
                
			}
			
			rs.close();
            stmt.close();
            
			return local;
			
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        
	}
	
	public void alterarLocal(Local local,int showsIds[]) {
		ShowsLocal sl = new ShowsLocal();
		ShowsLocalDAO dao = new ShowsLocalDAO();
        String sql = "UPDATE locais SET nome_local=?, capacidade=? WHERE id_local=?";
        
        try {        	
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, local.getNome());
			stmt.setInt(2, local.getCapacidade());
			stmt.setInt(3, local.getIdLocal());
			
	        dao.deletarShowPorLocal(local.getIdLocal());
			
			stmt.executeUpdate();
			stmt.close();
			
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        
        if(showsIds != null) {
			for(int i=0; i< showsIds.length; i++) {
				sl.setLocal_Id(local.getIdLocal());
				sl.setShow_Id(showsIds[i]);
				dao.adicionar(sl);
			}
		}
        
	}
	
	public void deletarLocal(Local local) {
        String sql = "DELETE FROM locais where id_local=?";
        
        ShowsLocalDAO sl = new ShowsLocalDAO();
        ShowDAO showDao = new ShowDAO();
  
        
        try {        	
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			int qtdShows = 0;
			qtdShows = sl.countShowPorLocais(local.getIdLocal());
				
			if (qtdShows>0) {
				sl.deletarShowPorLocal(local.getIdLocal());
			}
			
			ArrayList<Show> showsDoLocal = showDao.listarShowsPorIdLocal(local.getIdLocal());
			
			for(Show show : showsDoLocal) {
				showDao.deletarShow(show);
			}
			
			stmt.setInt(1, local.getIdLocal());
			
			stmt.executeUpdate();
			stmt.close();
			
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        
	}
	
}