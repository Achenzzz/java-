package com.ftl;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;

public class FreeMakerToXML {
    private static Configuration configuration = null;

    private static Template t = null;

    public static final String xmlTempPath = "xmlTemp.ftl";

    public static final String daoTempPath = "daoTemp.ftl";

    public static final String pojoTempPath = "pojoTemp.ftl";

    static {
        try {
            configuration = new Configuration();
            configuration.setDefaultEncoding("UTF-8");
            TemplateLoader templateLoader = new FileTemplateLoader(new File("src/main/java/com/ftl/template"));
            configuration.setTemplateLoader(templateLoader);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void createWord(Map<String,Object> dataMap, String fileName, String templte){
        File outFile = new File("d:/ftl/"+fileName);  //生成文件的路径
        try(Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));) {
            t = configuration.getTemplate(templte); //文件名
            t.process(dataMap, out, ObjectWrapper.BEANS_WRAPPER);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
