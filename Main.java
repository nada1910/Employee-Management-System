/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeemanagmentsystem;

import javax.swing.*;
public class Main {
    public static void main(String[] args){
        
        SwingUtilities.invokeLater(() -> {
            try {
                FileHandler fileHandler = new FileHandler();
                fileHandler.ensureDataFolder();                                 
                DepartmentService ds = new DepartmentService(fileHandler);    
                EmployeeService es = new EmployeeService(fileHandler, ds);      
                Manager manager = new Manager();                                 
                MainFrame frame = new MainFrame(es, ds, manager);                
                frame.setVisible(true);
                
            }
            //java jumps to the catch if the try block throw any exception
            catch (Exception ex) {
        
                JOptionPane.showMessageDialog(null,
                        "The application could not start:\n" + ex.getMessage(),
                        "Startup Error", JOptionPane.ERROR_MESSAGE);//make it red
                System.exit(1);
            }
        });
    }
        
    
}
