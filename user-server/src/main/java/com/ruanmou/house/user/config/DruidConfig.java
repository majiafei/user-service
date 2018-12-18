package com.ruanmou.house.user.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * durid的数据库连接池的配置
 * @ProjectName: house
 * @Package: com.runmou.house.server.houseserver.config
 * @ClassName: DruidConfig
 * @Author: majiafei
 * @Description:
 * @Date: 2018/12/17 14:44
 */
@SpringBootConfiguration
public class DruidConfig {

    /**
     * 定制durid数据源
     * @return
     */
    @ConfigurationProperties(prefix = "spring.druid")
    @Bean
    public DruidDataSource druidDataSource(StatFilter statFilter) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setProxyFilters(Lists.newArrayList(statFilter));

        return druidDataSource;
    }

    /**
     * 构建statFilter
     * @return
     */
    @Bean
    public StatFilter statFilter() {
        StatFilter statFilter = new StatFilter();
        // 超过3秒的sql为慢查询
        statFilter.setSlowSqlMillis(3000);
        statFilter.setMergeSql(true);
        statFilter.setLogSlowSql(true);

        return statFilter;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new StatViewServlet(), "/ddd/*");
    }

}
