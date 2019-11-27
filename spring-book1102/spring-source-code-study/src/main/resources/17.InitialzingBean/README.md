InitialzingBean(初始化中的Bean)

```


public interface InitializingBean {


	void afterPropertiesSet() throws Exception;

}


```

> 1.是个接口，就一个afterPropertiesSet方法
  2.使用方式，所有容器的bean均可以实现它
  3.实现它之后，在bean创建为字段初始化后，先调用beanPostProcessor的前置方法，然后就执行afterPropertiesSet对bean的属性等作出调整
  4.具体demo参考BeanPostProcessor
