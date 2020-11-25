
### String的长度限制
String的构造函数中有一些支持传入length属性的，该属性使用int定义。所以理论上长度支持int的最大值，`Integer#MAX_VALUE`
即2^32-1。
```java
public String(byte bytes[], int offset, int length) {
    checkBounds(bytes, offset, length);
    this.value = StringCoding.decode(bytes, offset, length);
}
```
但这是在运行期，构造String时所能支持的最大长度。在编译器对字符串的最大长度也有限制。

**常量池对String长度的限制**
根据《Java虚拟机规范》中第4.4章节常量池的定义，CONSTANT_String_info 用于表示 java.lang.String 
类型的常量对象，格式如下：
```text
CONSTANT_String_info {
    u1 tag;
    u2 string_index;
}
```
其中string_index标识指向CONSTANT_UTF8_info字符串常量对象，该对象存储真正的字符串的值。
```text
CONSTANT_Utf8_info {
    u1 tag;
    u2 length;
    u1 bytes[length];
}
```
其中，length则指明了 bytes[]数组的长度，其类型为u2，
通过翻阅《规范》，我们可以获悉。u2表示两个字节的无符号数，那么1个字节有8位，2个字节就有16位。
16位无符号数可表示的最大值位2^16 - 1 = 65535。
也就是说，Class文件中常量池的格式规定了，其字符串常量的长度不能超过65535。

#### 总结
在编译期，直接使用`String s = ""`定义字符串，常量池格式规定了字符串长度最大位65534
在运行期生成的字符串长度不能超过int的最大值。

参考[《Java虚拟机原理图解》 1.2.2、Class文件中的常量池详解（上）](https://blog.csdn.net/luanlouis/article/details/39960815)