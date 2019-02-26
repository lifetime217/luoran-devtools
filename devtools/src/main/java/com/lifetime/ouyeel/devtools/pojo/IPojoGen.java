package com.lifetime.ouyeel.devtools.pojo;

import com.lifetime.ouyeel.devtools.model.TableInfo;

/**
 * @author lifetime
 *
 */
public interface IPojoGen {
	public String gen(TableInfo tinfo);
	public String genAllFile(String projectDir,String pkg,TableInfo tinfo) throws Exception;
	
	public String type();
}
