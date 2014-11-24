package com.wahibhaq.sqlitewrapperproject;
 
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

 
public class SqliteTestActivity extends Activity {
 
	RelativeLayout rl;
	
	EditText txtName, txtEmail;
	Button btnSave, btnUpdate;
	
	final String TAG = "SqliteTestActivity";
	
	private String nameValue; 
	private String emailValue;
	private static int candidateId;
	
	DBOperationsManager dbObj;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlitetest);
        
    	dbObj = new DBOperationsManager(this);

        
        txtName = (EditText) findViewById(R.id.etName);
        txtEmail = (EditText) findViewById(R.id.etEmail);

        
        btnSave = (Button) findViewById(R.id.btnSave);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        
        btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        nameValue = txtName.getText().toString();
		        emailValue = txtEmail.getText().toString();


		        if(isAllDataEntered())
		        {
					storeInDb();
			       
		        }
		        else
					Toast.makeText(getApplicationContext(), "Error : Data is missing !", Toast.LENGTH_SHORT).show();

			}
		});
        

        btnUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
		        nameValue = txtName.getText().toString();
		        emailValue = txtEmail.getText().toString();


		        if(isAllDataEntered())
		        {
					updateEmail();
			       
		        }
		        else
					Toast.makeText(getApplicationContext(), "Error : Data is missing !", Toast.LENGTH_SHORT).show();

			}
		});
        
        
        fetchAllNames();
		fetchAllEmails();
		
    }
    
    
    private boolean isAllDataEntered()
    {
    	if(nameValue.equals("") || emailValue.equals(""))
    		return false;
    	else
    		return true;
    }
    
    private int fetchLastId()
    {
    	if(dbObj.fetchLastId().length() == 0)
    		candidateId = 0;
    	else
    		candidateId = Integer.valueOf( dbObj.fetchLastId());
    	
    	return candidateId;
    	
    }
  
    
    private void storeInDb()
    {
    	candidateId +=1 ;
    	
    	if(dbObj.creatCandidateRecord(String.valueOf(candidateId), nameValue, emailValue))
    	{
    		txtEmail.setText("");
    		txtName.setText("");
    		
			Toast.makeText(getApplicationContext(), "Data is saved !", Toast.LENGTH_SHORT).show();
			
			fetchLastRecord();
			
    	}
    	else
			Toast.makeText(getApplicationContext(), "Error : Data is not saved !", Toast.LENGTH_SHORT).show();

    	
    }
    
    private void fetchLastRecord()
    {
    	Log.i(TAG, "last record stored = " + dbObj.fetchRecordById(fetchLastId()).toString() );
    }
    
    private void fetchLastName()
    {
    	Log.i(TAG, "last name stored = " + dbObj.fetchLastCandidateName());

    }
    
    private void fetchAllNames()
    {
    	
    	Log.i(TAG, "all names = " + Arrays.toString(dbObj.getCandidateAllNameList()));

    }
    
    private void fetchAllEmails()
    {
    	Log.i(TAG, "all emails = " + Arrays.toString(dbObj.getCandidateAllEmailList() ) );

    }

	private void deleteRecordById(String Id)
	{
		dbObj.deleteRecord(Id);
	}
		
	
	private void updateEmail()
	{
		String idToUpdate = dbObj.fetchIdByName(nameValue);
		if(idToUpdate.isEmpty() == false)
		{
			dbObj.updateEmail(emailValue, idToUpdate);
			Log.i(TAG, "updated record = " + dbObj.fetchRecordById(Integer.valueOf(dbObj.fetchIdByName(nameValue))).toString() );
		}
		else
		{
			
		}
		
    	

		
	}
    
    public void onResume()
    {
    	super.onResume();
    	
    	fetchLastId();
    	nameValue = "";
    	emailValue = "";
    	
    }
 
    
}