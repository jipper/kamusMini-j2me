import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import controller.dbdb;
import me.regexp.*;

public class teskamus extends MIDlet implements CommandListener
{

	private Display display;
	
	private Command cmdExit, cmdFind,   cmdBackToMain;
	private Form frm;
	private Form frmResult;
	private TextField tfKeyword;
	//private StringItem strResult = new StringItem("Hasil", null), strResult1 = new StringItem("Hasil1", null), strResult2 = new StringItem("Hasil2", null), strResult3 = new StringItem("Hasil3", null), strResult4 = new StringItem("Hasil4", null);

	
	
	public teskamus() {
		// TODO Auto-generated constructor stub
		
	
		new dbdb();
		
		display = Display.getDisplay(this);
		
		cmdExit 			= new Command("cabut", Command.EXIT, 0);
		cmdFind 			= new Command("cari", Command.SCREEN, 1);
 
 		
 		cmdBackToMain = new Command("BackToMain", Command.SCREEN, 1);
		
		
		frm = new Form("waka");
		
		frm.addCommand(cmdExit);
		frm.addCommand(cmdFind);
	 
	 
		
		tfKeyword = new TextField("kata", "", 10, TextField.ANY	);
		
		frm.append(tfKeyword);
	//	frm.append(strResult);	 
		
		frm.setCommandListener(this);
		
		display.setCurrent(frm);
 
		
	}
	
	public void commandAction(Command c, Displayable s) {
		// TODO Auto-generated method stub
		if(c == cmdExit)
		{
			try {
				destroyApp(true);
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(c == cmdFind)
		{
		//	System.out.println(System.getProperty("microedition.io.file.FileConnection"));
						 
//			db_handler.filename = "tes_db";			
//			db_handler.init();
//			
			if(frmResult != null)
			{
				frmResult.deleteAll();
			}
			
			 baca_from_filestring_regex(tfKeyword.getString());
			 
 
			
		}
		else if(c == cmdBackToMain)
		{
			display.setCurrent(frm);
		}
	}

 
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		System.out.println("exiting....");
		
		 
		 
		notifyDestroyed();
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub
		
	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
		
		 
	}
	
	//new func
	private String readLine(Reader in) throws IOException {
	    // This is not efficient.
	    StringBuffer line = new StringBuffer();
	    int i;
	    while ((i = in.read()) != -1) {
	      char c = (char)i;
	      if (c == '\n') break;
	      if (c == '\r') ;
	      else line.append(c);
	    }
	    if (line.length() == 0) return null;
	    return line.toString();
	  }
	
	
	 
 
	
	public void baca_from_filestring_regex(final String tmp_string)
	{
 
		
		frmResult = new Form("resultz");
		frmResult.addCommand(cmdBackToMain);
		frmResult.setCommandListener(this);
		display.setCurrent(frmResult);
		
		System.out.println("starting...");
 		new Thread(new Runnable() {
 			
 		public void run() {
				// TODO Auto-generated method stub
				//InputStream is_arti = getClass().getResourceAsStream("/" + c + "_arti.txt");
				InputStream is_arti = getClass().getResourceAsStream("/arti.txt");
				DataInputStream dis_arti = new DataInputStream(is_arti);
				
				InputStream is_entri = getClass().getResourceAsStream("/entri.txt");
				DataInputStream dis_entri = new DataInputStream(is_entri);
				
				Reader reader_in_arti = new InputStreamReader(is_arti);
				Reader reader_in_entri = new InputStreamReader(is_entri);
				
				String tmp_data_arti;
				String tmp_data_entri;
				int zzz = 1;
 
				
				RE reg = new RE("^" + tmp_string.toLowerCase() +"(\\w*)");
				long millis_awal = System.currentTimeMillis();
				try {
					while(((tmp_data_entri = readLine(reader_in_entri)) != null) && ((tmp_data_arti = readLine(reader_in_arti)) != null))
					{
						
						
						
						if(reg.match(tmp_data_entri))	
						{
							
							StringItem strResult  = new StringItem("Hasil", null);
							strResult.setText(tmp_data_entri + " -> " + tmp_data_arti);
							frmResult.append(strResult);
							
						}
					 
	 
						
						
						
						zzz ++;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
	 
					e1.printStackTrace();
				}
				
				long millis_akhir = System.currentTimeMillis();
				
				System.out.println("jml waktu : " + (float)( millis_akhir - millis_awal)/1000 + " detik");
				Alert aler = new Alert("Selesai!", "wktu yg dprlukan : " + (float)( millis_akhir - millis_awal)/1000 + " detik" , null, AlertType.INFO);
				aler.setTimeout(Alert.FOREVER);
				display.setCurrent(aler);
				//closinggg
				try {
					is_arti.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					dis_arti.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					is_entri.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					dis_entri.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					reader_in_arti.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					reader_in_entri.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
 			}
 		}).start();
	}
	
	 
 
}



