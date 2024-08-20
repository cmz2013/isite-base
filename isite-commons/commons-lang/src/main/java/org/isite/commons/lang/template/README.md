Thymeleaf
        
    Thymeleaf是个XML/XHTML/HTML5模板引擎，模板必须符合xml规范。
    Thymeleaf的主要目标是提供一种可被浏览器正确显示的、格式良好的模板创建方式，因此也可以用作静态建模。

Thymeleaf优点

    1、thymeleaf由于使用了标签属性做为语法，模版页面直接用浏览器渲染，浏览器可以直接打开模板文件，使得前端和后端可以并行开发
    2、thymeleaf适合做服务器端渲染，通过服务器端模版渲染，客户端收到的是在服务器端根据模版渲染后的html信息
    3、thymeleaf可以支持前段浏览器渲染

FreeMarker

    FreeMarker是基于模板来生成文本输出。它不仅可以用作表现层的实现技术，而且还可以用于生成XML，JSP或Java 等。

FreeMarker优点

    1、性能不错
    2、内置大量常用功能，使用非常方便
    3、支持表达式语言
    4、支持宏定义（类似jsp标签）
    5、类似于jsp，学习成本低，符合以前使用jsp的习惯

比较

    从写代码的角度看，freemarker更习惯于我们的思维。
    从前后分离开发的角度看thymeleaf更合适，springboot官方推荐方案，值的绑定都是基于html的dom元素属性的，适合前后联调。