分布式锁使用说明
```
1）@Synchronized 支持接口申明式加锁，根据接口参数动态实例化key，自动释放锁
2）Locksmith 支持语句块的编程式加锁，须要手动释放锁（@see SyncAspect#doBefore）
3）项目必须引用和配置spring-boot-starter-data-redis，分布式并发锁才能正常工作
```
允许短时间的忙等待
```
1)如果长时间的排队等待锁，可以在前端实现：前端页面周期循环尝试请求接口，用户随时可以取消排队等待。
如果后端锁匠负责排队处理，很容易导致接口请求超时异常；而且，长时间的占用和浪费请求线程资源，会降低服务器的并发处理能力，即吞吐率。

2)短时间的忙等待需要配置，默认不等待，即并发冲突时立即中断请求
```
锁与事务同时使用导致锁失效的问题
```
    @Component
    public class OtherService {
    
        @Transactional
        public void execute(int id) {
            //......
            dataService.update(id);
            //......
        }
    }
    
    @Service
    public class DataService {
    
        @Synchronized(locks = { @Lock(name = "demon:data:${arg0}", keys = {"#id"}) })
        public void update(id) {
            //更新数据库操作 ......
        }
    }

由于spring的aop，会在execute方法之前开启事务，当锁住的代码执行完成后再提交事务，锁住的代码块执行是在事务之内执行的，
可以推断在代码块执行完时，事务还未提交，锁已经被释放，此时其他线程拿到锁之后进行锁住的代码块，读取的库存数据不是最新的。

解决方法：
不在事务里边加锁，在还没有开事务之前就加锁，那么就可以保证线程的安全性，从而不会出现脏读和数据不一致性等情况
    
    @Component
    public class OtherService {
    
        @Synchronized(locks = { @Lock(name = "demon:data:${arg0}", keys = {"#id"}) })
        public void execute(int id) {
            //......
            dataService.update(id);
            //......
        }
    }
    
    @Service
    public class DataService {
    
        @Transactional
        public void update(id) {
            //更新数据库操作 ......
        }
    }