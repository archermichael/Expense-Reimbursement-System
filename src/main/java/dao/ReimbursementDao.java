package dao;

import models.Reimbursement;

import java.util.List;

public interface ReimbursementDao {
    List<Reimbursement> getAllReimbursements();
    List<Reimbursement> getAllReimbursementsGivenId(Integer id);
    Boolean createReimbursement(Reimbursement reimbursement);
    Boolean updateReimbursementStatus(Reimbursement reimbursement);
}
