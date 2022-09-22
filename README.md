```
The purpose of this framework is to help developers better build Android components
```

**I.Necessary Configuration**
1. Adding dependencies and configurations in module build.gradle
 ```
 dependencies {
  ...
  implementation 'com.github.zxbear.ad_kbase:lib_base:v1.0.3'
}
 ```
 **II.Building the MVP Model**
 1. Add moduleName for in module build.gradle
 ```
 android {
   defaultConfig {
       ... ...
       javaCompileOptions{
           annotationProcessorOptions{
               arguments = [ moduleName : project.getName() ]
           }
       }
   }     
}
 ```
 2. Adding dependencies and configurations in module build.gradle
 ```
 dependencies {
  ...
  annotationProcessor 'com.github.zxbear.ad_kbase:mvp_compiler:v1.0.3'
}
 ```
 **III.use MVP module** 
1. annotation in you new class
```
@MvpAct(onCreate = PARAS.CREATE , path = "com.zxbear.testbase")
public class MainActivity {
 ...
}
```
2. Automatically build directory instructions
```
module
 --src
   --main
     |--java
     |  |--[defaule pack or you setting path]
     |  |  |--constract
     |  |  |  |--MainConstract.java
     |  |  |--presenter 
     |  |  |  |--MainPresenter.java
     |  |  |--MainActivity.java
     |--res
     |  |--layout
     |    --activity_main_layout.xml
     |--AndroidManifest.xml 
       --{<activity name:".MainActivity />"}
```

 
