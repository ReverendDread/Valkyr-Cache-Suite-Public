//import static org.junit.Assert.*;
//
//import java.lang.reflect.Field;
//
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.experimental.theories.suppliers.TestedOn;
//
//import javafx.application.Application;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//import lombok.extern.slf4j.Slf4j;
//import store.utilities.ReflectionUtils;
//
///**
// *
// */
//
///**
// * @author ReverendDread
// * Sep 22, 2019
// */
//@Slf4j
//public class ReflectionValueTest {
//
//	@BeforeClass
//	public static void initJFX() {
//	    Thread t = new Thread("JavaFX Init Thread") {
//	        public void run() {
//	            Application.launch(AsNonApp.class, new String[0]);
//	        }
//	    };
//	    t.setDaemon(true);
//	    t.start();
//	}
//
//	@Test
//	public void test() {
//		PublicTestClass testClass = new PublicTestClass();
//
//		String newValue = "placeholder";
//		assertTrue(testClass.someString.equals("apple"));
//		log.info("value of testClass is {}", testClass.someString);
//		for(Field field : testClass.getClass().getFields()) {
//			log.info("Field name is {}", field.getName());
//			ReflectionUtils.setValue(testClass, field, new TextField(newValue));
//		}
//		log.info("value of testClass is {}", testClass.someString);
//		assertTrue(testClass.someString.equals(newValue));
//		//ReflectionUtils.setValue(originalObject, field, value);
//	}
//
//	@Test
//	public void dude() {
//
//		TestByteClass testClass = new TestByteClass();
//		ComboBox<String> box = new ComboBox<String>();
//		box.getItems().add("10");
//
//		assertTrue(testClass.dudes[0] == 50);
//
//		for(Field field : testClass.getClass().getFields()) {
//			log.info("Field name is {}", field.getName());
//			ReflectionUtils.setValue(testClass, field, box);
//		}
//
//
//		assertTrue(testClass.dudes[0] == 10);
//
//	}
//
//
//	class PrivateTestClass {
//		private String someString = "testString1";
//	}
//
//	class PublicTestClass {
//		public String someString = "testString2";
//	}
//
//
//	class TestByteClass {
//
//		public byte[] dudes = {50};
//
//	}
//
//	public static class AsNonApp extends Application {
//
//	    @Override
//	    public void start(Stage primaryStage) throws Exception {
//	        // noop
//	    }
//	}
//
//}
