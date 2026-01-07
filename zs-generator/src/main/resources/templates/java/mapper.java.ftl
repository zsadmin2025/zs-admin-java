package ${packageName}.${moduleName}.${businessName}.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import ${packageName}.${moduleName}.${businessName}.domain.entity.${ClassName}Entity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * ${table.comment!} Mapper 接口
 * </p>
 *
 * @author ${author}
 * {@code @date} ${date}
 */
@Mapper
public interface ${ClassName}Mapper extends DataPermissionMapper<${ClassName}Entity> {

}
