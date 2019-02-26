package ${pkg}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${pkg}.core.ext.AbstractBaseService;
import ${pkg}.core.ext.BaseDao;
import ${pkg}.dao.I${entityName}Dao;
import ${pkg}.entity.biz.${entityName};
import ${pkg}.service.I${entityName}Service;


/**
 * @author lifetime
 *
 */
@Service
public class ${entityName}Service extends AbstractBaseService<${entityName}> implements I${entityName}Service{
	@Autowired
	private I${entityName}Dao dao;

	@Override
	public BaseDao<${entityName}> getDao() {
		return dao;
	}
	
	@Override
	public String add(${entityName} t) {
		return super.add(t);
	}

}