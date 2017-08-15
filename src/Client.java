
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.*;

public class Client extends JFrame implements ActionListener {
	
	public Color co;// = Color.blue;//new Color((int)(Math.random() * 0x1000000));
	
	//ChatServer myob =new ChatServer();
	
    WhiteBoard myObject;
    public WhiteBoard myOb;
    boolean sendingdone = false, receivingdone = false;
    Socket socketToServer;
    ObjectOutputStream myOutputStream;
    ObjectInputStream myInputStream;
    Frame myframe;
    TextField textfield, textfieldmsg, privatmsgnam,privatmsg, colo;
    TextArea textarea;
    Button connect, disconnect, history,privat, userlist,randcolor;
    Panel whiteboard;
    protected int lastX = 0, lastY = 0;

    public Client() {

        myframe = new JFrame();
        myframe.setSize(600, 600);
        myframe.setTitle("Chat Client");
        myframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        Panel p1 = new Panel(new FlowLayout());
        p1.add(new Label("Username"));
        
        textfieldmsg = new TextField(12);
        p1.add(textfieldmsg);

        connect = new Button("Connect");
        p1.add(connect);

        disconnect = new Button("Disconnect");
        disconnect.setEnabled(false);
        p1.add(disconnect);

        privatmsgnam =new TextField(8);
        privatmsgnam.setText("For private");
        privatmsgnam.addMouseListener(new MouseAdapter()
		{

			public void mouseClicked(MouseEvent m) {
				// TODO Auto-generated method stub
				privatmsgnam.setText("");
				
			}
	
		});
        p1.add(privatmsgnam);
        randcolor = new Button("Color");
        randcolor.setEnabled(false);
        p1.add(randcolor);
        
        colo = new TextField(3);
        colo.setEnabled(true);
        p1.add(colo);
        
        
        myframe.add(p1, BorderLayout.NORTH);
        textarea = new TextArea();
        textarea.setEditable(false);
        myframe.add(textarea, BorderLayout.CENTER);

        whiteboard = new Panel();
        whiteboard.add(new Label("White Board"));
        whiteboard.setBackground(Color.white);
        whiteboard.setPreferredSize(new Dimension(300, 200));
        whiteboard.addMouseListener(new getCoordinates());
        whiteboard.addMouseMotionListener(new drawLine());
        whiteboard.setEnabled(false);
        myframe.add(whiteboard, BorderLayout.EAST);

        Panel p2 = new Panel(new FlowLayout());
        p2.add(new Label("Message"));
        textfield = new TextField(30);
        p2.add(textfield);

        connect.addActionListener(this);
        userlist = new Button("User List");
        userlist.setEnabled(false);
        p2.add(userlist);
        history = new Button("History");
        history.setEnabled(false);
        p2.add(history);
        
        privat = new Button("Private");
        privat.setEnabled(false);
        p2.add(privat);
        
        //privatmsg = new TextField(15);
       // p2.add(privatmsg);
        
        

        myframe.add(p2, BorderLayout.SOUTH);
        myframe.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == connect) {
            myframe.setTitle(textfieldmsg.getText().trim()+"'s Chat Window");
            try {
                String user = textfieldmsg.getText().trim();
                if (user.length() == 0) {
                    JOptionPane.showMessageDialog(myframe, "User name cannot be blank!", "Username", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String server = "localhost";
                int port = 5683;
    			//socketToServer = new Socket("afsaccess2.njit.edu", port);

                socketToServer = new Socket(server, port);

                myOutputStream
                        = new ObjectOutputStream(socketToServer.getOutputStream());

                myInputStream
                        = new ObjectInputStream(socketToServer.getInputStream());
                new Listener().start();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            //myob.setColors(co);
            myObject = new WhiteBoard();
            myObject.setFlag("connect");
            myObject.setMessage("has entered the chat room...");
            myObject.setName(textfieldmsg.getText());
           
            textfield.setText("");

            try {
                myOutputStream.reset();
                myOutputStream.writeObject(myObject);

                textfield.addActionListener(this);
                textfieldmsg.setEditable(false);
                textfield.requestFocus();
                connect.setEnabled(false);
                userlist.setEnabled(true);
                userlist.addActionListener(this);
                disconnect.setEnabled(true);
                disconnect.addActionListener(this);
                whiteboard.setEnabled(true);
                history.setEnabled(true);
                history.addActionListener(this);
                privat.setEnabled(true);
                privat.addActionListener(this);
                randcolor.setEnabled(true);
                randcolor.addActionListener(this);

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
            
        }else if(ae.getSource()== randcolor){
        	myOb = new WhiteBoard();
        	//myObject.setDrawFlag("drawing");
        	//co = new Color((int)(Math.random() * 0x1000000));
        	//myObject.setColors(co);
        	String b = new String(colo.getText());
        	
        	
        	//myObject.setFlag("color");
        	myOb.setDrawFlag(b);
        	myOb =new WhiteBoard();
            
            if(b.equalsIgnoreCase("blue"))
        		{co=Color.blue;
        		myOb.setColors(co);
        		}
        	if(b.equalsIgnoreCase("red")){
        		co=Color.red;
        		myOb.setColors(co);
        	}
        		
        	if(b.equalsIgnoreCase("green"))
        		{
        		co=Color.green;
        		myOb.setColors(co);
        		}
        	//colo.setText(myObject.getMessage());
        	/*try {
                myOutputStream.reset();
                myOutputStream.writeObject(myObject);

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        	*/
        	
        }
        
        else if(ae.getSource()== privat)
        {
        	if (!sendingdone) {
                myObject = new WhiteBoard();
                myObject.setFlag("private");
                myObject.setMessage( /*" To "+ privatmsgnam.getText()+": "+ */textfield.getText());
                myObject.setName(textfieldmsg.getText());
                myObject.setPriname(privatmsgnam.getText());
                //myObject.setFlag(" ");
                textfield.setText("");
                try {
                    myOutputStream.reset();
                    myOutputStream.writeObject(myObject);

                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
            }
        	
        	
        }
        
        else if (ae.getSource() == disconnect) {
            myObject = new WhiteBoard();
            myObject.setFlag("disconnect");
            myObject.setMessage("has left the chat room...");
            myObject.setName(textfieldmsg.getText());

            try {
                myOutputStream.reset();
                myOutputStream.writeObject(myObject);

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
            sendingdone = true;
            receivingdone = true;
            userlist.setEnabled(false);
            disconnect.setEnabled(false);
            textfield.setEditable(false);
            history.setEnabled(false);
        } 
        else if (ae.getSource() == userlist) 
        {
            myObject = new WhiteBoard();
            myObject.setName(textfieldmsg.getText());
            myObject.setMessage("userlist");
            myObject.setFlag("userlist");
            try {
                myOutputStream.reset();
                myOutputStream.writeObject(myObject);

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } 
        
        else if (ae.getSource() == history) {
            myObject = new WhiteBoard();
            myObject.setFlag("history");
            myObject.setMessage("history");
            myObject.setName(textfieldmsg.getText());
            try {
                myOutputStream.reset();
                myOutputStream.writeObject(myObject);

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } else {
            if (!sendingdone) {
                myObject = new WhiteBoard();
                myObject.setMessage(textfield.getText());
                myObject.setName(textfieldmsg.getText());
                myObject.setFlag(" ");
                textfield.setText("");
                try {
                    myOutputStream.reset();
                   myOutputStream.writeObject(myObject);

                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
            }
        }

    }

    private class getCoordinates extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            track(event.getX(), event.getY());
        }
    }

    private class drawLine extends MouseMotionAdapter {

        @Override
        public void mouseDragged(MouseEvent event) {

            Graphics g = whiteboard.getGraphics();
            
            myObject = new WhiteBoard(lastX, lastY, event.getX(), event.getY());
            myObject.paint(g);
            myObject.setMessage("whiteboard");
            myObject.setName("whiteboard");
            myObject.setFlag("whiteboard");
            //myObject.setColors(co);
            try {
                myOutputStream.reset();
                myOutputStream.writeObject(myObject);

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
            track(event.getX(), event.getY());
        }
    }

    protected void track(int x, int y) {
        lastX = x;
        lastY = y;
    }

    class Listener extends Thread {

        @Override
        public void run() {
            System.out.println("Listening for messages from server . . . ");
            try {
                while (!receivingdone) {
                    myObject = (WhiteBoard) myInputStream.readObject();
                    
                    if (myObject.getFlag().equalsIgnoreCase("whiteboard")) {
                        int x1, y1, x2, y2;
                        //Color co=new Color((int)(Math.random() * 0x1000000));

                        x1 = myObject.getx1();
                        y1 = myObject.gety1();
                        x2 = myObject.getx2();
                        y2 = myObject.gety2();
                        //co =myObject.getColors();
                        Graphics g = whiteboard.getGraphics();
                        //myObject.setColor(new Color((int)(Math.random() * 0x1000000)));
                        //myObject.setColor(myObject.getColor());
                        //if(myObject.getName().equalsIgnoreCase("cr"))
                        //	co=Color.blue;
                       // g.setColor(new Color(255,0,0));
                        
                        
                    	
                       // g.setColor(myObject.getColors());
                        g.setColor(co);
                        System.out.println(" client " + co);//myObject.getName());
                        g.drawLine(x1, y1, x2, y2);
                        

                    } else {
                        textarea.append(myObject.getName() + myObject.getMessage() + "\n");
                    }

                }
            } catch (IOException ioe) {
                System.out.println("IOE: " + ioe.getMessage());
            } catch (ClassNotFoundException cnf) {
                System.out.println(cnf.getMessage());
            }
        }
    }

    public static void main(String[] arg) {

        Client client = new Client();

    }
}
