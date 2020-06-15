package com.ftl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateUtils {
    private static String driverClass = "com.mysql.jdbc.Driver";

    private static String connectionURL = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=utf-8&useSSL=false";

    private static String userName="root";

    private static String passwd = "123456";

    private static String packetageDAO = "com.ftl.dao";

    private static String packetagePojo = "com.ftl.vo";


    public static void main(String[] args) {
        // 第二个参数穿入null代表将库下所有的表都生成相应文件，传入具体表明可生成具体表对应的文件
        getData(getConnection(),"student");
    }

    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName(driverClass); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(connectionURL, userName, passwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;

    }

    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == '_') {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    public static void getData(Connection conn, String tableName){
        try {
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            tableName = tableName != null ? tableName : "%";
            ResultSet colRet = databaseMetaData.getTables(null,   "%",   tableName, new String[] {"TABLE"});
            while(colRet.next()) {
                Map<String, Object> dataMap = new HashMap();
                String name = colRet.getString("TABLE_NAME");
                String camelCase = underlineToCamel(name);
                String className =convert(camelCase);
                dataMap = getColumnsData(databaseMetaData, dataMap, name,camelCase);
                dataMap.put("tableName",name);
                dataMap.put("entityName", className);
                dataMap.put("entityNameLow", camelCase);
                dataMap.put("entityType",packetagePojo+"."+className);
                dataMap.put("packetageDAO",packetageDAO);
                dataMap.put("packetagePojo",packetagePojo);

                FreeMakerToXML.createWord(dataMap,"mapper/"+className+"Mapper.xml", FreeMakerToXML.xmlTempPath);
                FreeMakerToXML.createWord(dataMap,"dao/"+className+"Dao.java", FreeMakerToXML.daoTempPath);
                FreeMakerToXML.createWord(dataMap,"vo/"+className+".java", FreeMakerToXML.pojoTempPath);
            }
        }catch (SQLException e){
            e.getErrorCode();
        }
    }

    private static Map<String, Object> getColumnsData(DatabaseMetaData databaseMetaData, Map<String, Object> dataMap, String name,String camelCase) throws SQLException {
        ResultSet columns = databaseMetaData.getColumns(null,"%",  name,"%");
        List<Data> dataList = new ArrayList<>();
        List<DataStrute> dataStruteList = new ArrayList<>();
        while(columns.next()) {
            String column = columns.getString("COLUMN_NAME");
            Data data =  new Data();
            data.setNativeColumn(column);
            String camelClonmn = underlineToCamel(column);
            data.setConvertColumn(camelClonmn);
            dataList.add(data);

            String columnType = columns.getString("TYPE_NAME");
            String convertType = getJavaType(columnType);
            DataStrute dataStrute = new DataStrute();
            dataStrute.setColumn(underlineToCamel(column));
            dataStrute.setpColumn(convert(camelClonmn));
            dataStrute.setType(convertType);
            dataStruteList.add(dataStrute);
            dataMap.put("columnList",dataList);
            dataMap.put("dataStrutes",dataStruteList);
        }
        return dataMap;
    }

    private static String getJavaType(String columnType) {
        String convertType = null;
        switch (columnType){
            case "BIGINT" :
                convertType = "Long";
                break;
            case "VARCHAR":
                convertType = "String";
                break;
            case "TIMESTAMP":
                convertType = "Date";
                break;
            case "TINYINT":
            case "INT":
                convertType = "Integer";
                break;
            case "DECIMAL":
                convertType = "Double";
                break;
            default:
                convertType = "String";
        }
        return convertType;
    }

    private static String convert(String nativeString){
        String camelCase = underlineToCamel(nativeString);
        char[] cs=camelCase.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);
    }
}
