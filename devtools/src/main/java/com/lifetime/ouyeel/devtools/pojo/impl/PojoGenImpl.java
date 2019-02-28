package com.lifetime.ouyeel.devtools.pojo.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import com.lifetime.ouyeel.devtools.model.TableInfo;
import com.lifetime.ouyeel.devtools.pojo.IPojoGen;
import com.lifetime.ouyeel.devtools.util.SqlMappingJava;

/**
 * @author lifetime
 *
 */
public class PojoGenImpl implements IPojoGen {
	private static final char SignChar = '_';
	private GroupTemplate gt;

	public PojoGenImpl() {
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader(
				"/com/lifetime/ouyeel/devtools/pojo/impl/");
		try {
			Configuration cfg = Configuration.defaultConfiguration();
			gt = new GroupTemplate(resourceLoader, cfg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String genTemplate(String tmplName, String pkg, String entityName) {
		Template t = gt.getTemplate(tmplName);
		t.binding("entityName", entityName);
		t.binding("pkg", pkg);
		return t.render();
	}

	@Override
	public String genAllFile(String projectDir, String pkg, TableInfo tinfo) throws Exception {
		String entityName = convertTableName(tinfo.getName());
		StringBuffer files = new StringBuffer();
		files.append(genFile(projectDir + "/entity/biz/" + entityName + ".java", genEntity(tinfo, pkg)));
		files.append("\n").append(genService(projectDir, pkg, entityName));
		files.append("\n").append(genDao(projectDir, pkg, entityName));
		files.append("\n").append(genAction(projectDir, pkg, entityName));
		return files.toString();
	}

	private String genService(String projectDir, String pkg, String entityName) throws Exception {
		String iserviceString = genTemplate("i_service.tpl", pkg, entityName);
		String file = projectDir + "/service/I" + entityName + "Service.java";
		String m1 = genFile(file, iserviceString);

		iserviceString = genTemplate("service_impl.tpl", pkg, entityName);
		String file2 = projectDir + "/service/impl/" + entityName + "Service.java";
		String m2 = genFile(file2, iserviceString);
		return m1 + "\n" + m2;
	}

	private String genDao(String projectDir, String pkg, String entityName) throws Exception {
		String iserviceString = genTemplate("dao.tpl", pkg, entityName);
		String file = projectDir + "/dao/I" + entityName + "Dao.java";
		String msg = genFile(file, iserviceString);
		return msg;
	}

	private String genAction(String projectDir, String pkg, String entityName) throws Exception {
		String iserviceString = genTemplate("action.tpl", pkg, entityName);
		String file = projectDir + "/action/api/" + entityName + "Action.java";
		String msg = genFile(file, iserviceString);
		return msg;
	}

	private String genFile(String file, String content) throws Exception {
		File f = new File(file);
		if (!f.exists()) {
			if (!f.createNewFile()) {
				return ("文件自动创建失败： " + f.getName());
			}
		} else {
			return ("文件已经存在： " + f.getName());
		}
		if (f.canWrite()) {
			FileWriter fw = new FileWriter(f);
			fw.append(content);
			fw.close();
		} else {
			return ("文件写入失败，存在被占用的可能： " + f.getName());
		}
		return "成功：  " + file;
	}

	public String gen(TableInfo tinfo) {
		return genEntity(tinfo, null);
	}

	protected String genEntity(TableInfo tinfo, String pkg) {
		StringBuilder sb = new StringBuilder();
		sb.append("package " + pkg + ".entity.biz;\n\n");
		sb.append("import java.io.Serializable;\n");
		sb.append("import java.math.BigDecimal;\n\n");
		sb.append("import java.util.Date;\n\n");
		
		sb.append("import org.beetl.sql.core.annotatoin.AssignID;\n");
		sb.append("import org.beetl.sql.core.annotatoin.AutoID;\n");
		sb.append("import com.luoran.zzbird.core.BaseInfo;\n\n");

		sb.append(genTableRemark(tinfo.getRemark()));
		sb.append("\npublic class ");
		sb.append(convertTableName(tinfo.getName())).append(" extends BaseInfo implements Serializable {\n");
		sb.append("\n\tprivate static final long serialVersionUID = 1L;\n\n");

		sb.append("\tpublic ").append(convertTableName(tinfo.getName())).append("() {\n\t}\n");

		tinfo.getFields().forEach(item -> {
			String type = SqlMappingJava.get(item.getType());
			String field = convertFieldName(item.getName());
			String upField = convertTableName(item.getName());
			sb.append(genFieldRemark(item.getRemark()));
			if(item.isPri()){
				if("int".equalsIgnoreCase(item.getType())){
					sb.append("\n\t@AutoID");
				}else if("varchar".equalsIgnoreCase(item.getType())){
					sb.append("\n\t@AssignID(\"uuid\")");
				}
			}
			sb.append("\n\tpublic ").append(type).append(" get").append(upField).append("() {\n");
			sb.append("\t\treturn get" + type + "(\"" + field + "\");\n\t}\n");

			sb.append("\n\tpublic void set" + upField + "(" + type + " " + field + ") {\n");
			sb.append("\t\tset(\"" + field + "\", " + field + ");\n\t}\n");
		});

		sb.append("\n}");
		return sb.toString();
	}

	public String genFieldRemark(String remark) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\t/**\n\t * ").append(remark).append("\n\t */");
		return sb.toString();
	}

	public String genTableRemark(String remark) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n/**\n * ").append(remark).append("\n */");
		return sb.toString();
	}

	String convertTableName(String val) {
		String newVal = "";
		int lastSign = -1;
		for (int i = 0; i < val.length(); i++) {
			if (i == 0 || i == lastSign) {
				newVal += String.valueOf(val.charAt(i)).toUpperCase();
			} else {
				if (SignChar == val.charAt(i)) {
					lastSign = i + 1;
					continue;
				} else {
					newVal += String.valueOf(val.charAt(i));
				}
			}
			lastSign = -1;
		}
		return newVal;
	}

	String convertFieldName(String val) {
		String newVal = "";
		int lastSign = -1;
		for (int i = 0; i < val.length(); i++) {
			if (i == lastSign) {
				newVal += String.valueOf(val.charAt(i)).toUpperCase();
			} else {
				if (SignChar == val.charAt(i)) {
					lastSign = i + 1;
					continue;
				} else {
					newVal += String.valueOf(val.charAt(i));
				}
			}
			lastSign = -1;
		}
		return newVal;
	}

	public String type() {
		return "normal";
	}

}
