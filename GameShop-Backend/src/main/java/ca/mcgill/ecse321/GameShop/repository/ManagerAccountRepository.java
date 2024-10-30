package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.ManagerAccount;

public interface ManagerAccountRepository extends CrudRepository<ManagerAccount, Integer> {
    // Find manager account by staff id
    public ManagerAccount findManagerAccountByStaffId(Integer staffId);
}
