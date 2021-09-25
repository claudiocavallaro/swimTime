package com.time.swimtime.persistence;

import com.time.swimtime.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    private static final UserDAO INSTANCE = new UserDAO();

    private static final String SELECT_NOME = "select * from db.userdb where nome = ?;";

    private static final String INSERT = "insert into db.userdb(nome, anno, sesso, societa, codice) " +
            " values ( ?, ?, ?, ?, ?);";

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        return INSTANCE;
    }

    public boolean insert(String nome, String anno, String sesso, String societa, String codice) {
        boolean flag = true;
        try (final Connection conn = DBManager.createConnection();
             final PreparedStatement statement = conn.prepareStatement(INSERT);) {

            statement.setString(1, nome);
            statement.setString(2, anno);
            statement.setString(3, sesso);
            statement.setString(4, societa);
            statement.setString(5, codice);
            statement.execute();

        }catch(SQLException s) {
            logger.info("Utente già inserito");
            flag = false;
        }
        return flag;
    }

    public List<User> get(String nome) {
        final List<User> utenti = new ArrayList<>();
        try (final Connection conn = DBManager.createConnection();
             final PreparedStatement statement = conn.prepareStatement(SELECT_NOME);) {

            statement.setString(1, nome);
            statement.execute();
            ResultSet result = statement.getResultSet();

            while (result.next()) {
                final User user = new User();
                user.setId(result.getLong("id"));
                user.setNome(result.getString("nome"));
                user.setAnno(result.getString("anno"));
                user.setSesso(result.getString("sesso"));
                user.setSocieta(result.getString("societa"));
                user.setCodice(result.getString("codice"));
                utenti.add(user);

            }
        } catch (SQLException s) {
            s.printStackTrace();
        }

        return utenti;
    }
}
