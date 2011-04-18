package controller;
 

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;
public class dbdb {

	public RecordStore rStore = null;
	public String filename = null;
	//initzzz
	public void  init()
	{
			try
			{
				rStore = RecordStore.openRecordStore(this.filename, true);
			}
			catch(RecordStoreException rse)
			{
				rse.printStackTrace();
			}
	}
	
	//close
	public void close(boolean apus) throws RecordStoreNotOpenException, RecordStoreNotFoundException, RecordStoreException
	{
		//save it to db and only close not remove
		if(apus)
		{
			String filename = rStore.getName();
			rStore.closeRecordStore();
			RecordStore.deleteRecordStore(filename);
		}
		else
		{
			rStore.closeRecordStore();
		}
	}
	
	public void searchRecordStore(TextField tfFind,StringItem str)
	{
		try
		{
			if(rStore.getNumRecords() > 0)
			{
				SearchFilter search = new SearchFilter(tfFind.getString());
				RecordEnumeration re = rStore.enumerateRecords(search, null, false);
				
				
				if(re.numRecords() > 0)
				{
					 
					ByteArrayInputStream bais = new ByteArrayInputStream(re.nextRecord());
					DataInputStream dis = new DataInputStream(bais);
								
		//			dis.readUTF();				 // get the id
					String cari = dis.readUTF(); // get word1
					String hasil = dis.readUTF(); // get word2
				
					
					str.setText(cari + " :: " + hasil);
					
					dis.close();
					bais.close();
				}
				
				re.destroy();
					
				
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	//add new record
	public synchronized void addNewData(String nama,String nilai) throws IOException, RecordStoreNotOpenException, RecordStoreFullException, RecordStoreException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
 
		dos.writeUTF(nama);
		dos.writeUTF(nilai);
		
		byte[] b = baos.toByteArray();
		
		rStore.addRecord(b, 0, b.length);
	}
	
	public synchronized RecordEnumeration rEnumerate() throws RecordStoreNotOpenException
	{
		return rStore.enumerateRecords(null, null, false);
	}
}


class SearchFilter implements RecordFilter
{
	private String searchText = null;
	
	public SearchFilter(String searchText)
	{
		this.searchText = searchText.toLowerCase();
	}
	
	public boolean matches(byte[] candidate)
	{
		String str = new String(candidate).toLowerCase();
		
		
		if(searchText != null && str.indexOf(searchText) != -1)
			return true;
		else
			return false;
	}
}

