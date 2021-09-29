package com.time.swimtime.persistence;

import com.time.swimtime.model.Gara;
import com.time.swimtime.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GareDAO {

    private static final Logger logger = LoggerFactory.getLogger(GareDAO.class);

    private static final GareDAO INSTANCE = new GareDAO();

    private static final String SELECT_BY_ID = "select * from db.garedb where iduser = ?";

    private static final String INSERT = "insert into db.garedb(data, tipo, tempo, vasca, federazione, categoria, iduser, time) " +
            " values(?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String INSERT_ASSOCIATIVA = "insert into db.gareuser(idgara, iduser, data) values (?, ?, ?);";

    private static final String SELECT_LAST_DATE = "select data\n" +
            "from db.gareuser\n" +
            "where iduser = ? " +
            "order by data desc \n" +
            "limit 1";

    private GareDAO() {
    }

    public static GareDAO getInstance() {
        return INSTANCE;
    }

    public boolean insert(String data, String tipo, String tempo, String vasca, String federazione, String categoria, Long iduser, Long time){
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
            statement.setLong(8, time);
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
            statement.setDate(3, new Date(System.currentTimeMillis()));
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
                gara.setTime(result.getLong("time"));
                gare.add(gara);
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return gare;
    }

    public Date getLastDate(Long idUser){
        Date date = null;
        try (final Connection conn = DBManager.createConnection();
             final PreparedStatement statement = conn.prepareStatement(SELECT_LAST_DATE);) {

            statement.setLong(1, idUser);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                date = resultSet.getDate("data");
            }

        }catch(SQLException s) {
            logger.info("Errore tabella associativa");
        }
        return date;
    }

}
