package services;

import dao.ReimbursementDao;
import dao.ReimbursementDaoImpl;
import models.Reimbursement;

import java.util.List;

public class ReimbursementService {
    ReimbursementDao reimbursementDao;

    public ReimbursementService(){
        reimbursementDao = ReimbursementDaoImpl.getInstance();
    }

    public List<Reimbursement> getAllReimbursements() {
        return reimbursementDao.getAllReimbursements();
    }

    public List<Reimbursement> getAllReimbursementsGivenId(Integer id) {
        return reimbursementDao.getAllReimbursementsGivenId(id);
    }

    public Boolean createReimbursement(Reimbursement reimbursement){
        if (reimbursementDao.createReimbursement(reimbursement))
            return true;
        return false;
    }

    public Boolean updateReimbursementStatus(Reimbursement reimbursement){
        if (reimbursementDao.updateReimbursementStatus(reimbursement))
            return true;
        return false;
    }
}
