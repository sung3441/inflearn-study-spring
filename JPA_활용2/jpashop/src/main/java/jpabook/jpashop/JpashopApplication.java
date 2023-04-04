package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.hibernate.Hibernate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {
	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	/**
	 * entity 그대로 반환 시 LAZY 설정이 되어 있으면 proxy 객체를 반환함
	 * proxy 객체를 json 형태로 변환 시키기 위해 빈 등록
	 */
	@Bean
	Hibernate5Module hibernate5Module() {
		return new Hibernate5Module();

		// LAZY 로딩 설정이 되어 있는 필드를 강제로 로딩해서 가져옴
//		Hibernate5Module hibernate5Module = new Hibernate5Module();
//		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
//		return hibernate5Module;
	}
}
