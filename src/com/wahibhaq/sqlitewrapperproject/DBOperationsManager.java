package com.wahibhaq.sqlitewrapperproject;


import java.io.IOException;
import java.util.ArrayList;


import android.content.Context;
import android.util.Log;

public class DBOperationsManager {
	
	DatabaseQuery queryCandidate;
	ArrayList<String> queryString;
	ArrayList<ArrayList<String>> queryString1;

	private String[] candidateAllNameList, candidateAllEmailList;


	private Context appContext;

	public DBOperationsManager(Context context) {
		

	        appContext = context; 
	        queryCandidate = new DatabaseQuery(appContext,"CandidatesList", true);
	        
	}

	
	//creat newUser
	public boolean creatCandidateRecord(String id, String name, String email)
	{
		queryCandidate.appendData("candidate_id",id);
		queryCandidate.appendData("candidate_name",name);
		queryCandidate.appendData("candidate_email", email);
		
		return queryCandidate.addRow();
		
		
	}

	
	public void updateEmail(String text, String candidateId)
	{
		queryCandidate.appendData("candidate_email", text);
		queryCandidate.updateRow("candidate_id", candidateId);
	}
	
	public String fetchLastId()
	{
		queryString = queryCandidate.getSelectiveDataBySortColumn(new String[] {"candidate_id"}, null, null, null, null, "candidate_id", "DESC");
		
    	if(queryString.size() != 0)
    		return queryString.get(0);
		else
			return "";

	}
	
	//fetch only name field from last records
	public String fetchLastCandidateName()
	{
		queryString = queryCandidate.getSelectiveDataBySortColumn(new String[] {"candidate_name"}, null, null, null, null, "candidate_id", "DESC");
		
    	if(queryString.size() != 0)
    		return queryString.get(0);
		else
			return "";	
	}
	
	//fetch all fields from last record
	public ArrayList<String> fetchRecordById(int id)
	{
		queryString = queryCandidate.getSelectiveDataBySortColumn( new String[] {"candidate_name","candidate_email"}, "candidate_id = " + "'" + id + "'", null, null, null, "candidate_id", "DESC");
		
		return queryString;
		
	}
	
	public String fetchIdByName(String name)
	{
		queryString = queryCandidate.getSelectiveDataBySortColumn( new String[] {"candidate_id"}, "candidate_name = " + "'" + name + "'", null, null, null, "candidate_id", "DESC");
		
		if(queryString.size() != 0)
    		return queryString.get(0);
		else
			return "";	
		
		
	}
	

	//designed to fetch all candidates records from table in a single Array List
	public void fetchCandidatesListData()
		{
			
			///fetching data from Outings Table
			queryString1 = queryCandidate.getData( new String[] {"candidate_name","candidate_email"}, null, null, null, null, "candidate_id", "ASC");
	        ArrayList<String> queryRow = new ArrayList<String>();
	        
	        Log.v("query1 size", String.valueOf(queryString1.size()));

	        candidateAllNameList = new String[queryString1.size()];
	        candidateAllEmailList = new String[queryString1.size()];
	        
	        

	        
	        if( queryString1.size() > 0)
	        {
	        	for(int i=0;i<queryString1.size();i++)
	        	{
	        		queryRow = queryString1.get(i);
	        		
	        			
	        			candidateAllNameList[i] = queryRow.get(0);
	        		    candidateAllEmailList[i] = queryRow.get(1);
	        		
	        	}

	        } 
	        
	      
			
		}
		

		public String[] getCandidateAllNameList()
		{
			fetchCandidatesListData();
			return candidateAllNameList;
		}

		public String[] getCandidateAllEmailList()
		{
			fetchCandidatesListData();

			return candidateAllEmailList;
		}
		
		

	public void deleteRecord(String Id) {
		
		queryCandidate.deleteRow("candidate_id", Id);


	}
		


	
}
