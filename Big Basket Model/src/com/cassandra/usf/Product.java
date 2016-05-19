package com.cassandra.usf;

public class Product {

     public String pid;
	 public String pname;
	 public String pdesc;
	 public String pcost;
	 
	 public Product(String spdtid, String spdtname, 
			 String spdtdesc, String sprice) {
		 pid = spdtid;
		 pname = spdtname;
		 pdesc = spdtdesc;
		 pcost = sprice;
		
	 }
	 
	 public String getpdtid(){
		return pid;		 
	 }
	 
	 public String getpdtname(){
			return pname;		 
	}
	 public String getpdtdesc(){
			return pdesc;		 
	}
	 public String getprice(){
			return pcost;		
	}
	 
}
