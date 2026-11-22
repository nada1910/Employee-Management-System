
package com.mycompany.employeemanagmentsystem;

public class Validator {
    //nada
    public static void validateCredentials(String username,String password)
              throws ValidationException {
        if(username==null || username.trim().isEmpty()){
            throw new ValidationException("Username Must Not Be Empty");
           
        }
        if(password==null || username.trim().isEmpty()){
            throw new ValidationException("Password Must Not Be Empty");
        }
        
        
    }
    public static void validateSearchQuery(String search)
            throw ValidationException{
                if(search==null || search.trim().isEmpty()){
            throw new ValidationException("Search Query Must Not Be Empty");
           
        }
            }
    
    
    
    
}
