package com.example.commonutil.holder;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wei.song
 * @since 2023/6/26 19:28
 */
public class DataSourceTenantContextHolder {

    public static final String DEFAULT_TENANT = "default";
    private static final TransmittableThreadLocal<String> DATASOURCE_HOLDER = new TransmittableThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "default";
        }
    };

    public DataSourceTenantContextHolder() {
    }

    public static void setDefaultTenant() {
        setCurrentTenant("default");
    }

    public static String getCurrentTenant() {
        return StringUtils.defaultIfEmpty((String) DATASOURCE_HOLDER.get(), "default");
    }

    public static void setCurrentTenant(String tenant) {
        DATASOURCE_HOLDER.set(StringUtils.defaultIfEmpty(tenant, "default"));
    }

}
