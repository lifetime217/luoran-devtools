package ${pkg}.dao;

import org.beetl.sql.core.engine.PageQuery;

import ${pkg}.core.ext.BaseDao;
import ${pkg}.entity.biz.${entityName};

/**
 * @author lifetime
 *
 */
public interface I${entityName}Dao extends BaseDao<${entityName}> {

	public void queryPage(PageQuery<${entityName}> pageQuery);

}
