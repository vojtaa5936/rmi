package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import server.db.Db;
import shared.Skupina;
import shared.Trida;

public class Tridy extends UnicastRemoteObject implements shared.Tridy {

    protected Tridy() throws RemoteException {
        super();
    }

    

    @Override
    public List<Trida> getTridy() throws RemoteException {
        List<Trida> tridy = new ArrayList<>();

        try (Connection conn = Db.get().getConnection();
                Statement tridyStmt = conn.createStatement();
                ResultSet tridyRs = tridyStmt
                        .executeQuery("SELECT id, CONCAT_WS('.', rocnik, oznaceni) AS oznaceni FROM tridy");
                PreparedStatement skupinyStmt = conn
                        .prepareStatement("SELECT id, oznaceni FROM skupiny WHERE trida = ?");) {
            while (tridyRs.next()) {
                System.out.println(tridyRs.getInt("id"));               
                Trida trida = new Trida().setId(tridyRs.getInt("id")).setOznaceni(tridyRs.getString("oznaceni"));
                System.out.println(trida.getOznaceni());
                skupinyStmt.setInt(1, trida.getId());
                try (ResultSet skupinyRs = skupinyStmt.executeQuery()){
                    while (skupinyRs.next()) {
                        trida.setSkupina(new Skupina(skupinyRs.getString("oznaceni")).setId(skupinyRs.getInt("id")));
                        System.out.println(skupinyRs.getString("oznaceni"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                tridy.add(trida);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return tridy;
    }

    @Override
    public Trida getTrida(int id) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean writeTrida(Trida trida) throws RemoteException {
        try (Connection conn = Db.get().getConnection()) {
            conn.setAutoCommit(false); // zacatek transakce

            // priprava dotazu s pozadavkem na poskytnuti doplnenych automaticky
            // generovanych klicu
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO tridy (rocnik, oznaceni) values (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, Integer.parseInt(trida.getOznaceni().substring(0,1)));
                stmt.setString(2, trida.getOznaceni().substring(2));
                System.out.println(trida.getOznaceni().substring(0, 1));
                System.out.println(trida.getOznaceni().substring(2));
                if (stmt.executeUpdate() != 1) {
                    throw new Exception("Nepodaril se zapis tridy");
                }

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    trida.setId(rs.getInt(1)); // ziskani vygenerovaneho id
                    System.out.println("ziskane id: " + trida.getId());
                }
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
                conn.rollback();
                throw e;
            }

            try (java.sql.PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO skupiny (trida, oznaceni) values (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                for (Iterator<Skupina> it = trida.skupiny(); it.hasNext();) {
                    Skupina sk = it.next();

                    System.out.println("id tridy"+trida.getId());
                    stmt.setInt(1, trida.getId());
                    System.out.println(sk.getOznaceni());
                    stmt.setString(2, sk.getOznaceni());
                    if (stmt.executeUpdate() == 1) {
                        ResultSet rs = stmt.getGeneratedKeys();
                        if (rs.next()) {
                            sk.setId(rs.getInt(1));
                        }
                        rs.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback(); // zamutnuti trannsakce, odvolani zmen
                throw e;
            }

            conn.commit(); // potvrzeni transakce
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
