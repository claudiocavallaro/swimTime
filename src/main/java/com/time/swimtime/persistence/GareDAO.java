package com.time.swimtime.persistence;

import com.time.swimtime.model.Gara;
import com.time.swimtime.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GareDAO {

    private static final Logger logger = LoggerFactory.getLogger(GareDAO.class);

    private static final GareDAO INSTANCE = new GareDAO();

    private static final String SELECT_BY_ID = "select * from db.garedb where iduser = ?";

    private static final String INSERT = "insert into db.garedb(data, tipo, tempo, vasca, federazione, categoria, iduser) " +
            " values(?, ?, ?, ?, ?, ?, ?);";

    private static final String INSERT_ASSOCIATIVA = "insert into db.gareuser(idgara, iduser) values (?, ?);";

    private GareDAO() {
    }

    public static GareDAO getInstance() {
        return INSTANCE;
    }

    public boolean insert(String data, String tipo, String tempo, String vasca, String federazione, String categoria, Long iduser){
        boolean flag = true;
        try (final Connection conn = DBManager.createConnection();
             final PreparedStatement statement = conn.prepareStatement(INSERT);) {

            statement.setString(1, data);
            statement.setString(2, tipo);
            statement.setString(3, tempo);
            statement.setString(4, vasca);
            statement.setString(5, federazione);
            statement.setString(6, categoria);
            statement.setLong(7, iduser);
            statement.execute();

        }catch(SQLException s) {
            logger.info("Gara già inserita");
            flag = false;
        }
        return flag;
    }

    public boolean insertAssociativa(Long idGara, Long idUser){
        boolean flag = true;
        try (final Connection conn = DBManager.createConnection();
             final PreparedStatement statement = conn.prepareStatement(INSERT_ASSOCIATIVA);) {

            statement.setLong(1, idGara);
            statement.setLong(2, idUser);
            statement.execute();

        }catch(SQLException s) {
            logger.info("Coppia già inserita");
            flag = false;
        }
        return flag;
    }

    public List<Gara> get(String idUser){
        final List<Gara> gare = new ArrayList<>();
        try (final Connection conn = DBManager.createConnection();
             final PreparedStatement statement = conn.prepareStatement(SELECT_BY_ID);) {

            statement.setLong(1, Long.parseLong(idUser));
            statement.execute();
            ResultSet result = statement.getResultSet();

            while (result.next()) {
                final Gara gara = new Gara();
                gara.setId(result.getLong("id"));
                gara.setData(result.getString("data"));
                gara.setTipo(result.getString("tipo"));
                gara.setTempo(result.getString("tempo"));
                gara.setVasca(result.getString("vasca"));
                gara.setFederazione(result.getString("federazione"));
                gara.setCategoria(result.getString("categoria"));
                gara.setUserId(result.getLong("iduser"));
                gare.add(gara);
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return gare;
    }
}
