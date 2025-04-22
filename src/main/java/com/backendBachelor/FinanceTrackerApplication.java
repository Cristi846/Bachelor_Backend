package com.backendBachelor;

import com.backendBachelor.Config.FirebaseConfig;
import com.backendBachelor.Repository.ExpenseRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;


@SpringBootApplication
public class FinanceTrackerApplication {

    public static void main(String[] args) {
        try {
            // Initialize Firebase
            FirebaseConfig.initializeFirebase();

            // Start Spring Boot application
            SpringApplication.run(FinanceTrackerApplication.class, args);

            System.out.println("Finance Tracker application started successfully!");


        } catch (IOException e) {
            System.err.println("Failed to initialize Firebase: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}