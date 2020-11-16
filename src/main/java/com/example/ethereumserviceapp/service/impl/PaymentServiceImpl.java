package com.example.ethereumserviceapp.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.ethereumserviceapp.model.Case;
import com.example.ethereumserviceapp.model.CasePayment;
import com.example.ethereumserviceapp.model.State;
import com.example.ethereumserviceapp.model.entities.SsiApplication;
import com.example.ethereumserviceapp.service.EthereumService;
import com.example.ethereumserviceapp.service.MongoService;
import com.example.ethereumserviceapp.service.PaymentService;
import com.example.ethereumserviceapp.utils.EthAppUtils;
import com.example.ethereumserviceapp.utils.MonitorUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private EthereumService ethServ;
    private MongoService mongoServ;

    @Autowired
    public PaymentServiceImpl(EthereumService ethServ, MongoService mongoServ) {
        this.ethServ = ethServ;
        this.mongoServ = mongoServ;
    }
    
    @Override
    @Scheduled(cron = "0 0 0 1 * ?")
    public void startPayment(){
        
        List<String> uuids = this.ethServ.getAllCaseUUID();
        uuids.stream().forEach(uuid -> {
            // get the case from the block chain
            Optional<Case> theCase = this.ethServ.getCaseByUUID(uuid);
            //if the case does not exist or is a case belonging to a non principal member, continue to the next case
            if(!theCase.isPresent() || theCase.get().getState().equals(State.NONPRINCIPAL)){
                return;
            }
            Case caseToBePaid = theCase.get();
            LocalDateTime startDate = caseToBePaid.getHistory().entrySet().iterator().next().getKey();
            LocalDateTime currentDate = LocalDateTime.now();
            BigDecimal paymentValue = BigDecimal.valueOf(0);
            if (caseToBePaid.getState().equals(State.ACCEPTED)) {
                Optional<SsiApplication> ssiApp = mongoServ.findByUuid(uuid);
                if(startDate.isBefore(currentDate)){
                    List<SsiApplication> allHouseholdApps = mongoServ.findByTaxisAfmIn(EthAppUtils.fetchAllHouseholdAfms(ssiApp.get())); 
                    paymentValue = MonitorUtils.calculateCurrentPayment(caseToBePaid, ssiApp.get(), allHouseholdApps).subtract(caseToBePaid.getOffset());
                    //Call to payment service
                    State paymentState = paymentService(paymentValue, caseToBePaid);
                    addPayment(paymentValue, caseToBePaid, currentDate, paymentState);
                }
            }
            //if case is rejected then check the previous month history for days during which the case was accepted
            if (caseToBePaid.getState().equals(State.REJECTED) || caseToBePaid.getState().equals(State.SUSPENDED)) {
                // get the number of days of the previous month during which the case was accepted
                Long acceptedDates= caseToBePaid.getHistory().entrySet().stream().filter(
                        e -> (e.getKey().toLocalDate().compareTo(currentDate.toLocalDate().minusMonths(1)) >= 0) 
                        && e.getKey().toLocalDate().isBefore(currentDate.toLocalDate())
                        && e.getValue().equals(State.ACCEPTED)).count();
                if(acceptedDates.intValue() > 0){
                    Optional<SsiApplication> ssiApp = mongoServ.findByUuid(uuid);
                    //check payment credentials
                    if(ssiApp.isPresent() && !ssiApp.get().getHouseholdPrincipal().getAfm().equals(ssiApp.get().getTaxisAfm())){
                        List<SsiApplication> allHouseholdApps = mongoServ.findByTaxisAfmIn(EthAppUtils.fetchAllHouseholdAfms(ssiApp.get())); 
                        paymentValue = MonitorUtils.calculateCurrentPayment(caseToBePaid, ssiApp.get(), allHouseholdApps).subtract(caseToBePaid.getOffset());
                        //Call to payment service
                        State paymentState = paymentService(paymentValue, caseToBePaid);
                        addPayment(paymentValue, caseToBePaid, currentDate, paymentState);
                    }
                } else if(caseToBePaid.getState().equals(State.REJECTED) ){
                    // if the case's state is rejected and there are no days during the month during which the case was accepted, delete it from the block chain 
                    ethServ.deleteCaseByUuid(uuid);
                }
            }
        });
    }

    private State paymentService(BigDecimal valueToBePaid, Case caseToBePaid){
        //mock Call to external service
        if(!mockExternalPaymentService(valueToBePaid, caseToBePaid.getUuid())){
            caseToBePaid.setOffset(BigDecimal.valueOf(0));
            //caseToBePaid.setState(State.PAID);
            return State.FAILED;
        } 
        caseToBePaid.setOffset(BigDecimal.valueOf(0));
        return State.PAID;
    }

    private Boolean mockExternalPaymentService(BigDecimal valueToBePaid, String uuid){
        return true;
    }

    private void addPayment(BigDecimal valueToBePaid, Case caseToBePaid, LocalDateTime currentDate, State state){
        CasePayment payment = new CasePayment();
        payment.setPaymentDate(currentDate);
        payment.setPayment(valueToBePaid);
        payment.setState(state);
        ethServ.addPayment(caseToBePaid, payment);
        log.info("new payment :{}", payment);
    }
}
