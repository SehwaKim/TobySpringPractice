import org.junit.Test;
import springbook.proxy.Hello;
import springbook.proxy.HelloTarget;
import springbook.proxy.HelloUppercase;
import springbook.proxy.UppercaseHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReflectionTest {
    @Test
    public void invokeMethod() throws Exception {
        String name = "Spring";

        Method lengthMethod = String.class.getMethod("length");
        int length = (int) lengthMethod.invoke(name);

        assertThat(name.length(), is(length));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat(charAtMethod.invoke(name, 0), is('S'));
    }

    @Test
    public void simpleProxy(){
//        Hello hello = new HelloTarget();
        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertThat(proxiedHello.sayHello("Sehwa"), is("HELLO SEHWA"));
        assertThat(proxiedHello.sayHi("Sehwa"), is("HI SEHWA"));
        assertThat(proxiedHello.sayThankYou("Sehwa"), is("THANK YOU SEHWA"));
    }

    @Test
    public void dynamicProxy(){
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(getClass().getClassLoader(),
                                                            new Class[]{ Hello.class },
                                                            new UppercaseHandler(new HelloTarget()));

        assertThat(proxiedHello.sayHello("Sehwa"), is("HELLO SEHWA"));
        assertThat(proxiedHello.sayHi("Sehwa"), is("HI SEHWA"));
        assertThat(proxiedHello.sayThankYou("Sehwa"), is("THANK YOU SEHWA"));
    }
}
