# Unicode欺骗

## 概念

**Unicode编码**

Unicode实际上是一个字符和数字之间的映射关系表，并不是实际的编码方案。后来才制定了UTF(Unicode Transformation Formats)标准，包括UTF-8、UTF-16和UTF-32等。可以理解为Unicode是一个设计图，而UTF-X是其中一种实现。

**视觉欺骗**

视觉欺骗是最常见的Unicode安全问题。视觉问题一般指几个不同的字符在某个字体下看起来较为相同。可能是字符之间一对一相似、多个字符的组合字符和一个字符相似等。





## 题目  

### [HCTF 2018]admin

随便注册一个账号user

登录之后查看源码发现提示`<!-- you are not admin -->`

![](https://gitee.com/chen-lishan/tuc/raw/master/1not admin.png)

再用admin注册，提示账号已被注册

根据提示和题目名猜测，要让我们登录admin用户就可以得到flag。

（用admin在登录页面乱输密码试了下，输入123时就登进去了！这样暴力破解也能得到flag）



在change password页面查看页面源码，可以发现泄露了源码

![](https://gitee.com/chen-lishan/tuc/raw/master/源码.png)

```
https://github.com/woadsl1234/hctf_flask/
```

在app文件夹中，查看routes.py文件：

有登录、注册、改密码、退出、edit这几个功能模块

```python
#!/usr/bin/env python
# -*- coding:utf-8 -*-

from flask import Flask, render_template, url_for, flash, request, redirect, session, make_response
from flask_login import logout_user, LoginManager, current_user, login_user
from app import app, db
from config import Config
from app.models import User
from forms import RegisterForm, LoginForm, NewpasswordForm
from twisted.words.protocols.jabber.xmpp_stringprep import nodeprep
from io import BytesIO
from code import get_verify_code

@app.route('/code')
def get_code():
    image, code = get_verify_code()
    # 图片以二进制形式写入
    buf = BytesIO()
    image.save(buf, 'jpeg')
    buf_str = buf.getvalue()
    # 把buf_str作为response返回前端，并设置首部字段
    response = make_response(buf_str)
    response.headers['Content-Type'] = 'image/gif'
    # 将验证码字符串储存在session中
    session['image'] = code
    return response

@app.route('/')
@app.route('/index')
def index():
    return render_template('index.html', title = 'hctf')

@app.route('/register', methods = ['GET', 'POST'])
def register():

    if current_user.is_authenticated:
        return redirect(url_for('index'))

    form = RegisterForm()
    if request.method == 'POST':
        name = strlower(form.username.data)
        if session.get('image').lower() != form.verify_code.data.lower():
            flash('Wrong verify code.')
            return render_template('register.html', title = 'register', form=form)
        if User.query.filter_by(username = name).first():
            flash('The username has been registered')
            return redirect(url_for('register'))
        user = User(username=name)
        user.set_password(form.password.data)
        db.session.add(user)
        db.session.commit()
        flash('register successful')
        return redirect(url_for('login'))
    return render_template('register.html', title = 'register', form = form)

@app.route('/login', methods = ['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('index'))

    form = LoginForm()
    if request.method == 'POST':
        name = strlower(form.username.data)
        session['name'] = name
        user = User.query.filter_by(username=name).first()
        if user is None or not user.check_password(form.password.data):
            flash('Invalid username or password')
            return redirect(url_for('login'))
        login_user(user, remember=form.remember_me.data)
        return redirect(url_for('index'))
    return render_template('login.html', title = 'login', form = form)

@app.route('/logout')
def logout():
    logout_user()
    return redirect('/index')

@app.route('/change', methods = ['GET', 'POST'])
def change():
    if not current_user.is_authenticated:
        return redirect(url_for('login'))
    form = NewpasswordForm()
    if request.method == 'POST':
        name = strlower(session['name'])
        user = User.query.filter_by(username=name).first()
        user.set_password(form.newpassword.data)
        db.session.commit()
        flash('change successful')
        return redirect(url_for('index'))
    return render_template('change.html', title = 'change', form = form)

@app.route('/edit', methods = ['GET', 'POST'])
def edit():
    if request.method == 'POST':
        
        flash('post successful')
        return redirect(url_for('index'))
    return render_template('edit.html', title = 'edit')

@app.errorhandler(404)
def page_not_found(error):
    title = unicode(error)
    message = error.description
    return render_template('errors.html', title=title, message=message)

def strlower(username):
    username = nodeprep.prepare(username)
    return username
```



#### Unicode欺骗

注册登录修改密码都用到了`strlower()`函数转换成小写。这个函数是自定义的

![](https://gitee.com/chen-lishan/tuc/raw/master/strlower.png)

`nodeprep.prepare()`函数：将大写转换成小写。但当遇见`ᴬᴰᴹᴵᴺ`时，会转换成`ADMIN`。即，对于字符`ᴬ`，调用一次函数会转换成`A`, 再调用一次会转换成`a`。这是这个函数的漏洞。

注册登录修改密码都用到了`strlower()`函数：

![](https://gitee.com/chen-lishan/tuc/raw/master/大小写.png)



![](https://gitee.com/chen-lishan/tuc/raw/master/大小写2.png)



因此，注册ᴬᴰᴹᴵᴺ用户，登录，由于在login函数里使用了一次nodeprep.prepare函数，因此我们登录上去看到的用户名为ADMIN，此时我们再修改密码，又调用了一次nodeprep.prepare函数将name转换为admin，然后我们就可以改掉admin的密码，最后利用admin账号登录即可拿到flag。





