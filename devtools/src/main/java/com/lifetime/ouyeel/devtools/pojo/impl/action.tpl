package ${pkg}.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ${pkg}.core.ext.BaseAction;
import ${pkg}.core.ext.IBaseService;
import ${pkg}.entity.biz.${entityName};
import ${pkg}.service.I${entityName}Service;

/**
 * @author lifetime
 *
 */
@Controller
@RequestMapping("${strutil.toLowerCase(entityName)}")
public class ${entityName}Action  implements BaseAction<${entityName}> {

	@Autowired
	private I${entityName}Service service;
	
	@RequestMapping
	public String index() {
		return "${strutil.toLowerCase(entityName)}";
	}

	@Override
	public IBaseService<${entityName}> getService() {
		return service;
	}

}
