package com.example.demo;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
@MapperScan(basePackages="com.example.board.mapper")
public class ExcelDownApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExcelDownApplication.class, args);
	}
	
	// SqlSessionFactory 설정
	@Bean
	public SqlSessionFactory sqlSessoinFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		
		sessionFactory.setDataSource(dataSource);
		return sessionFactory.getObject();
	}

}
