package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.EmployeeAccount;

import java.util.List;

public interface EmployeeAccountRepository extends CrudRepository<EmployeeAccount, Integer> {
    // Find employee account by staff id
    public EmployeeAccount findEmployeeAccountByStaffId(Integer staffId);

    // Find employee account by email
    public EmployeeAccount findEmployeeAccountByEmail(String email);

    // Find all employees filtered by their active status
    public List<EmployeeAccount> findEmployeesByIsActive(Boolean isActive);

    // Find all employees
    @Override
    public List<EmployeeAccount> findAll();
}
