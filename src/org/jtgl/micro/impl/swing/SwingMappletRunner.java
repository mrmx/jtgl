/*
 * SwingMappletRunner.java
 *
 * Created on 29 de marzo de 2004, 21:06
 */

package org.jtgl.micro.impl.swing;

import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import org.jtgl.micro.*;

/**
 * Swing Mapplet runner
 * @author  Manuel Polo (manuel_polo at yahoo dot es)
 */
public class SwingMappletRunner extends JFrame {
    public final static int DEFAULT_WIDTH       = 128;
    public final static int DEFAULT_HEIGHT      = 160;
    
    private JMappletContextCanvas mappletCanvas;
    private JLabel lblStatus;
    private JMenuBar menuBar;
    private JMenu menuMapplet;        
    private JMenuItem menuMappletPause;    
    
    private JMenu menuView;
    private JMenu menuViewZoom;    
    private Mapplet currentMapplet;
    private ResourceBundle rscStr;
    private boolean paused = false;
    
    /** Creates a new instance of SwingMappletRunner */
    public SwingMappletRunner(int width,int height) {
        super();            
        initApp();
        setTitle(getStr("main.title"));                        
        getContentPane().add(mappletCanvas = new JMappletContextCanvas(this) ,BorderLayout.CENTER);
        lblStatus = new JLabel("");
        lblStatus.setBorder(new TitledBorder(""));
        getContentPane().add(lblStatus ,BorderLayout.SOUTH);
        mappletCanvas.setPreferredSize(new Dimension(width,height));                
        try {
            mappletCanvas.setIgnoreRepaint(true);
            createBufferStrategy(1);            
        }catch(Exception ex){
            ex.printStackTrace();
        }
        pack();
        mappletCanvas.requestFocus();
        setVisible(true);   
        
        setResizable(false);
        addWindowListener(new WindowAdapter(){ public void windowClosing(WindowEvent e){ actionExit();}});
        addMouseWheelListener(new MouseWheelHandler());
    }
    
    public SwingMappletRunner() {
        this(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }
    
    public static void main(String [] args){
        new SwingMappletRunner();
    }
    
    public void runMapplet(Mapplet mapplet){    
        if(mapplet != null){
            currentMapplet = mapplet;
            mappletCanvas.runMapplet(mapplet);                        
            paused = false;
        }
    }
    
    
    private void initApp(){                
        initLnF();
        initL10n();        
        setJMenuBar(createMenuBar());
    }    

    private void setZoom(int zoom){
        mappletCanvas.setZoom(zoom);   
        validate();
        pack();
    }
    
    private void initLnF(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }        
    }
    
    private void initL10n(){
        //TODO: extend to client subclass
        String packageName = SwingMappletRunner.class.getName();
        String bundle = packageName.substring( 0 , packageName.lastIndexOf('.') ).replace('.','/') + "/locale";
        rscStr = ResourceBundle.getBundle(bundle);
    }
  
    private JMenuBar createMenuBar(){
        menuBar = new JMenuBar();
        //Menu File
        JMenu menuFile = new JMenu(getStr("main.menu.file"));        
        JMenuItem menuFileRun = new JMenuItem(getStr("main.menu.file.run"));
        menuFileRun.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ actionRun();}});        
        menuFile.add(menuFileRun);
        menuFile.addSeparator();
        JMenuItem menuFileExit = new JMenuItem(getStr("main.menu.file.exit"));
        menuFileExit.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ actionExit();}});
        menuFile.add(menuFileExit);
        menuBar.add(menuFile);
        //Menu Mapplet
        menuMapplet = new JMenu(getStr("main.menu.mapplet")); 
        //menuMapplet.setEnabled(false);
        menuMappletPause = new JMenuItem(getStr("main.menu.mapplet.pause"));
        menuMappletPause.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ actionMappletPause();}});
        menuMapplet.add(menuMappletPause);
        menuBar.add(menuMapplet);
        //Menu View
        menuView = new JMenu(getStr("main.menu.view"));
        menuViewZoom = new JMenu(getStr("main.menu.view.zoom"));
        JMenuItem menuViewZoom1 = new JMenuItem("1:1");
        menuViewZoom1.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ setZoom(1);}});
        menuViewZoom.add(menuViewZoom1);
        JMenuItem menuViewZoom2 = new JMenuItem("2:1");        
        menuViewZoom2.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ setZoom(2);}});        
        menuViewZoom.add(menuViewZoom2);
        JMenuItem menuViewZoom3 = new JMenuItem("3:1");        
        menuViewZoom3.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ setZoom(3);}});        
        menuViewZoom.add(menuViewZoom3);
        JMenuItem menuViewZoom4 = new JMenuItem("4:1");        
        menuViewZoom4.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ setZoom(4);}});        
        menuViewZoom.add(menuViewZoom4);
        
        menuView.add(menuViewZoom);
        menuBar.add(menuView);
        return menuBar;
    }   
    
    private void actionRun(){    
        setStatus("Run mapplet..");
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
        pack();
    }
    
    private String getStr(String key){
        return rscStr.getString(key);
    }
    
    private class MouseWheelHandler implements MouseWheelListener {
        
        public void mouseWheelMoved(MouseWheelEvent e) {                 
            setZoom(mappletCanvas.getZoom() + e.getWheelRotation());
        }
        
    }    
    
}
