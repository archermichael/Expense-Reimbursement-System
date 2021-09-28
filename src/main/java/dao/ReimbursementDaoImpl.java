package dao;

import models.Reimbursement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDaoImpl implements ReimbursementDao {
    private static ReimbursementDao reimbursementDao;

    private ReimbursementDaoImpl(){
        try{
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static ReimbursementDao getInstance(){
        if (reimbursementDao == null){
            reimbursementDao = new ReimbursementDaoImpl();
        }
        return reimbursementDao;
    }

    @Override
    public List<Reimbursement> getAllReimbursements() {
        List<Reimbursement> reimbursements = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(ConnectionUtil.url, ConnectionUtil.username, ConnectionUtil.password)){
            String sql = "SELECT reimb_id, reimb_amount, reimb_submitted, \n" +
                    "\t   reimb_resolved, reimb_description, reimb_receipt,\n" +
                    "\t   reimb_author,\n" +
                    "\t   author_fname, author_lname,\n" +
                    "\t   reimb_resolver,\n" +
                    "\t   eu2.user_first_name AS resolver_fname,\n" +
                    "\t   eu2.user_last_name AS resolver_lname,\n" +
                    "\t   reimb_status_id, reimb_type_id\n" +
                    "FROM ers_users eu2 RIGHT JOIN (\n" +
                    "\tSELECT er.reimb_id, er.reimb_amount, er.reimb_submitted, \n" +
                    "\t\t   er.reimb_resolved, er.reimb_description, er.reimb_receipt,\n" +
                    "\t\t er.reimb_author,\n" +
                    "\t\t   eu.user_first_name AS author_fname, eu.user_last_name AS author_lname,\n" +
                    "\t\t   er.reimb_resolver, er.reimb_status_id, er.reimb_type_id\n" +
                    "\tFROM ers_reimbursement er, ers_users eu \n" +
                    "\tWHERE er.reimb_author = eu.ers_users_id\n" +
                    ") reimb ON eu2.ers_users_id = reimb.reimb_resolver;";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                reimbursements.add(new Reimbursement(rs.getInt(1), rs.getDouble(2), rs.getTimestamp(3), rs.getTimestamp(4), rs.getString(5), rs.getBytes(6), rs.getInt(7), rs.getString(8), rs.getString(9), rs.getInt(10), rs.getString(11), rs.getString(12), rs.getInt(13), rs.getInt(14)));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return reimbursements;
    }

    @Override
    public List<Reimbursement> getAllReimbursementsGivenId(Integer id) {
        List<Reimbursement> reimbursements = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(ConnectionUtil.url, ConnectionUtil.username, ConnectionUtil.password)){
            String sql = "SELECT reimb_id, reimb_amount, reimb_submitted, \n" +
                    "\t   reimb_resolved, reimb_description, reimb_receipt,\n" +
                    "\t   reimb_author,\n" +
                    "\t   author_fname, author_lname,\n" +
                    "\t   reimb_resolver,\n" +
                    "\t   eu2.user_first_name AS resolver_fname,\n" +
                    "\t   eu2.user_last_name AS resolver_lname,\n" +
                    "\t   reimb_status_id, reimb_type_id\n" +
                    "FROM ers_users eu2 RIGHT JOIN (\n" +
                    "\tSELECT er.reimb_id, er.reimb_amount, er.reimb_submitted, \n" +
                    "\t\t   er.reimb_resolved, er.reimb_description, er.reimb_receipt,\n" +
                    "\t\t   er.reimb_author,\n" +
                    "\t\t   eu.user_first_name AS author_fname, eu.user_last_name AS author_lname,\n" +
                    "\t\t   er.reimb_resolver, er.reimb_status_id, er.reimb_type_id\n" +
                    "\tFROM ers_reimbursement er, ers_users eu \n" +
                    "\tWHERE er.reimb_author = eu.ers_users_id AND er.reimb_author = ?\n" +
                    ") reimb ON eu2.ers_users_id = reimb.reimb_resolver;";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                reimbursements.add(new Reimbursement(rs.getInt(1), rs.getDouble(2), rs.getTimestamp(3), rs.getTimestamp(4), rs.getString(5), rs.getBytes(6), rs.getInt(7), rs.getString(8), rs.getString(9), rs.getInt(10), rs.getString(11), rs.getString(12), rs.getInt(13), rs.getInt(14)));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return reimbursements;
    }

    @Override
    public Boolean createReimbursement(Reimbursement reimbursement) {
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.url, ConnectionUtil.username, ConnectionUtil.password)){
            String sql = "INSERT INTO ers_reimbursement VALUES (DEFAULT, ?, DEFAULT, NULL, ?, NULL, ?, NULL, 2, ?);";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDouble(1, reimbursement.getAmount());
            ps.setString(2, reimbursement.getDescription());
            //ps.setBytes(3, reimbursement.getReceipt());
            ps.setInt(3, reimbursement.getAuthorId());
            ps.setInt(4, reimbursement.getTypeId());

            if (ps.executeUpdate() != 0){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean updateReimbursementStatus(Reimbursement reimbursement) {
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.url, ConnectionUtil.username, ConnectionUtil.password)){
            String sql = "UPDATE ers_reimbursement SET reimb_status_id = ?, reimb_resolver = ?, reimb_resolved = CURRENT_TIMESTAMP WHERE reimb_id = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, reimbursement.getStatusId());
            ps.setInt(2, reimbursement.getResolverId());
            ps.setInt(3, reimbursement.getId());

            if (ps.executeUpdate() != 0){
                return true;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
