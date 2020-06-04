package com.example.myapplication;

import android.os.AsyncTask;

public class TaskRequestString extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            return RequestHandler.sendQuery(strings[0], null);
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String s) {
        if(s!=null){

        }
    }

}