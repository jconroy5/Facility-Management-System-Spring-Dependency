package com.fms.client;

import com.fms.main.*;
import com.fms.maintenance.Maintenance;
import com.fms.services.MaintenanceService;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MaintenanceClient {
    public MaintenanceClient() throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/app-context.xml");
        MaintenanceService maintenanceService = (MaintenanceService) context.getBean("maintenanceService");

        /**
         * Below we'll be adding "office" locations for the fictitious "Super Company" as example Facilities.
         */

        //office1
        Facility office1 = (Facility) context.getBean("facility");
        FacilityDetails office1Details = (FacilityDetails) context.getBean("facilityDetail");
        office1.setFacilityID(1);
        office1Details.setName("Super Company New York");
        office1Details.setLocation("New York, NY");
        office1Details.setNumberOfRooms(10);
        office1.setFacilityDetail(office1Details);

        //office2
        Facility office2 = (Facility) context.getBean("facility");
        FacilityDetails office2Details = (FacilityDetails) context.getBean("facilityDetail");
        office2.setFacilityID(2);
        office2Details.setName("Super Company Atlanta");
        office2Details.setLocation("Atlanta, GA");
        office2Details.setNumberOfRooms(17);
        office2.setFacilityDetail(office2Details);

        //office3
        Facility office3 = (Facility) context.getBean("facility");
        FacilityDetails office3Details = (FacilityDetails) context.getBean("facilityDetail");
        office3.setFacilityID(3);
        office3Details.setName("Super Company Los Angeles");
        office3Details.setLocation("Los Angeles, CA");
        office3Details.setNumberOfRooms(30);
        office3.setFacilityDetail(office3Details);

        //office4
        Facility office4 = (Facility) context.getBean("facility");
        FacilityDetails office4Details = (FacilityDetails) context.getBean("facilityDetail");
        office4.setFacilityID(4);
        office4Details.setName("Super Company Austin");
        office4Details.setLocation("Austin, TX");
        office4Details.setNumberOfRooms(23);
        office4.setFacilityDetail(office4Details);

        //example maintenance request for office1
        System.out.println("\nMaintenanceClient: Creating New Maintenance Request...");
        Maintenance maintenance = maintenanceService.makeFacilityMaintRequest(office1, "WiFi APs are down", 50);
        System.out.println("\nMaintenanceClient: Maintenance Request has been created");

        //scheduling the example maintenance request
        System.out.println("\nMaintenanceClient: Scheduling Maintenance Request...");
        maintenanceService.scheduleMaintenance(maintenance);
        System.out.println("\nMaintenanceClient: Maintenance Request has been scheduled");

        //calculating total cost of maintenance request at office1
        System.out.println("\nMaintenanceClient: Calculating Total Maintenance Cost...");
        int totalCost = maintenanceService.calcMaintenanceCostForFacility(office1);
        System.out.println("The total cost of the maintenance at Facility #" + office1.getFacilityID() + " is $" + totalCost + ".");

        //listing CURRENT maintenance requests in a table, using example Facilities (offices, set up above)
        System.out.println("\nMaintenanceClient: Listing Current Maintenance Requests at Facility...");
        List<Maintenance> maintRequestList = maintenanceService.listMaintRequests(office2);
        Object[][] requests = new Object[maintRequestList.size() + 1][2];
        requests[0] = new Object[] {"Maintenance Request Details", "Cost"};
        for (int i = 1; i <= maintRequestList.size(); i++) {
            requests[i] = new Object[] {maintRequestList.get(i-1).getMaintenanceDetails(), maintRequestList.get(i-1).getCost()};
        }
        System.out.println("Current Maintenance Requests at Facility #" + office2.getFacilityID() + ":");
        for (Object[] row : requests) {
            System.out.format("   %-29s%6s\n", row);
        }

        //listing COMPLETED maintenance requests in a table, using example Facilities (offices, set up above)
        System.out.println("\nMaintenanceClient: Listing Completed Maintenance Requests at Facility...");
        List<Maintenance> maintenanceList = maintenanceService.listMaintenance(office2);
        Object[][] maintenanceTable = new Object[maintenanceList.size() + 1][2];
        maintenanceTable[0] = new Object[] {"Maintenance Details", "Cost"};
        for (int i = 1; i <= maintenanceList.size(); i++) {
            maintenanceTable[i] = new Object[] {maintenanceList.get(i-1).getMaintenanceDetails(), maintenanceList.get(i-1).getCost()};
        }
        System.out.println("Completed Maintenance Requests at Facility #" + office2.getFacilityID() + ":");
        for (Object[] row : maintenanceTable) {
            System.out.format("   %-30s%6s\n", row);
        }

        //listing Facility problems in a table
        System.out.println("\nMaintenanceClient: Listing Problems Affecting a Facility...");
        List<Maintenance> facilityProblemsList = maintenanceService.listFacilityProblems(office2);
        Object[][] problems = new Object[facilityProblemsList.size() + 1][2];
        problems[0] = new Object[] {"Problem Details", "Cost"};
        for (int i = 1; i <= facilityProblemsList.size(); i++) {
            problems[i] = new Object[] {facilityProblemsList.get(i-1).getMaintenanceDetails(), facilityProblemsList.get(i-1).getCost()};
        }
        System.out.println("Problems affecting Facility #" + office2.getFacilityID() + ":");
        for (Object[] row : problems) {
            System.out.format("   %-30s%6s\n", row);
        }

        //calculating the down-time for a Facility
        System.out.println("\nMaintenanceClient: Calculating Facility Down-Time...");
        int downTime = maintenanceService.calcDownTimeForFacility(office2);
        System.out.println("Facility #" + office2.getFacilityID() + " was down for maintenance for " + downTime + " days, "
                + "assuming each completed maintenance request took one work week (5 days) to complete.");

        //calculating the problem rate for a Facility
        System.out.println("\nMaintenanceClient: Calculating Facility Problem Rate...");
        double problemRate = maintenanceService.calcProblemRateForFacility(office2) * 100;
        System.out.print("\nThe problem rate at Facility #" + office2.getFacilityID() + " is ");
        System.out.format("%.2f", problemRate);
        System.out.print("%.");
    }
}
