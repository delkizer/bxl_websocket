package net.spotv.smartalarm.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;

@Configuration
@PropertySource("classpath:/application.properties")
public class DBConfig {
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Bean
	@ConfigurationProperties( prefix = "spring.datasource.hikari" )
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	
	@Bean
	public DataSource dataSource(){
		DataSource dataSource = new HikariDataSource( hikariConfig() );
		return new DataSourceSpy(dataSource);
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory( DataSource dataSource ) throws Exception { 
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		
		bean.setDataSource(dataSource);
		bean.setMapperLocations( applicationContext.getResources( "classpath:mapper/*.xml" ) );
		bean.setTypeAliasesPackage( "net.spotv.smart_alarm" );
		
		return bean.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate( SqlSessionFactory seqSessionFactory ) {
		return new  SqlSessionTemplate(seqSessionFactory);
	}

}
