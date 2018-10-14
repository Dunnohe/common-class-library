package common.guava.base;

import com.google.common.base.Optional;

public class CharMatcherDemo {

    public static void main(String[] args) {
        String nullStr = null;
        String valueStr = "123";
        //创建一个optional实例,当valueStr为null会抛空指针异常。
        Optional optional = Optional.of(valueStr);
        //创建引用缺失的Optional实例
        Optional<Object> absent = Optional.absent();


    }
}
