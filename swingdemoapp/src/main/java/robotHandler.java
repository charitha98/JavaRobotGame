/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Acer
 */
public class robotHandler implements Runnable{
   
    private SwingArena swingArena;
    private boolean shutdown = false;
    private Object mutex = new Object();

    public robotHandler(SwingArena swing){
        
        swingArena = swing;        
    }
    @Override
    public void run() {
        
        while(true){
            
            swingArena.setRobotPosition();
            
        }
//        synchronized(mutex){
//            shutdown = true;
//        }
    }
}
