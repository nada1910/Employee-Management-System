
package com.mycompany.employeemanagmentsystem;

public class Manager {
    private static final String username="nadaa";
    private static final String password="nadaa1910";
    private boolean loggedIn;
    
    public String getUsername() {
        return username;
    }
    
    public boolean login(String username,String password){
        if(this.password.equals(password)&&this.username.equals(username)){
            this.loggedIn=true;
            return true;
        }
        return false;
    }
    
    public void logout() { loggedIn = false; }
    public boolean isLoggedIn() { return loggedIn; }
}
