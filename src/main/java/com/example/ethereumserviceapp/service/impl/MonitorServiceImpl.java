/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ethereumserviceapp.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.ethereumserviceapp.model.Case;
import com.example.ethereumserviceapp.model.CasePayment;
import com.example.ethereumserviceapp.model.CredsAndExp;
import com.example.ethereumserviceapp.model.HouseholdMember;
import com.example.ethereumserviceapp.model.State;
import com.example.ethereumserviceapp.model.entities.SsiApplication;
import com.example.ethereumserviceapp.service.EthereumService;
import com.example.ethereumserviceapp.service.MongoService;
import com.example.ethereumserviceapp.service.MonitorService;
import com.example.ethereumserviceapp.utils.DateUtils;
import com.example.ethereumserviceapp.utils.EthAppUtils;
import com.example.ethereumserviceapp.utils.ExportCaseToExcel;
import com.example.ethereumserviceapp.utils.MonitorUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author nikos
 */
@Service
@Slf4j
public class MonitorServiceImpl implements MonitorService {

    private MongoService mongoServ;

    private EthereumService ethServ;

    @Autowired
    public MonitorServiceImpl(MongoService mongoS, EthereumService ethServ) {
        this.mongoServ = mongoS;
        this.ethServ = ethServ;
    }

    MonitorUtils monitorUtils;

    @Override
    @Scheduled(cron = "0 0 12 * * ?")
    public void startScheduledMonitoring() {
        startMonitoring(LocalDateTime.now(), false);
    }

    // parameters:
    // dateNow: the date of the monitoring process
    // uuid: the uuid of the case to be monitored
    // sync: parameter used for testing true for synchronization with the
    // blockchaing, false(default) make asynchronous transactions with the
    // blockchain
    @Override
    public void startMonitoring(LocalDateTime dateNow, Boolean isTest) {
        LocalDateTime currentDate = dateNow == null ? LocalDateTime.now() : dateNow;
        List<String> uuids = this.ethServ.getAllCaseUUID();
        uuids.stream().forEach(uuid -> {

            Optional<Case> theCase = this.ethServ.getCaseByUUID(uuid);
            if (!theCase.isPresent()) {
                log.info("error - case not present");
                return;
            }

            Case monitoredCase = theCase.get();
            // int caseState = c.get().getState().getValue();

            if (monitoredCase.getState().equals(State.NONPRINCIPAL)) {
                log.info("case non principal");
                return;
            }

            log.info("looking into case {} with state {}", uuid, monitoredCase.getState());
            Optional<SsiApplication> ssiCase = mongoServ.findByUuid(uuid);
            if (!ssiCase.isPresent()) {
                log.info("application in database not present");
                updateCase(monitoredCase, State.REJECTED, null, currentDate, isTest, uuid, null);
                return;
            }

            SsiApplication ssiApp = ssiCase.get();

            // if this is not a principal case update state as non principal and continue to
            // the next case
            if (!ssiApp.getTaxisAfm().equals(ssiApp.getHouseholdPrincipal().getAfm())) {
                updateCase(monitoredCase, State.NONPRINCIPAL, null, currentDate, isTest, uuid, null);
                return;
            }

            if (monitoredCase.getState().equals(State.REJECTED)) {
                Iterator<Entry<LocalDateTime, State>> itr = monitoredCase.getHistory().entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry<LocalDateTime, State> entry = itr.next();
                    // if the case is rejected for more than one month then delete it
                    if (entry.getValue().equals(State.REJECTED)
                            && entry.getKey().toLocalDate().isBefore(currentDate.toLocalDate().minusMonths(1))) {
                        // Optional<SsiApplication> ssiCase = mongoServ.findByUuid(uuid);
                        Set<String> householdAfms = ssiApp.getHouseholdComposition().stream().map(s -> s.getAfm())
                                .collect(Collectors.toSet());
                        for (String hhuuid : mongoServ.findUuidByTaxisAfmIn(householdAfms)) {
                            this.mongoServ.deleteByUuid(hhuuid);
                        }
                        break;
                    }
                }
                // return;
            }
            // if the payment has failed for 3 consecutive months delete the case
            if (checkForFailedPayments(monitoredCase)) {
                mongoServ.deleteByUuid(uuid);
                ethServ.deleteCaseByUuid(uuid);
                return;
            }

            Set<String> householdAfms = ssiApp.getHouseholdComposition().stream().map(s -> s.getAfm())
                    .collect(Collectors.toSet());
            List<SsiApplication> householdApps = mongoServ.findByTaxisAfmIn(householdAfms);

            // make external API calls that may update certain values in DB
            if (!externalChecksAndUpdate(monitoredCase, householdApps, ssiApp)) {
                updateCase(monitoredCase, State.REJECTED, ssiApp, currentDate, isTest, uuid, null);
            }
            // check if credentials are valid and not expired
            if (!credentialsOk(uuid, householdApps, currentDate, isTest)) {
                return;
            }
            LocalDateTime firstAcceptedDate = LocalDateTime.of(ssiApp.getTime(), LocalTime.of(00, 00, 00));
            Boolean accepted = false;
            // find the first day the case was accepted
            Iterator<Entry<LocalDateTime, State>> it = monitoredCase.getHistory().entrySet().iterator();
            while (it.hasNext() && !accepted) {
                Entry<LocalDateTime, State> entry = it.next();
                accepted = entry.getValue().equals(State.ACCEPTED) ? true : false;
                if (accepted) {
                    firstAcceptedDate = entry.getKey();
                }
            }
            // if (MonitorUtils.isCaseOlderThanSixMonths(firstAcceptedDate, currentDate) ||
            // !MonitorUtils.checkExternalSources()) {
            // //update the status of the case to REJECTED and the date with the current
            // date
            // updateCase(uuid, State.REJECTED, ssiCase.isPresent()? ssiApp : null,
            // currentDate, sync);
            // //this.mongoServ.deleteByUuid(uuid);
            // } else {
            // final SsiApplication ssiApp = ssiApp;
            // handleDeceasedMember(ssiApp);
            // check the application by the uuid and update the case accordingly

            // aggregate all the financial values of the household to one new application
            // for verification
            SsiApplication aggregatedSsiApp = EthAppUtils.aggregateHouseholdValues(householdApps);
            if (checkHouseholdCredentials(monitoredCase, ssiApp, householdApps, aggregatedSsiApp)
                    && !MonitorUtils.isCaseOlderThanSixMonths(firstAcceptedDate, currentDate)) {
                // if there is a missing application in the household suspend the case
                if (!checkHouseholdApplications(monitoredCase, ssiApp, householdApps, currentDate.toLocalDate())) {
                    rejectOrSuspendCases(uuid, State.SUSPENDED, householdApps, currentDate, isTest);
                    return;
                }
                updateCase(monitoredCase, State.ACCEPTED, ssiApp, currentDate, isTest, uuid, aggregatedSsiApp);
            } else {
                rejectOrSuspendCases(uuid, State.REJECTED, householdApps, currentDate, isTest);
            }
            // }
        });
    }

    private void rejectOrSuspendCases(String uuid, State state, List<SsiApplication> householdApps,
            LocalDateTime currentDate, Boolean isTest) {
        for (SsiApplication hhSsiApp : householdApps) {
            Optional<Case> theCase = this.ethServ.getCaseByUUID(hhSsiApp.getUuid());
            updateCase(theCase.isPresent() ? theCase.get() : new Case(), state, hhSsiApp, currentDate, isTest, uuid,
                    null);
        }
    }

    private void updateCase(Case monitoredCase, State state, SsiApplication ssiApp, LocalDateTime currentDate,
            Boolean isTest, String uuid, SsiApplication aggregatedSsiApp) {
        // Optional<Case> theCase = this.ethServ.getCaseByUUID(uuid);
        if (!(monitoredCase.getUuid() == null || "".equals(monitoredCase.getUuid()))) {
            // synchronize transaction for test data only if the state changes
            // if(sync && theCase.get().getState().equals(state)){
            // sync = false;
            // }
            monitoredCase.setState(state);
            monitoredCase.setDate(currentDate);
            if (ssiApp != null) {
                List<SsiApplication> allHouseholdApps = mongoServ
                        .findByTaxisAfmIn(EthAppUtils.fetchAllHouseholdAfms(ssiApp));
                // BigDecimal offsetBefore = theCase.get().getOffset();
                MonitorUtils.calculateOffset(monitoredCase, ssiApp, allHouseholdApps);
                if (state.equals(State.ACCEPTED)) {

                    // find the dates the case is accepted during this month and add 1 for the
                    // current day that hasn't yet been saved in the block chain
                    Long acceptedDatesCurrMonth = monitoredCase.getHistory().entrySet().stream().filter(
                            e -> (e.getKey().toLocalDate().compareTo(currentDate.toLocalDate().withDayOfMonth(1)) >= 0)
                                    && e.getKey().toLocalDate().compareTo(currentDate.toLocalDate()) <= 0
                                    && e.getValue().equals(State.ACCEPTED))
                            .count() + 1;

                    BigDecimal dailySum = MonitorUtils.calculateCurrentPayment(monitoredCase, ssiApp, allHouseholdApps,
                            currentDate.toLocalDate(), true);

                    monitoredCase.setDailyValue(
                            dailySum.divide(BigDecimal.valueOf(acceptedDatesCurrMonth), 2, RoundingMode.HALF_UP));
                    monitoredCase.setDailySum(dailySum);

                    if (isTest) {
                        ExportCaseToExcel excelExporter = new ExportCaseToExcel(monitoredCase);
                        try {
                            excelExporter.export();
                        } catch (IOException e1) {
                            log.error("export to excel error :{}", e1.getMessage());
                        }
                    }
                }
                
                
                // synchronize transaction for test data only if the offset changes
                // if(offsetBefore.compareTo(theCase.get().getOffset()) == 0){
                //     sync = false;
                // }
            }
            this.ethServ.updateCase(monitoredCase);
            log.info("updated case uuid :{}, date :{}, state :{}, dailyValue :{}, offset:{}, sum:{} ", monitoredCase.getUuid(), monitoredCase.getDate(), monitoredCase.getState(), monitoredCase.getDailyValue(), monitoredCase.getOffset(), monitoredCase.getDailySum());
        } else {
            log.error("cannot find case {} while trying to update it", uuid);
        }
    }

    private Boolean credentialsOk(String uuid, List<SsiApplication> householdApps, LocalDateTime currentDate, Boolean sync){
        Boolean credsOk = true;
        CredsAndExp[] credIdAndExp = this.mongoServ.findCredentialIdsByUuid(uuid);
        if(credIdAndExp == null){
            return true;
        }
        for(int i = 0; i < credIdAndExp.length; i++){
            log.info("checking credential {} from case {}", credIdAndExp[i].getId(), uuid);
            //check if the credential has not expired
            LocalDateTime expiresAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(credIdAndExp[i].getExp())), ZoneId.systemDefault());
            //Date expiresAt = Date.from(Instant.ofEpochSecond(Long.parseLong(credIdAndExp[i].getExp())));
            log.info("credential expires at {}", expiresAt);
            if (!expiresAt.isAfter(currentDate)) {
                //if credentials have expired update case as suspended(paused)
                rejectOrSuspendCases(uuid, State.SUSPENDED, householdApps, currentDate, sync);
                credsOk = false;
                break;
            }
            //check if the credential is revoked
            boolean isRevoked = this.ethServ.checkRevocationStatus(credIdAndExp[i].getId());
            log.info("is credential {} revoked? == {}", credIdAndExp[i].getId(), isRevoked);
            if (isRevoked){
                rejectOrSuspendCases(uuid, State.REJECTED, householdApps, currentDate, sync);
                credsOk = false;
                break;
            }
            credsOk = true;
        }

        return credsOk;
    }

    private Boolean checkForFailedPayments(Case monitoredCase){
        List<CasePayment> failedPayments = monitoredCase.getPaymentHistory().stream().filter(s -> s.getState().equals(State.FAILED)).collect(Collectors.toList());
        int failedCount = 1;
        if(failedPayments.size() >= 3){
            for(int i = 1; i<failedPayments.size(); i++){
                if(failedPayments.get(i).getPaymentDate().getMonthValue() == failedPayments.get(i-1).getPaymentDate().getMonthValue()+1){
                    failedCount++;
                    if(failedCount >= 3){
                        log.info("rejected - payment failed for 3 or more months");
                        return true; 
                    }
                } else{
                    failedCount = 1;
                }
            }
        }

        return false;
    }

    private Boolean checkHouseholdApplications(Case monitoredCase, SsiApplication ssiApp, List<SsiApplication> householdApps, LocalDate currentDate){
        //final LocalDate currentDate = LocalDate.now();
        final LocalDate endDate = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), EthAppUtils.monthDays(currentDate));

        List<HouseholdMember> household = ssiApp.getHouseholdComposition();

        //check if by the end of the month all the members of the household have submitted an application
        if(currentDate.equals(endDate)){
            List<String> appAfms = householdApps.stream().map(a -> a.getTaxisAfm()).collect(Collectors.toList());
            List<String> householdAfms = household.stream().map(m -> m.getAfm()).collect(Collectors.toList());

            if(!householdAfms.containsAll(appAfms)){
                return false;
            }
        }
        return true;
    }

    private Boolean checkHouseholdCredentials(Case monitoredCase, SsiApplication ssiApp, List<SsiApplication> householdApps, SsiApplication aggregatedSsiApp){
        //final LocalDate currentDate = LocalDate.now();
        //final LocalDate endDate = LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), EthAppUtils.monthDays(currentDate));

        List<HouseholdMember> household = ssiApp.getHouseholdComposition();

        //check if by the end of the month all the members of the household have submitted an application
        // if(currentDate.equals(endDate)){
        //     List<String> appAfms = householdApps.stream().map(a -> a.getTaxisAfm()).collect(Collectors.toList());
        //     List<String> householdAfms = household.stream().map(m -> m.getAfm()).collect(Collectors.toList());

        //     if(!householdAfms.containsAll(appAfms)){
        //         return false;
        //     }
        // }

        //check for deceased members in the household
        // if(household.stream().anyMatch(h -> checkForDeceasedMembers(h))){
        //     log.info("rejected - deceased member in household");
        //     return false;
        // }

        //check if there are more than one principal members
        if(mongoServ.findByHouseholdPrincipalIn(household).size()>1){
            log.info("rejected - more than one principal in household");
            return false;
        }

        for(HouseholdMember member:household){
            List<SsiApplication> householdDuplicates = mongoServ.findByHouseholdComposition(member);
            if(householdDuplicates.size()>1){
                log.info("rejected - duplicate applications in household");
                return false;
            }
        }

        //check if two months have passed while the application is in status suspended
        Iterator<Entry<LocalDateTime, State>> it = monitoredCase.getHistory().entrySet().iterator();
        LocalDate suspendStartDate = LocalDate.of(1900, 1, 1);
        LocalDate suspendEndDate = LocalDate.of(1900, 1, 1);
        while(it.hasNext()){
            if(suspendEndDate.equals(suspendStartDate.plusMonths(2))){
                log.info("rejected - application suspended for 2 months or more");
                return false;
            }
            Map.Entry<LocalDateTime, State> entry = it.next();
            if(!entry.getValue().equals(State.SUSPENDED)){
                suspendStartDate = LocalDate.of(1900, 1, 1);
                continue;
            }
            if(suspendStartDate.equals(LocalDate.of(1900, 1, 1))){
                suspendStartDate = entry.getKey().toLocalDate();
            }
            suspendEndDate = entry.getKey().toLocalDate();
        }

        //economics check
        if(EthAppUtils.getTotalMonthlyValue(aggregatedSsiApp, null).compareTo(BigDecimal.ZERO) == 0){
            log.info("rejected - financial data restriction (total household income > payment thresshold)");
            return false;
        }

        //validate each household application credentials
        for(SsiApplication app:householdApps){
            if(!checkIndividualCredentials(app)){
                
                return false;
            }
        }

        return true;
    }

    public Boolean checkIndividualCredentials(SsiApplication ssiApp){

        List<HouseholdMember> household = ssiApp.getHouseholdComposition();
        if(household == null){
            log.info("rejected - household missing");
            return false;
        }

        //external oaed check
        // if(!oaedRegistrationCheck(ssiApp.getOaedId())){
        //     log.info("rejected - applicant not found on OAED");
        //     return false;
        // }

        //check unemployment status
        // if(ssiApp.getUnemployed().equals("false")){
        //     return false;
        // }

        //external housing subsidy check
        // if(!houseBenefitCheck(ssiApp.getTaxisAfm()){
        //     log.info("rejected - housing benefits");
        //     return false;
        // }
        
        // check for luxury living
        // if(ssiApp.getLuxury() == null? false : ssiApp.getLuxury().equals(String.valueOf(Boolean.TRUE))){
        //     log.info("rejected - luxury living");
        //     return false;
        // }
        
        // check that if there differences in Amka register
        // if(differenceInAmka(ssiApp.getTaxisAmka())){
        //     log.info("rejected - differences in AMKA");
        //     return false;
        // }

        //check if iban exists in other application
        if(mongoServ.findByIban(ssiApp.getIban()).size() > 1){
            log.info("rejected - duplicate IBAN");
            return false;
        }
        log.info("return check true ?");
        return true;
    }

    //mock check for deceased members in the household
    private Boolean checkForDeceasedMembers(HouseholdMember member){
        return false;
    }

    private void handleDeceasedMember(SsiApplication ssiApp){
        List<HouseholdMember> household = ssiApp.getHouseholdComposition();
        boolean changed = false;
        for(HouseholdMember member:household){
            if(checkForDeceasedMembers(member)){
                household.remove(member);
                changed = true;
            }
        }

        if(changed){
            ssiApp.setHouseholdComposition(household);
            LinkedHashMap<String, List<HouseholdMember>> hhHistory = ssiApp.getHouseholdCompositionHistory();
            hhHistory.put(DateUtils.dateToString(LocalDateTime.now()), household);
            ssiApp.setHouseholdCompositionHistory(hhHistory);
            mongoServ.updateSsiApp(ssiApp);
        }
    }

    //mock oaed registration check
    private void oaedRegistrationCheck(Case monitoredCase, SsiApplication ssiApp, List<LocalDate> rejectionDates){
        // mock check
        if(monitoredCase.equals("")){
            LocalDate actualUpdateDate = LocalDate.now(); // mock date, this should return the actual date of the altered credential
            ssiApp.setUnemployed("false");
            mongoServ.updateSsiApp(ssiApp);
            rejectionDates.add(actualUpdateDate);
        }
    }
    //mock housing subsidy check
    private void houseBenefitCheck(Case monitoredCase, SsiApplication ssiApp, List<LocalDate> rejectionDates){
        // mock check
        if(monitoredCase.equals("")){
            LocalDate actualUpdateDate = LocalDate.now(); // mock date, this should return the actual date of the altered credential
            rejectionDates.add(actualUpdateDate);
        }
    }

    private void luxuryLivingCheck(Case monitoredCase, SsiApplication ssiApp, List<LocalDate> rejectionDates){
        // mock check
        if(monitoredCase.equals("")){
            LocalDate actualUpdateDate = LocalDate.now(); // mock date, this should return the actual date of the altered credential
            ssiApp.setLuxury("true");
            mongoServ.updateSsiApp(ssiApp);
            rejectionDates.add(actualUpdateDate);
        }
    }

    //mockAmkaCheck
    private void amkaCheck(Case monitoredCase, SsiApplication ssiApp, List<LocalDate> rejectionDates){
        // mock check
        if(monitoredCase.equals("")){
            LocalDate actualUpdateDate = LocalDate.now(); // mock date, this should return the actual date of the altered credential
            rejectionDates.add(actualUpdateDate);
        }
    }

    private void financialsCheck(Case monitoredCase, SsiApplication ssiApp){
        //mock check
        if(monitoredCase.equals("")){
            //update financial values and histories
            
        }
    }

    private Boolean externalChecksAndUpdate(Case monitoredCase, List<SsiApplication> householdApps, SsiApplication principalApp){

        List<LocalDate> rejectionDates = new ArrayList<>();
        //validate each household application credentials
        for(SsiApplication app:householdApps){
            oaedRegistrationCheck(monitoredCase, app, rejectionDates);
            houseBenefitCheck(monitoredCase, app, rejectionDates);
            luxuryLivingCheck(monitoredCase, app, rejectionDates);
            amkaCheck(monitoredCase, app, rejectionDates);
            financialsCheck(monitoredCase, app);
        }
        handleDeceasedMember(principalApp);

        if(!rejectionDates.isEmpty()){
            LocalDate minDate = rejectionDates.stream()
                    .min( Comparator.comparing( LocalDate::toEpochDay ) )
                    .get();
            if(monitoredCase.getRejectionDate().equals("") || DateUtils.dateStringToLD(monitoredCase.getRejectionDate()).isAfter(minDate)){
                monitoredCase.setRejectionDate(DateUtils.dateToString(minDate));
            }
            return false;
        }

        return true;
        
    }

}
