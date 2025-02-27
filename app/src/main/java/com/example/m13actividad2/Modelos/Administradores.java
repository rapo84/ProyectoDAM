package com.example.m13actividad2.Modelos;


public class Administradores extends Persona{
    
    private boolean isAdmin;
    
    

   public Administradores(String UID){

       super(UID);
    }

    public Administradores(String UID, String dni, String nombre, String apellidos, int telefono, String correo, String password, String categoria, boolean isAdmin) {
        super(UID, dni, nombre, apellidos, telefono, correo, password, categoria);
        this.isAdmin = isAdmin;
    }

    //Getter
   public boolean isAdmin() {
        return isAdmin;
    }
   
    //Setter
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
   
    //Sobrescribimos el metodo toString de la clase Persona
   @Override
   public String toString(){
     return "Administradores{" +super.toString() + "Es admin = "+ isAdmin +"super Persona" +'}';
   }


    
    
}
