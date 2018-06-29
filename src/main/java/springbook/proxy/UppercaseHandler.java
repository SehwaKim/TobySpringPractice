package springbook.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {
    Hello target;

    public UppercaseHandler(Hello target){
        this.target = target; // dynamic proxy 로 부터 전달받은 요청을 다시 타깃 오브젝트에 위임해야하기 때문에
                                // 타깃 오브젝트를 주입받아 둔다.
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        if(ret instanceof String){
            return ((String)ret).toUpperCase();
        }else {
            return ret;
        }
    }
}
