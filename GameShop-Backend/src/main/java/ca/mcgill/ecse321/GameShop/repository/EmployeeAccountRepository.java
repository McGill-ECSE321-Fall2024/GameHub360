package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;

public interface EmployeeAccountRepository extends CrudRepository<EmployeeAccount, Integer> {
    // Find employee account by staff id
    public EmployeeAccount findEmployeeAccountByStaffId(Integer staffId);

    // Find employee account by email
    public EmployeeAccount findEmployeeAccountByEmail(String email);
}
