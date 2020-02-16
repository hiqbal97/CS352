/**
This class is the class of Client Form
@author 
*/
import java.awt.Color;
import javax.swing.filechooser.*; 
import java.net.*;
import java.io.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
public class Client extends javax.swing.JFrame
{
    public Client() {
        initComponents();
        
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        textAddress = new javax.swing.JTextField();
        textAddress.setText("localhost");
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        fileChooser = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        op = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Connect and Upload");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Server Address");

        fileChooser.setFileFilter(new FileNameExtensionFilter( "MSWord", "doc", "docx"));

        op.setEditable(false);
        op.setColumns(20);
        op.setRows(5);
        op.setSelectedTextColor(new java.awt.Color(255, 0, 51));
        jScrollPane1.setColumnHeaderView(op);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(28)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(fileChooser, GroupLayout.PREFERRED_SIZE, 596, GroupLayout.PREFERRED_SIZE)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(jLabel1)
        							.addPreferredGap(ComponentPlacement.UNRELATED)
        							.addComponent(textAddress, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE)
        							.addGap(29)
        							.addComponent(jButton1))))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(73)
        					.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 487, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(7)
        			.addComponent(fileChooser, GroupLayout.PREFERRED_SIZE, 388, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel1)
        				.addComponent(textAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jButton1))
        			.addGap(32)
        			.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        			.addGap(71))
        );
        getContentPane().setLayout(layout);

        pack();
    }
    private Socket socket;
    private OutputStream out;
    private InputStream in;
    private int CHUNK_SIZE = 1024;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)
    {
       try
       {
          byte buff[] = new byte[1024]; 
          op.setForeground(Color.red);
          int portNum = 5520;
          String hostAddress = textAddress.getText();
          socket = new Socket(hostAddress, portNum);
          out = socket.getOutputStream();
          in = socket.getInputStream();
          op.append ("Connected" + "\n");
          op.append("Send file name: " 
                         + fileChooser.getSelectedFile().getName() + "\n");
          sendNullTerminatedString(fileChooser.getSelectedFile().getName());
          op.append("Send file length: " 
                        + Long.toString(fileChooser.getSelectedFile().length())
                        + "\n");
          sendNullTerminatedString(Long.toString
                                  (fileChooser.getSelectedFile().length()));
          op.append("Sending file....." + "\n");
          sendFile(fileChooser.getSelectedFile().getPath());
          op.append("File sent Waiting for the Server....." + "\n");
          in.read(buff,0,1);
          if( buff[0] == '@')
          {
             op.append("Upload O.K." + "\n");
          }
          else
          {
             op.append("Error" + "\n");
          }
          socket.close();
          out.close();
          in.close();
          op.append("Disconnected." + "\n");
       }
       catch (Exception ex)
       {
          op.append(ex.toString() + "\n");
       }
    }

    public static void main(String args[])
    {
  
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
   
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea op;
    private javax.swing.JTextField textAddress;
   private void sendNullTerminatedString(String s)
   {
      try
      {
         out.write(s.getBytes());
         out.write('\0');
      }
      catch (Exception ex)
      {
         op.append(ex.toString() + '\n');
      }
   } 
   
   private void sendFile(String fullPathFileName)
   {
      try
      {
         int count;
         byte[] buffer = new byte[CHUNK_SIZE];
         File file = new File(fullPathFileName);
         FileInputStream in = new FileInputStream(file);
         while((count = in.read(buffer)) > -1)
         {
            try
            {
               out.write(buffer, 0, count);
            }
            catch(Exception ex)
            {
               op.append(ex.toString() + "\n");
            }
        }
      }
      catch ( Exception ex)
      {
         op.append(ex.toString() + "\n");
      }
   }
}