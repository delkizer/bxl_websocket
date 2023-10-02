package net.spotv.smart_alarm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.spotv.smartalarm.Application;


@SpringBootTest ( classes = Application.class )
class ApplicationTests {

	@Test
	void contextLoads() {
		System.out.println( "this"  );
	}

}
