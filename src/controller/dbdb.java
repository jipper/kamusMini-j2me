package controller;
 

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import com.sun.midp.io.SystemOutputStream;
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
	
	public void searchDRS(TextField tfFind, StringItem str)
	{
		RecordEnumeration ree = null;
		ComparatorString comp = new ComparatorString();

		try {
			ree = rStore.enumerateRecords(null, comp, false);
		} catch (RecordStoreNotOpenException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		while(ree.hasNextElement())
		{
			try {
		//		 System.out.println(new String(ree.nextRecord())); // bikin erro ja
				ree.nextRecord();
			} catch (InvalidRecordIDException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RecordStoreNotOpenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RecordStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void searchRecordStore(TextField tfFind,StringItem str)
	{
		
		
		try
		{
			if(rStore.getNumRecords() > 0)
			{
				
				SearchFilter search = new SearchFilter(tfFind.getString());
				System.out.println("finding : "+ tfFind.getString());
				RecordEnumeration re = rStore.enumerateRecords(search, null, false);
				
				
				if(re.numRecords() > 0)
				{
					//System.out.println(new String(re.nextRecord(),0,re.nextRecord().length));
					
					ByteArrayInputStream bais = new ByteArrayInputStream(re.nextRecord());
					DataInputStream dis = new DataInputStream(bais);
								
		//			dis.readUTF();				 // get the id
					String cari = dis.readUTF(); // get word1
	//				System.out.println("string1 = "+cari);
					String hasil = dis.readUTF(); // get word2
	//				System.out.println("string2 = "+hasil);
					
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
			e.printStackTrace();
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



class ComparatorString implements RecordComparator
{
  private byte[] recData = new byte[10];

  // Read from a specified byte array    
  private ByteArrayInputStream strmBytes = null;
  
  // Read Java data types from the above byte array
  private DataInputStream strmDataType = null;
  
  public void compareStringClose()
  {
    try
    {
      if (strmBytes != null)
        strmBytes.close();
      if (strmDataType != null)
        strmDataType.close();
    }
    catch (Exception e)
    {}
  }
  
  public int compare(byte[] rec1, byte[] rec2)
  {
    String str1, str2;
    
    try
    {
      // If either record is larger than our buffer, reallocate
      int maxsize = Math.max(rec1.length, rec2.length);
      if (maxsize > recData.length)
        recData = new byte[maxsize];

      // Read record #1
      // Only need one read because the string to
      // sort on is the first "field" in the record
      strmBytes = new ByteArrayInputStream(rec1);
      strmDataType = new DataInputStream(strmBytes);
      str1 = strmDataType.readUTF();

      // Read record #2      
      strmBytes = new ByteArrayInputStream(rec2);
      strmDataType = new DataInputStream(strmBytes);
      str2 = strmDataType.readUTF();  
      
      // Compare record #1 and #2      
      int result = str1.compareTo(str2);
      if (result == 0)
        return RecordComparator.EQUIVALENT;
      else if (result < 0)
        return RecordComparator.PRECEDES;
      else
        return RecordComparator.FOLLOWS;          
      
    }      
    catch (Exception e)
    { 
      return RecordComparator.EQUIVALENT;
    } 
  }
}  
