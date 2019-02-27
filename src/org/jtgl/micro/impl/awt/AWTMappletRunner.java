/*
 * AWTMappletRunner.java
 *
 * Created on 24 de diciembre de 2003, 21:27
 */

package org.jtgl.micro.impl.awt;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import org.jtgl.micro.*;

/**
 * AWT Mapplet runner
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class AWTMappletRunner extends Frame {
    public final static int DEFAULT_WIDTH       = 128;
    public final static int DEFAULT_HEIGHT      = 160;
    
    private MappletContextCanvas mappletCanvas;
    private Label lblStatus;
    private MenuBar menuBar;
    private Menu menuMapplet;        
    private MenuItem menuMappletPause;    
    
    private Menu menuView;
    private Menu menuViewZoom;    
    private Mapplet currentMapplet;
    private ResourceBundle rscStr;
    private boolean paused = false;
    
    /** Creates a new instance of AWTMappletRunner */
    public AWTMappletRunner(int width,int height) {
        super();        
        initApp();
        setTitle(getStr("main.title"));                        
        add(BorderLayout.CENTER, mappletCanvas = new MappletContextCanvas(this));
        add(BorderLayout.SOUTH, lblStatus = new Label(""));
        mappletCanvas.setPreferredSize(new Dimension(width,height));                
        pack();
        mappletCanvas.requestFocus();
        setVisible(true);   
        
        setResizable(false);
        addWindowListener(new WindowAdapter(){ public void windowClosing(WindowEvent e){ actionExit();}});
    }
    
    public AWTMappletRunner() {
        this(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
        
    public static void main(String [] args){
        new AWTMappletRunner();
    }

    public void runMapplet(Mapplet mapplet){    
        if(mapplet != null){
            currentMapplet = mapplet;
            mappletCanvas.runMapplet(mapplet);            
            paused = false;
        }
    }

    
    
    private void initApp(){                
        initL10n();        
        setMenuBar(createMenuBar());
    }    

    private void setZoom(int zoom){
        mappletCanvas.setZoom(zoom);        
        pack();
    }
    
    private void initL10n(){
        //TODO: extend to client subclass
        String className = AWTMappletRunner.class.getName();
        String bundle = className.substring( 0 , className.lastIndexOf('.') ).replace('.','/') + "/locale";        
        System.out.println("initL10n() resourcebundle:"+bundle);
        rscStr = ResourceBundle.getBundle(bundle);
    }
  
    private MenuBar createMenuBar(){
        menuBar = new MenuBar();
        //Menu File
        Menu menuFile = new Menu(getStr("main.menu.file"));        
        MenuItem menuFileRun = new MenuItem(getStr("main.menu.file.run"));
        menuFileRun.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ actionRun();}});        
        menuFile.add(menuFileRun);
        menuFile.addSeparator();
        MenuItem menuFileExit = new MenuItem(getStr("main.menu.file.exit"));
        menuFileExit.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ actionExit();}});
        menuFile.add(menuFileExit);
        menuBar.add(menuFile);
        //Menu Mapplet
        menuMapplet = new Menu(getStr("main.menu.mapplet")); 
        //menuMapplet.setEnabled(false);
        menuMappletPause = new MenuItem(getStr("main.menu.mapplet.pause"));
        menuMappletPause.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ actionMappletPause();}});
        menuMapplet.add(menuMappletPause);
        menuBar.add(menuMapplet);
        //Menu View
        menuView = new Menu(getStr("main.menu.view"));
        menuViewZoom = new Menu(getStr("main.menu.view.zoom"));
        MenuItem menuViewZoom1 = new MenuItem("1:1");
        menuViewZoom1.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ setZoom(1);}});
        menuViewZoom.add(menuViewZoom1);
        MenuItem menuViewZoom2 = new MenuItem("2:1");        
        menuViewZoom2.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ setZoom(2);}});        
        menuViewZoom.add(menuViewZoom2);
        MenuItem menuViewZoom3 = new MenuItem("3:1");        
        menuViewZoom3.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ setZoom(3);}});        
        menuViewZoom.add(menuViewZoom3);
        MenuItem menuViewZoom4 = new MenuItem("4:1");        
        menuViewZoom4.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ setZoom(4);}});        
        menuViewZoom.add(menuViewZoom4);
        
        menuView.add(menuViewZoom);
        menuBar.add(menuView);
        return menuBar;
    }   
    
    private void actionRun(){    
        if(currentMapplet != null){
            setStatus("Restarting mapplet..");
            runMapplet(currentMapplet);
        }
    }
    
    private void actionExit(){
        notifyKill(currentMapplet);
        dispose();        
        System.exit(0);
    }
    
    private void notifyKill(Mapplet mapplet){
        if(mapplet != null)            
                mappletCanvas.notifyKill(mapplet);                
    }    
    
    private void actionMappletPause(){
        paused = !paused;
        menuMappletPause.setLabel(paused?getStr("main.menu.mapplet.resume"):getStr("main.menu.mapplet.pause"));
        if(paused)
            mappletCanvas.pause();
        else
            mappletCanvas.start();
    }
    
    private void setStatus(String msg){
        lblStatus.setText(msg);
    }
    
    private String getStr(String key){
        return rscStr.getString(key);
    }
}
