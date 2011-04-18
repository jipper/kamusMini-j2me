import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import controller.dbdb;

public class teskamus extends MIDlet implements CommandListener
{

	private Display display;
	
	private Command cmdExit, cmdFind, cmdStoring,   cmdCloseAndDeleteRms;
	private Form frm;
	private TextField tfKeyword;
	private StringItem strResult = new StringItem("Hasil", null);
	
	private dbdb db_handler; 
	 
	
	
	private String word1, word2;
	
	public teskamus() {
		// TODO Auto-generated constructor stub
		
		db_handler = new dbdb();
		db_handler.filename = "tes_db";
		db_handler.init();
		
		
		display = Display.getDisplay(this);
		
		cmdExit 	= new Command("cabut", Command.EXIT, 0);
		cmdFind 	= new Command("cari", Command.SCREEN, 1);
		cmdStoring 	= new Command("FileToRms", Command.SCREEN, 2);
 		cmdCloseAndDeleteRms = new Command("closeAndDeleteRms", Command.SCREEN, 5);
		
		
		frm = new Form("waka");
		
		frm.addCommand(cmdExit);
		frm.addCommand(cmdFind);
	 
		frm.addCommand(cmdStoring);
 		frm.addCommand(cmdCloseAndDeleteRms);
		
		tfKeyword = new TextField("kata", "", 10, TextField.ANY	);
		
		frm.append(tfKeyword);
		frm.append(strResult);	 
		
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
			
			db_handler.searchRecordStore(tfKeyword, strResult);
			
		}
		else if(c == cmdStoring)
		{
			//read from file.... and storing to rms db-temporary-as-y'all've-known-about...hhh
			try {
				baca_from_file_and_store_to_rms(db_handler);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 
		 
		else if(c == cmdCloseAndDeleteRms)
		{
			
			try {
				db_handler.close(true);
			} catch (RecordStoreNotOpenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RecordStoreNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RecordStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				destroyApp(true);
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

 
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		System.out.println("exiting....");
		try {
			db_handler.close(false);
		} catch (RecordStoreNotOpenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RecordStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyDestroyed();
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub
		
	}

	protected void startApp() throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
		
		 
	}
	
	
	
	///da func
	public void baca_from_file_and_store_to_rms(final dbdb x) throws IOException
	{
		new Thread(new Runnable() {
			public void run() {
 
				 
				 
 
						//DataInputStream dis = fd.openDataInputStream();
						
						InputStream is = getClass().getResourceAsStream("/data.res");
						DataInputStream dis = new DataInputStream(is); 
						
						int max_loop = 0;
						try {
							max_loop = dis.readInt();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("maxloop : " + max_loop);
						
						for(int i = 1; i <= max_loop ;i++)
						{
					 
						 
							 try {
								word1 = dis.readUTF();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							 try {
								word2 = dis.readUTF();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							 
								
							 							  
								System.out.println("word1 	-> " + word1);					 
								System.out.println("word2 	-> " + word2);
					  
							
							
							try {
								x.addNewData( word1, word2);
								System.out.println("==========added=================");
							} catch (RecordStoreNotOpenException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (RecordStoreFullException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (RecordStoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						
						try {
							is.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							dis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
				 
				 
 

			}
		}).start();
	}
	
 
 
}



