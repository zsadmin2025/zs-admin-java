package com.zs.common.mp.interceptor;

import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.zs.common.mp.handler.MyDataPermissionHandler;
import jakarta.validation.constraints.NotNull;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 数据权限拦截器
 */
public class MyDataPermissionInterceptor extends DataPermissionInterceptor implements InnerInterceptor {



    private MyDataPermissionHandler myDataPermissionHandler;

    public void setDataPermissionHandler(final MyDataPermissionHandler dataPermissionHandler) {
        this.myDataPermissionHandler = dataPermissionHandler;
    }


    public void beforeQuery(Executor executor, @NotNull MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        if (!InterceptorIgnoreHelper.willIgnoreDataPermission(ms.getId())) {
            PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
            mpBs.sql(this.parserSingle(mpBs.sql(), ms.getId()));
        }
    }

    protected void processSelect(@NotNull Select select, int index, String sql, Object obj) {
        if(this.myDataPermissionHandler != null){
            if (this.myDataPermissionHandler instanceof MultiDataPermissionHandler) {
                String whereSegment = (String) obj;
                this.processSelectBody(select, whereSegment);
                List<WithItem> selectBodyList = select.getWithItemsList();
                if (!CollectionUtils.isEmpty(selectBodyList)) {
                    selectBodyList.forEach((withItem) -> this.processSelectBody(withItem, whereSegment));
                }
            } else if (select instanceof PlainSelect) {
                this.setWhere((PlainSelect) select, (String) obj);
            } else if (select instanceof SetOperationList setOperationList) {
                List<Select> selectBodyList = setOperationList.getSelects();
                selectBodyList.forEach((s) -> this.setWhere((PlainSelect) s, (String) obj));
            }
        }
    }

    protected void setWhere(@NotNull PlainSelect plainSelect, @NotNull String whereSegment) {
        if(this.myDataPermissionHandler != null){
            Expression sqlSegment = this.myDataPermissionHandler.getSqlSegment(plainSelect, whereSegment);
            if (null != sqlSegment) {
                plainSelect.setWhere(sqlSegment);
            }
        }
    }
}
