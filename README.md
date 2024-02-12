# JavaHelloWorld
自己写的一些测试Dmeo.我的文章中相关的测试也会放到这里


## 2023年05月30日

- 添加 shell bash API的java封装. 封装成一个标准的rest api的标准格式. 就像调用一个本地方法一样. 


![wecom-temp-71d4dca0bbbcd0b997c73961e739941a.png](https://upload.firfor.cn/images/README/1704279529025.png?imageView2/0/interlace/1/q/50|imageslim)



### [demo-cache](demo-cache)

用于验证`caffeine` 的使用的一些基本特征. 如果在使用它前没有把这些基本的特征搞清楚. 可能会出现一些非预期的低级错误. 

比如:

- 加载了一个caffeine的异步缓存. 如果这个缓存的future的加载成功需要较长的时间,且此时间超过了缓存过期时间. 那这个future在完成时是已经过期状态.还是说从完成时刻开始算有效时间.
- 一个key的asyncLoad方法正在执行. 相同的key的调用方法是被阻塞还是继续触发load? 
  - 答案: 阻塞等第一个load完成.  这个与guavaCache的行为一致. 