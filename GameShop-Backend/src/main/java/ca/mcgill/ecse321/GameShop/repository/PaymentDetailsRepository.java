package ca.mcgill.ecse321.GameShop.repository;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.GameShop.model.PaymentDetails;

public interface PaymentDetailsRepository extends CrudRepository<PaymentDetails, Integer> {
    public PaymentDetails findPaymentDetailsByPaymentDetailsId(Integer paymentDetailsId);
}
