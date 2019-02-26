package com.lifetime.ouyeel.devtools.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.lifetime.ouyeel.devtools.model.FieldInfo;
import com.lifetime.ouyeel.devtools.model.TableInfo;

/**
 * @author lifetime
 *
 */
public class DbUtil {

	public static List<TableInfo> initAllTables(Connection conn, String dbName) {
		List<TableInfo> tables = new ArrayList<>();
		Statement stt = null;
		try {
			stt = conn.createStatement();
			ResultSet res = stt.executeQuery("SELECT TABLE_NAME,TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + dbName + "'");
			while (res.next()) {
				TableInfo tinfo = new TableInfo(res.getString("TABLE_NAME"), res.getString("TABLE_COMMENT"));
				tables.add(tinfo);
			}
			close(stt, res);
			tables.forEach(item ->{
				try {
					Statement stt2 = conn.createStatement();
					ResultSet res2 = stt2.executeQuery("select column_name,data_type,column_comment,column_key from information_schema.columns where table_schema = '" + dbName + "' and table_name = '" + item.getName() + "'");
					while (res2.next()) {
						FieldInfo finfo = new FieldInfo();
						finfo.setName(res2.getString("column_name"));
						finfo.setType(res2.getString("data_type"));
						finfo.setRemark(res2.getString("column_comment"));
						finfo.setPri("PRI".equalsIgnoreCase(res2.getString("column_key")));
						item.getFields().add(finfo);
					}
					close(stt2, res2);
				} catch (SQLException e) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tables;
	}
	
	
	public static void close(Statement stt,ResultSet res){
		if(res != null){
			try {
				res.close();
			} catch (SQLException e) {
			}
		}
		
		if(stt != null){
			try {
				stt.close();
			} catch (SQLException e) {
			}
		}
	}

}
