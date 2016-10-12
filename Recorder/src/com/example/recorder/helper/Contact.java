package com.example.recorder.helper;


public class Contact {
    
    //private variables
    int _id;
    //bien luu so dien thoai
    String _name;
    //bien luu note
    String _phone_number;
    //bien luu trang thai (0=recorder booinh thuong; 1=auto save; 2= don't recorder)
    int _status;
     
    // Empty constructor
    public Contact(){
         
    }
    // constructor
    public Contact(int id, String name, String _phone_number, int status){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this._status=status;
    }
     
    // constructor
    public Contact(String name, String _phone_number, int status){
        this._name = name;
        this._phone_number = _phone_number;
        this._status = status;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
    
    public int getStatus()
    {
    	return this._status;
    }
    
    public void setStatus(int status)
    {
    	this._status=status;
    }
     
    // getting name
    public String getName(){
        return this._name;
    }
     
    // setting name
    public void setName(String name){
        this._name = name;
    }
     
    // getting note
    public String getPhoneNumber(){
        return this._phone_number;
    }
     
    // setting note
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
}
