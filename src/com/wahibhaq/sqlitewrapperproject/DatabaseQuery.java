package com.wahibhaq.sqlitewrapperproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.util.Log;


/**
 * This class adds multiple entries to the database and pulls them back out.
 * 
 * @author Hanly De Los Santos (http://hdelossantos.com)
 * @author Wahib-Ul-Haq
 */

public class DatabaseQuery {
	// Variables area
	private ArrayList<String> arrayKeys = new ArrayList<String>();
	private ArrayList<String> arrayValues = new ArrayList<String>();
	private DBAdapter database;
	private Context appContext;
	
	private static final String DATABASE_NAME = "candidaterecord.db";

	

	/**
	 * Initialize the ArrayList
	 * 
	 * @param context
	 *            Pass context from calling class.
	 */
	// just to open database assuming db is already there
	public DatabaseQuery(Context context, String tableName) {

		appContext = context;
		
		database = new DBAdapter(context, tableName, null, null);
		database.open();

	}

	// constructor to import db from assets
	public DatabaseQuery(Context context, String tableName, 	boolean isImportFromAsset) {

		appContext = context;
		
		//To delte the /cache and /databases
		//clearApplicationData(appContext); 

		if (isImportFromAsset == true) {

			try {
				importDBFromAssets();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}

		// Call the database adapter to create the table
		database = new DBAdapter(context, tableName, null, null);
		database.open();

	}
	
	public static void clearApplicationData(Context context) {
	    Log.i("delete", "Clearing app cache");

	    File cache = context.getCacheDir();
	    File appDir = new File(cache.getParent());
	    if (appDir.exists()) {
	        String[] children = appDir.list();
	        for (String s : children) {
	            File f = new File(appDir, s);
	            if(deleteDir(f))
	                Log.i("delete", String.format("*** DELETED -> (%s) ***", f.getAbsolutePath()));
	        }
	    }
	}

	private static boolean deleteDir(File dir) {
	    if (dir != null && dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }
	    return dir.delete();
	}

	/**
	 * Append data to an ArrayList to then submit to the database
	 * 
	 * @param key
	 *            Key of the value being appended to the Array.
	 * @param value
	 *            Value to be appended to Array.
	 */
	public void appendData(String key, String value) {
		arrayKeys.add(key);
		arrayValues.add(value);
	}
	

	/**
	 * This method adds the row created by appending data to the database. The
	 * parameters constitute one row of data.
	 */
	public boolean addRow() {
		
		if(database.insertEntry(arrayKeys, arrayValues) == -1) //-1 means error occured
			return false;
		else
			return true;
	}
	
	

	// This method was added by me which updates the first row in the table
	// rowIndexField is the field which will be used to search for the exact row
	// e.g updateRow("user_id", 1)
	// rowIndexField should be other than the one you are updating
	public void updateRow(String rowIndexField, String rowIndex) { // previously
																	// rowIndex
																	// was long
																	// before
																	// guid was
																	// used
		
		database.updateEntry(rowIndexField, rowIndex, arrayKeys, arrayValues);
	}
	
	public void updateAllRows()
	{
		database.update(" SET " + arrayKeys.get(0) + "='" + arrayValues.get(0)+"'");

	}

	public boolean ClearTable() {
		
		return database.clearTable();
	}

	public void deleteRow(String field, String rowIndex) 
	{
		database.removeEntry(field, rowIndex);
	}

	/**
	 * Get data from the table.
	 * 
	 * @param keys
	 *            List of columns to include in the result.
	 * @param selection
	 *            Return rows with the following string only. Null returns all
	 *            rows.
	 * @param selectionArgs
	 *            Arguments of the selection.
	 * @param groupBy
	 *            Group results by.
	 * @param having
	 *            A filter declare which row groups to include in the cursor.
	 * @param sortBy
	 *            Column to sort elements by.
	 * @param sortOption
	 *            ASC for ascending, DESC for descending.
	 * @return Returns an ArrayList<String> with the results of the selected
	 *         field.
	 */

	// to fetch all records from a single column
	public ArrayList<String> getDataBySortColumn(String[] keys,
			String selection, String[] selectionArgs, String groupBy,
			String having, String sortBy, String sortOption) {

		ArrayList<String> list = new ArrayList<String>();
		Cursor results = database.getAllEntries(keys, selection, selectionArgs,
				groupBy, having, sortBy, sortOption);
		while (results.moveToNext())
			list.add(results.getString(results.getColumnIndex(sortBy)));
		results.close();
		return list;

	}


	// To fetch all records for multiple columns
	public ArrayList<ArrayList<String>> getData(String[] keys,
			String selection, String[] selectionArgs, String groupBy,
			String having, String sortBy, String sortOption) {

		ArrayList<String> childList = null;

		ArrayList<ArrayList<String>> parentList = new ArrayList<ArrayList<String>>();

		Cursor results = database.getAllEntries(keys, selection, selectionArgs,
				groupBy, having, sortBy, sortOption);

		while (results.moveToNext()) {
			childList = new ArrayList<String>();

			for (String colName : results.getColumnNames()) {
				childList
						.add(results.getString(results.getColumnIndex(colName)));

			}

			parentList.add(childList);
		}
		results.close();

		return parentList;
	}

	// fetch selective record from a single column e.g select id from tablename
	// where fieldToSearch = valueToSearch;
	public ArrayList<String> getSelectiveDataBySortColumn(String[] keys,
			String selection, String[] selectionArgs, String groupBy,
			String having, String sortBy, String sortOption) {

		ArrayList<String> list = new ArrayList<String>();
		Cursor results = database.getAllEntries(keys, selection, selectionArgs,
				groupBy, having, sortBy, sortOption);
		//while (results.moveToNext())
			//list.add(results.getString(results.getColumnIndex(sortBy)));
		
		while (results.moveToNext())
		{

			for (String colName : results.getColumnNames()) 
			{
				list.add(results.getString(results.getColumnIndex(colName)));
	
			}
		}
		
		results.close();
		return list;

	}

	/*
	 * public ArrayList<ArrayList<String>> getSelectiveRowData(String[] keys,
	 * String fieldToSearch, String operationToPerform, String valueToSearch,
	 * String[] selectionArgs, String groupBy, String having, String sortBy,
	 * String sortOption) {
	 * 
	 * ArrayList<String> childList = null;
	 * 
	 * ArrayList<ArrayList<String>> parentList = new
	 * ArrayList<ArrayList<String>>();
	 * 
	 * Cursor results = database.getAllEntries(keys, fieldToSearch +
	 * operationToPerform + "'" + valueToSearch + "'" + " AND IsDeleted = '0'",
	 * selectionArgs, groupBy, having, sortBy, sortOption);
	 * 
	 * while (results.moveToNext()) { childList = new ArrayList<String>();
	 * 
	 * for (String colName : results.getColumnNames()) { childList
	 * .add(results.getString(results.getColumnIndex(colName)));
	 * 
	 * }
	 * 
	 * parentList.add(childList); } results.close();
	 * 
	 * return parentList; }
	 */

	

	
	
	// for custom Where commands to execute e.g using between in where etc
	public ArrayList<ArrayList<String>> getSelectiveCustomRowData(
			String[] keys, String whereCommand, String[] selectionArgs,
			String groupBy, String having, String sortBy, String sortOption) {

		ArrayList<String> childList = null;

		ArrayList<ArrayList<String>> parentList = new ArrayList<ArrayList<String>>();

		Cursor results = database.getAllEntries(keys, whereCommand,
				selectionArgs, groupBy, having, sortBy, sortOption);

		while (results.moveToNext()) {
			childList = new ArrayList<String>();

			for (String colName : results.getColumnNames()) {
				childList.add(results.getString(results.getColumnIndex(colName)));

			}

			parentList.add(childList);
		}
		results.close();

		return parentList;
	}

	/**
	 * Destroy the reporter.
	 * 
	 * @throws Throwable
	 */
	public void destroy() throws Throwable {
		database.close();
	}

	public void deleteDBFile()
	{
		String DB_PATH = "/data/data/tum.praktikum.itestra/databases";
	   	deleteFiles(DB_PATH);
	   
	}
	
	public static void deleteFiles(String path) {

        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }
    }
	
	// Copying database from Assets folder and creating a folder 'databases' in
	// /data/data/com.proanglerassist.proanglerassist/ if not exists
	public void importDBFromAssets() throws IOException {
		//String DB_PATH = "/data/data/com.proanglerassist.proanglerassist/databases/";

		// /////using assets//////
		

		// Open your local db as the input stream
		InputStream myInput = appContext.getAssets().open(DATABASE_NAME);
		
		Log.i("Input from Assets : ", myInput.toString());

		// ///using raw folder///////
		/*
		 * Log.v("myInput", "using raw");
		 * 
		 * String DB_NAME = "proanglerassist.db"; InputStream myInput =
		 * appContext.getResources().openRawResource(R.raw.proanglerassist);
		 * 
		 * Log.v("myInput", myInput.toString());
		 */

		// /fetching path of package
		PackageManager m = appContext.getPackageManager();// getPackageManager();
		String s = appContext.getPackageName();
		PackageInfo p = null;

		try {
			p = m.getPackageInfo(s, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s = p.applicationInfo.dataDir;

		Log.i(" db package path", s);

		// /creating databases folder in pacakage
		String dirPath = s + File.separator + "databases";
		File projDir = new File(dirPath);

		if (!projDir.exists()) {

			projDir.mkdirs();
			
		
			Log.i("dir path exists now after creation", dirPath);

			// Path to the just created empty db
			String outFileName = dirPath + File.separator + DATABASE_NAME;

			// Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);
			Log.i("myOutput", myOutput.toString());

			// transfer bytes from the inputfile to the outputfile
			// byte[] buffer = new byte[3072];//originally it was 1024

			byte[] buffer = new byte[myInput.available()];
			// byte[] buffer = new byte[2048];
			int length;

			// Log.v("buffer ", String.valueOf(buffer));

			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}

			Log.i("myOutput after write", myOutput.toString());

			// Close the streams
			myOutput.flush();
			myOutput.close();
		
		}
		else
		{
			Log.i("dir path already exists", dirPath);

		
		
		}

		myInput.close();

	}

}
