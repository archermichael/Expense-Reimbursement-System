package dao;

import models.Reimbursement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReimbursementDaoImplTest {
    ReimbursementDao reimbursementDao;

    @BeforeEach
    void setUp() {
        reimbursementDao = Mockito.mock(ReimbursementDaoImpl.class);
    }

    @Test
    void getAllReimbursements() {
        List<Reimbursement> expectedResult = new ArrayList<>();

        Mockito.when(reimbursementDao.getAllReimbursements()).thenReturn(new ArrayList<>());
        List<Reimbursement> actualResult = reimbursementDao.getAllReimbursements();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getAllReimbursementsGivenId() {
        Integer id = 13;

        List<Reimbursement> expectedResult = new ArrayList<>();

        Mockito.when(reimbursementDao.getAllReimbursementsGivenId(id)).thenReturn(new ArrayList<>());
        List<Reimbursement> actualResult = reimbursementDao.getAllReimbursementsGivenId(id);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void createReimbursement() {
        Reimbursement reimbursement = new Reimbursement();

        Boolean expectedResult = true;

        Mockito.when(reimbursementDao.createReimbursement(reimbursement)).thenReturn(true);

        Boolean actualResult = reimbursementDao.createReimbursement(reimbursement);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void updateReimbursementStatus() {
        Reimbursement reimbursement = new Reimbursement();

        Boolean expectedResult = true;

        Mockito.when(reimbursementDao.updateReimbursementStatus(reimbursement)).thenReturn(true);

        Boolean actualResult = reimbursementDao.updateReimbursementStatus(reimbursement);

        assertEquals(expectedResult, actualResult);
    }
}