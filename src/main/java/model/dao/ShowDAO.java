package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import jdbc.ConnectionFactory;
import model.beans.Show;
import model.beans.ShowBanda;
import model.beans.ShowsLocal;

public class ShowDAO {
	private Connection connection;

	public ShowDAO() {
		this.connection = new ConnectionFactory().getConnection();
	}
	
	public void adicionarShow(Show show, int bandasIds[]) {
		ShowBanda sb = new ShowBanda();
		ShowsLocal sl = new ShowsLocal();
		
		ShowsBandaDAO dao = new ShowsBandaDAO();
		ShowsLocalDAO slDao = new ShowsLocalDAO();
		
		String sql = "INSERT INTO shows(local_id,data) VALUES(?,?)";
		
		try {
			PreparedStatement stmt = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

			stmt.setInt(1, show.getIdLocal());
			stmt.setDate(2, new Date(
                    show.getData().getTimeInMillis()));
			
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();

			if (rs.next()) {
				sb.setId_show(rs.getInt(1));
				sl.setShow_Id(rs.getInt(1));
			}
			
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		if(bandasIds != null) {
			for(int i=0; i< bandasIds.length; i++) {
				sb.setId_banda(bandasIds[i]);
				dao.adicionar(sb);
			}
		}
		
		sl.setLocal_Id(show.getIdLocal());
		slDao.adicionar(sl);
		
	}
	
	public void alterarShow(Show show, int[] bandasIds) {
        String sql = "UPDATE shows SET data=?, local_id=? WHERE id_show=?";
		ShowBanda sb = new ShowBanda();
		ShowsBandaDAO dao = new ShowsBandaDAO();
		
        try {        	
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			stmt.setDate(1, new Date(
                    show.getData().getTimeInMillis()));
			stmt.setInt(2, show.getIdLocal());
			stmt.setInt(3, show.getIdShow());

			stmt.executeUpdate();
			stmt.close();
			
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        
        dao.deletarBandaPorShow(show.getIdShow());
        
        if(bandasIds != null) {
			for(int i=0; i< bandasIds.length; i++) {
				sb.setId_banda(bandasIds[i]);
				sb.setId_show(show.getIdShow());
				dao.adicionar(sb);
			}
		}
        
	}
	
	public ArrayList<Show> listarShows() {
		String sql = "select * from shows order by id_show";
		ShowsBandaDAO sb = new ShowsBandaDAO();

        try {
        	ArrayList<Show> shows = new ArrayList<Show>();

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
            	Show show = new Show();
            	
            	show.setIdLocal(rs.getInt("local_id"));
            	show.setIdShow(rs.getInt("id_show"));
            	
            	  Calendar data = Calendar.getInstance();
                  data.setTime(rs.getDate("data"));
                  show.setData(data);
                		
                  int numBandas = 0;
                  numBandas = sb.countBandaPorShow(show.getIdShow());
                  
                  show.setNumBandas(numBandas);                  

                shows.add(show);
            }
            
            rs.close();
            stmt.close();
            
            return shows;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
	
	public Show selecionarShow(Show show) {
        String sql = "select * from shows where id_show=?";
		ShowsBandaDAO sb = new ShowsBandaDAO();
        
        try {        	
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, show.getIdShow());
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
            	
				show.setIdShow(rs.getInt("id_show"));
            	show.setIdLocal(rs.getInt("local_id"));

          	  Calendar data = Calendar.getInstance();
                data.setTime(rs.getDate("data"));
                show.setData(data);
                
                int numBandas = 0;
                numBandas = sb.countBandaPorShow(show.getIdShow());
                
                show.setNumBandas(numBandas);

			}
			
			rs.close();
            stmt.close();
            
			return show;
			
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        
	}
	
	
	public void deletarShow(Show show) {
        String sql = "DELETE FROM shows where id_show=?";
    	ShowsBandaDAO sb = new ShowsBandaDAO();
    	ShowsLocalDAO sl = new ShowsLocalDAO();
    	
        try {        	
			PreparedStatement stmt = connection.prepareStatement(sql);
			
			int qtdShows = 0;
			qtdShows = sb.countShowPorBanda(show.getIdShow());
			
			if (qtdShows>0) {
				sb.deletarShowPorBanda(show.getIdShow());
			}
			
			int qtdLocais=0;
			qtdLocais = sl.countLocaisPorShow(show.getIdShow());
			
			if (qtdLocais>0) {
				sl.deletarLocalPorShow(show.getIdShow());
			}
			
			stmt.setInt(1, show.getIdShow());
			
			stmt.executeUpdate();
			stmt.close();
			
        }catch(SQLException e) {
            throw new RuntimeException(e);
        }
        
	}
}
