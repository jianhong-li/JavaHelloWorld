package com.jianhongl.fresh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jianhongl.fresh.department.Department;
import com.jianhongl.fresh.department.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Digester digester = new Digester();
        // Do we want to use a validating parser
        digester.setValidating(false);

        // 匹配 department 节点时,创建Department对象.
        digester.addObjectCreate("department", Department.class);

        // 匹配 department 节点时,设置对象属性
        digester.addSetProperties("department");

        // 匹配 department/user 节点时, 创建 User 对象.
        digester.addObjectCreate("department/user", User.class);
        // 匹配 department/user 节点时, 设置 属性
        digester.addSetProperties("department/user");

        // 匹配 department/user 节点时, 调用 department的 addUser 方法
        // Note:
        //      1) 当 end() 方法被调用时,Digester会找到位于栈顶部的对象之后的对象.
        //      2) 调用其指定的方法.
        //      3) 同时把栈顶部对象作为参数传入.(因此没有指定参数相关的配置) , 用于设置父对象的子对象. 以在栈对象之间建立父子关系.
        digester.addSetNext("department/user", "addUser");

        // 匹配 department/extension 节点时,调用 department对象的 putExtension 方法
        // Note:
        //      1) callMethodRule 该规则用于在 end() 方法调用时. 执行栈顶部对象 的某个方法
        digester.addCallMethod("department/extension", "putExtension", 2);

        digester.addCallParam("department/extension/property-name", 0);

        digester.addCallParam("department/extension/property-value", 1);

        try {
            InputStream testXmlInputStream = Thread.currentThread().getContextClassLoader()
                                                 .getResourceAsStream("test.xml");
            Department department = (Department) digester.parse(testXmlInputStream);
            System.out.println(JSON.toJSONString(department, SerializerFeature.PrettyFormat));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

    }
}
