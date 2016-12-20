/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shooter.game.server.model.map_building;

/**
 *
 * @author ILMAZ
 */
public class Point {
    public boolean r = false; //right
    public boolean d = false; //down
    public int n = 0;
    
    public void r(){
        r = !r;
    }
    
    public void d(){
        d = !d;
    }
}
