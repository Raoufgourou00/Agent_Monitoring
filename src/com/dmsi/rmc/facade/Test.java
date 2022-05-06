package com.dmsi.rmc.facade;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Connection conn = new Connection("localhost",8080,"/saveServer",1);
		conn.collectData();
		System.out.println(conn.toJSONObject().toString());
		
		
	}

}
