package com.example.recorder.frament;

/**
 * Created by Tin on 2/23/2016.
 */
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;

import com.example.recorder.R;
import com.example.recorder.adapter.LvByContactAdapter;
import com.example.recorder.adapter.LvFileAdapter;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
 
 
 
public class TwoFragment extends Fragment{
	ListView lvInbox;
 
    public TwoFragment() {
        // Required empty public constructor
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        lvInbox = (ListView)view.findViewById(R.id.list);
        ShowSavedFiles();
        return view;
    }
    

    
    
    
    public void ShowSavedFiles(){
    	String root = Environment.getExternalStorageDirectory().toString();
    	ArrayList<String> filesinfolder = GetFiles(root +"/audioSave/");
    	Collections.reverse(filesinfolder);
    	   try {
    		   LvFileAdapter adapter
    		   = new LvFileAdapter(getActivity(), filesinfolder, "/audioSave/");

    		   lvInbox.setAdapter(adapter);
    		   
				} catch (Exception e) {
					// TODO: handle exception
				}
    	  }
    
    public ArrayList<String> GetFiles(String directorypath){
        ArrayList<String> Myfiles = new ArrayList<String>();
        File f = new File(directorypath);
        f.mkdirs();
        File[] files = f.listFiles(new AudioFileFilter());
        if(files.length==0){
            return Myfiles;
        }
        else{
            
			for(int i=0;i<files.length;i++)
			{
				int check = 0;
            	check = files[i].getName().toString().indexOf("check");
        		if(check>0)
        		{
        			Myfiles.add(files[i].getName());
        		}
                
			}
        }
        return Myfiles;
    }
    
    class AudioFileFilter implements FileFilter
    {
      private final String[] okFileExtensions = 
        new String[] {"amr", "3gpp", "mpg"};
     
      public boolean accept(File file)
      {
        for (String extension : okFileExtensions)
        {
          if (file.getName().toLowerCase().endsWith(extension))
          {
            return true;
          }
        }
        return false;
      }
    }

    
    public void showByContact()
    {
    	String root = Environment.getExternalStorageDirectory().toString();
    	ArrayList<String> filesinfolder = GetFiles(root +"/audioSave/");
    	ArrayList<String> arrByContact = groupContact(filesinfolder);
    	try {
    		LvByContactAdapter adapter
 		   = new LvByContactAdapter(getActivity(), arrByContact, "/audioSave/");

 		   lvInbox.setAdapter(adapter);
 		   
				} catch (Exception e) {
					// TODO: handle exception
				}
    	Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG).show();
    	
    }
    
    private ArrayList<String> groupContact(ArrayList<String> filesinfolder)
    {
    	ArrayList<String> arrByContact = new ArrayList<>();
    	int i =0;
    	 while (i<filesinfolder.size()){
			String file = filesinfolder.get(i);
    		String phoneFile = getPhone(file);
    		arrByContact.add(file);
    		int j = i+1;
    		
    		while (j < filesinfolder.size()) {
    			String fileJ = filesinfolder.get(j);
    			String phoneJ = getPhone(fileJ);
    			if(phoneFile.equals(phoneJ))
    			{
    				arrByContact.add(fileJ);
    				filesinfolder.remove(j);
    			}
    			j++;
			}
    		i++;
		}
    	return arrByContact;
    }
    
    private String getPhone(String data)
    {
    	String[] parts = data.split("_");
    	String date = parts[0];
    	String data2 = parts[1];
    	String[] parts2 = data2.split("-date-");
    	String time1 = parts2[0];
    	String data3 = parts2[1];
    	String[]part3 = data3.split("-phone-");
    	String phone = part3[0];
    	String[]part4 = time1.split("-");
    	String time = part4[0]+":" +part4[1];
    	return phone;
    }

	
 
}