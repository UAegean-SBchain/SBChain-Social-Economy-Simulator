package com.example.ethereumserviceapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.ethereumserviceapp.model.HouseholdMember;
import com.example.ethereumserviceapp.model.entities.SsiApplication;
import com.example.ethereumserviceapp.utils.CsvUtils;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.anyOf;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
public class TestCsvUtils {

    @Test
    public void testCsvImport() {

        try {
            File initialFile = new File("src/main/resources/testData.csv");
            InputStream is = new FileInputStream(initialFile);
            CsvUtils.csvToSsiApplication(is);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Test
    public void testRandAFMGeneration() {
        assertEquals(CsvUtils.getRandomNumberByLength(9).length(), 9);
    }

    @Test
    public void testGetRandmoName() {
        String[] result = new String[0];
        try {
            result = CsvUtils.getRandomFirstName();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(result);
        System.out.println(result[0]);
    }

    @Test
    public void testGetRandomSurname() {
        assertThat(CsvUtils.getRandomSurName(), isOneOf("Doe", "Black", "Jo"));
    }

    @Test
    public void testBuildHouseholdString() {
        List<SsiApplication> householdAppList = new ArrayList<SsiApplication>();
        HouseholdMember principalMember = new HouseholdMember();
        principalMember.setAfm("1231312312");
        principalMember.setName("nikos");
        principalMember.setSurname("tr");

        SsiApplication principalApp = new SsiApplication();
        principalApp.setId(String.format("%09d", new Random().nextInt(10000)));
        principalApp.setUuid(String.format("%09d", new Random().nextInt(10000)));
        //ssiApp.setCredentialIds(transformCrendentialIds(csvRecord.get("credentialIds")));
        principalApp.setSsn(String.format("%09d", new Random().nextInt(10000)));
        principalApp.setTaxisAfm(principalMember.getAfm());
        principalApp.setTaxisFamilyName(principalMember.getSurname());
        principalApp.setTaxisFirstName(principalMember.getName());
        principalApp.setTaxisFathersName("anast");
        principalApp.setTaxisMothersName("agg");
        principalApp.setHouseholdPrincipal(principalMember);

        householdAppList.add(principalApp);

        SsiApplication memberApp = new SsiApplication();
        memberApp.setId(String.format("%09d", new Random().nextInt(10000)));
        memberApp.setUuid(String.format("%09d", new Random().nextInt(10000)));
        //ssiApp.setCredentialIds(transformCrendentialIds(csvRecord.get("credentialIds")));
        memberApp.setSsn(String.format("%09d", new Random().nextInt(10000)));
        memberApp.setTaxisAfm("2");
        memberApp.setTaxisFamilyName("ks");
        memberApp.setTaxisFirstName("kat");
        memberApp.setTaxisFathersName("l");
        memberApp.setTaxisMothersName("r");
        memberApp.setHouseholdPrincipal(principalMember);

        householdAppList.add(memberApp);

//        System.out.println(CsvUtils.makeHouseHoldString(householdAppList.g));
        //assertEquals("nikos;tr;1231312312;;|kat;ks;2;;", CsvUtils.makeHouseHoldString(householdAppList.get(0).getHouseholdComposition()));
    }



    @Test
    public void testMakeAdult(){
        System.out.println(CsvUtils.getAdultDateOfBirth());
    }

    @Test
    public void testMakeMinor(){
        System.out.println(CsvUtils.getMinorDateOfBirth());
    }

    @Test
    public void testMakeHouseholdApp() {
        List<SsiApplication> houshold = CsvUtils.generateMockHouseholdApplications();
        if (houshold.size() > 1)
            assertEquals(houshold.get(0).getHouseholdComposition().get(0).getAfm(), houshold.get(1).getHouseholdComposition().get(0).getAfm());
    }

    @Test
    public void testMakeHouseholdAppCSV() {
        List<SsiApplication> houshold = CsvUtils.generateMockData(50);
        CsvUtils.writeToCSV(houshold);
    }

}
