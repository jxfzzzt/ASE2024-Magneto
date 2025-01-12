package com.xiaoleilu.ucloud.udb;

/**
 * DB类型id，mysql/mongodb按版本细分各有一个id 
 * @author Looly
 *
 */
public enum DBTypeId {
	mysql5_6("mysql-5.6"), 
	mysql5_5("mysql-5.5"), 
	mysql5_1("mysql-5.1"), 
	percona5_6("percona-5.6"), 
	percona5_5("percona-5.5"), 
	mongodb2_4("mongodb-2.4"), 
	mongodb2_6("mongodb-2.6");

	private String value;

	private DBTypeId(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
