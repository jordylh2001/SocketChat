package org.example;

import org.junit.Test;

public class PrimarycontrollerTest{
    @Test
    public void IniciarsesionTest(){
        PrimaryController prim=new PrimaryController();
        if(prim.Checar("jordy")){
            if(prim.IniciarSesion("jordy","1234")){
                System.out.println("Bienvenido: Jordy");
            }else{
                System.out.println("Verifique los datos");
            }
        }else{
            System.out.println("El usuario no existe");
        }
    }

    @Test
    public void CrearUsuarioTest(){
        PrimaryController prim=new PrimaryController();
        if(prim.Checar("jordy")){
            System.out.println("El usuario ya existe");
        }else{
            prim.Crearuser("jordy","1234");
        }
    }
}
