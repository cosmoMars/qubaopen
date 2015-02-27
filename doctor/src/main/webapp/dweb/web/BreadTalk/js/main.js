/**
 * Created by Knight on 2015/1/23.
 */

// 金币对像
var bread6 = new Image();
bread6.src = "bread_061.png";
bread6.value = 5;
bread6.speed = 5;
var bread5 = new Image();
bread5.src = "bread_051.png";
bread5.value = 10;
bread5.speed = 8;
var bread4 = new Image();
bread4.src = "bread_04.png";
bread4.value = 15;
bread4.speed = 10;
var bread3 = new Image();
bread3.src = "bread_03.png";
bread3.value = 20;
bread3.speed = 13;
var bread2 = new Image();
bread2.src = "bread_02.png";
bread2.value = 30;
bread2.speed = 18;
var bread1 = new Image();
bread1.src = "bread_01.png";
bread1.value = 50;
bread1.speed = 25;


var bomb = new Image();
bomb.src = "banger.png";
bomb.value = 0;
bomb.speed = 10;

var heroImg = new Image();
heroImg.src = "hero.png";
var bg = new Image();
bg.src = "bg.jpg";

var breadCode=null;

// 金币类;
function Money(x, y, speed, img) {
    // 没次循环增加的像素数
    this.speed = speed;
    this.x = x;
    this.y = y;
    this.width = img.width;
    this.height = img.height;
    this.img = img;
    this.value = img.value;
}
Money.prototype = {
    draw: function (ctx) {
        ctx.drawImage(this.img, this.x, this.y);
    },
    move: function () {
        this.y += this.speed;
    }
};
// 娃娃脸
function Hero(x, y, img) {
    this.grade = 0;
    this.life = 1;
    this.time = 30000;
    this.x = x;
    this.y = y;
    this.img = img;
    this.width = img.width;
    this.height = img.height;
}
Hero.prototype = {
    draw: function (ctx) {
        ctx.drawImage(this.img, this.x, this.y);
    },
    touch: function (other) {
        if (this.x + this.width > other.x && this.x < other.x + other.width &&
            this.y + this.height > other.y && this.y < other.y + other.height) {
            this.grade += other.value;
            if(other.value==0){
                App.gameOver();
            }
            return true;
        }
        return false;
    }
};
var App = {
    // 对象
    elements: [],
    backImg: bg,
    imgs: [ bread6 , bread5 , bread4 , bread3 , bread2 , bread1 ],
    hero: null,
    // 画布
    canvas: null,
    // 绘制工具
    context: null,
    // 游戏定时器
    timer: null,
    // 人物移动定时器
    moveTimer: null,
    //最高分数
    bestScore:0,
    //对话框显示
    dialogShow:false,
    // 速度（更新间隔speed * 10）
    speed: 0,
    bombs:0,
    pause: false,
    // 绘制对象
    draw: function () {
        // 清屏
        //this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
        // 绘制背景
        this.context.drawImage(this.backImg, 0, 0);
        // 绘制娃娃脸
        this.hero.draw(this.context);
        // 绘制金币
        for (var i = 0; i < this.elements.length; i++) {
            var o = this.elements[i];
            // 清理屏幕外的对象
            if (o.x > this.canvas.width || o.x < 0 || o.y > this.canvas.height || o.y < 0) {
                this.elements.splice(i, 1);
                i--;
                this.hero.life--;
                if(o.value==0){
                    App.bombs--;
                }
            } else if (this.hero.touch(o)) {
                this.elements.splice(i, 1);
                i--;
            } else {
                o.draw(this.context);
            }
        }
        // 绘制生命值及得分
        this.context.textAlign = "left";
        this.context.font = 'normal 20px Arial';
        this.context.fillStyle = "#666";
//            this.context.fillText("生命:" + this.hero.life, 10, 30);
        this.context.fillText("分数:" + this.hero.grade, 10, 35);
        if(this.hero.time/1000>0){
            this.context.fillText("剩余时间:" + this.hero.time/1000, 10, 70);
        }else{
            this.context.fillText("剩余时间:" + "0.000", 10, 70);
        }
    },
    // 循环处理
    loop: function () {
        var me = App;
        if (me.pause) {
            return;
        }
        for (var i = 0; i < me.elements.length; i++) {
            me.elements[i].move();
        }
        var chance = Math.random() * 1000;
        // 1/10的对象添加概率
        if (chance < 100 && me.elements.length < 6) {
            var img = me.imgs[parseInt(chance % me.imgs.length)];
            var x = Math.random() * (me.canvas.width - me.imgs[parseInt(chance % me.imgs.length)].width);
            var y = 0;
            var speed = img.speed;
            var money = new Money(x, y, speed, img);
            me.addElement(money);
        }else if(chance>=100 && chance<150 && me.elements.length < 6 && me.bombs<3){
            var img = bomb;
            var x = Math.random() * (me.canvas.width - me.imgs[parseInt(chance % me.imgs.length)].width);
            var y = 0;
            var speed = img.speed;
            var money = new Money(x, y, speed, img);
            me.addElement(money);
            me.bombs++;
        }
        me.draw();
        if (
           // me.hero.life <= 0 ||
            me.hero.time<=0) {
            me.gameOver();
        }

        me.hero.time-=18;
    },
    // 开始游戏
    gameStart: function (id, speed) {
        var me = this;
        me.canvas = getId("canvas");
        me.context = getId("canvas").getContext("2d");
        me.speed = speed;
        me.hero = new Hero((me.canvas.width - heroImg.width) / 2, me.canvas.height - heroImg.height, heroImg);
        if (this.timer != null) me.gameOver();
        me.canvas.ontouchmove = function (e) {
            e = window.event || e;
            var x=e.targetTouches[0].pageX;
            //var x = e.clientX - App.canvas.offsetLeft - App.hero.width / 2;

            if (x > 0 && x < me.canvas.width - me.hero.width) {
                me.hero.x = x;
            }
            //阻止冒泡 必须加 不然move事件 不能持续生效
            e.preventDefault();
        }
        me.timer = setInterval(me.loop, me.speed * 3);
    },
    // 暂停游戏
    gamePause: function () {
        this.pause = true;
        this.context.textAlign = "center";
        this.context.fillStyle = "#f00";
        this.context.font = 'bold 50px Arial';
        this.context.fillText("暂停!", this.canvas.width / 2, this.canvas.height / 2);
        this.context.font = 'bold 20px Arial';
        this.context.fillText("请按点击屏幕开始...", this.canvas.width / 2, this.canvas.height / 2 + 40);
    },
    // 结束游戏
    gameOver: function () {
        clearInterval(this.timer);
        clearInterval(this.moveTimer);
        this.elements = [];
        this.pause = false;
        this.timer = null;
        this.bombs = 0;
        this.moveTimer=null;

        if(this.hero.grade>this.bestScore){
            this.bestScore=this.hero.grade;
        }

        this.context.fillStyle = "rgba(239,239,239,0.8)";
        var x = this.canvas.width * 0.1;
        var y = this.canvas.height * 0.3;
        var w = this.canvas.width * 0.8;
        var h = this.canvas.height *0.3;
        this.context.roundRect(x, y, w, h, 5, true);


        if(App.hero.time>=0){
            this.context.textAlign = "center";
            this.context.fillStyle = "#f00";
            this.context.font = 'bold 30px Arial';
            this.context.fillText("本次得分:"+this.hero.grade, this.canvas.width / 2, y+h/5 );

            this.context.fillStyle = "#333";
            this.context.font = 'bold 15px Arial';
            this.context.fillText("（下次注意躲避爆竹哦！） ", this.canvas.width / 2, y+h/2.8 );

            this.context.fillStyle = "#333";
            this.context.font = 'bold 20px Arial';
            this.context.fillText("最高得分:"+this.bestScore, this.canvas.width / 2, y+h/2 );

        }else{
            this.context.textAlign = "center";
            this.context.fillStyle = "#f00";
            this.context.font = 'bold 30px Arial';
            this.context.fillText("本次得分:"+this.hero.grade, this.canvas.width / 2, y+h/3.5 );

            this.context.fillStyle = "#333";
            this.context.font = 'bold 20px Arial';
            this.context.fillText("最高得分:"+this.bestScore, this.canvas.width / 2, y+h/2 );

        }

        this.context.fillStyle = "#efefef";
        x = this.canvas.width * 0.18;
        y = this.canvas.height * 0.5;
        w = this.canvas.width * 0.3;
        h = this.canvas.height *0.06;
        this.context.roundRect(x, y, w, h, 5, true);
        this.context.textAlign = "center";
        this.context.fillStyle = "#333";
        this.context.font = 'bold 20px Arial';
        this.context.fillText("再玩一次", x+w/2, y+h/1.65 );

        this.context.fillStyle = "#efefef";
        x = this.canvas.width * 0.52;
        this.context.roundRect(x, y, w, h, 5, true);

        this.context.textAlign = "center";
        this.context.fillStyle = "#333";
        this.context.font = 'bold 20px Arial';
        this.context.fillText("领取兑换码", x+w/2, y+h/1.65 );

    },
    // 添加对象
    addElement: function (o) {
        this.elements.push(o);
    }
}

window.onload = function () {
    var can = getId("canvas");
    var ctx = getId("canvas").getContext("2d");

    initWindows();


    //ctx.drawImage(bg, 0, 0);
    ctx.drawImage(heroImg, (can.width - heroImg.width) / 2, can.height - heroImg.height);
    ctx.textAlign = "center";
    ctx.fillStyle = "#f00";
    ctx.font = 'bold 20px Arial';
    ctx.fillText("请按点击屏幕开始...", can.width / 2, can.height / 2);

    document.onkeyup = function (e) {
        if (e.keyCode != 32) {
            return;
        }
        if (App.timer == null) {
            ctx.clearRect(0, 0, can.width,can.height);
            App.gameStart("canvas", 6);
        } else if (App.pause) {
            App.pause = false;
        } else {
            App.gamePause("canvas", 6);
        }
    };
    //添加事件响应
    can.addEventListener("touchstart", function(e){

        var x=e.targetTouches[0].pageX;
        var y=e.targetTouches[0].pageY;
        if (App.canvas == null) {
            ctx.clearRect(0, 0, can.width,can.height);
            App.gameStart("canvas", 6);
            return;
        }

        var bx = $(window).width()* 0.18;
        var by = $(window).height() * 0.5;
        var bw = $(window).width() * 0.3;
        var bh = $(window).height() *0.06;
        var bx2 = $(window).width()* 0.52;

        if (App.timer == null) {
            if( x>bx && x<bx+bw && y>by && y<by+bh ){
                App.gameStart("canvas", 6);
                return;
            }else if(x>bx2 && x<bx2+bw && y>by && y<by+bh ){
//                    getCode();
//                    return;
            }

            return;
        }

    });


    //添加事件响应
    can.addEventListener("touchend", function(e){
    });

    can.addEventListener("click", function(e){
        var x= event.pageX ;
        var y= event.pageY ;

        var by = $(window).height() * 0.5;
        var bw = $(window).width() * 0.3;
        var bh = $(window).height() *0.06;
        var bx2 = $(window).width()* 0.52;

        if (App.timer == null) {
            if(x>bx2 && x<bx2+bw && y>by && y<by+bh ){
                getCode();
            }
        }
    });

    //获取点击坐标
    function getEventPosition(ev){
        var x, y;
        if (ev.layerX || ev.layerX == 0) {
            x = ev.layerX;
//                y = ev.layerY;
        }else if (ev.offsetX || ev.offsetX == 0) { // Opera
            x = ev.offsetX;
//                y = ev.offsetY;
        }
//            return {x: x, y: y};
        return x;
    }

};
function getId(id) {
    return document.getElementById(id);
}

//初始化 游戏窗口大小
function initWindows(){
    var winHeight=$(window).height();
    var winWidth=$(window).width();
//        alert("height:"+winHeight+",width:"+winWidth);
    var oMainView=$("#canvas");
    oMainView.height(winHeight);
    if(winWidth>winHeight){
        oMainView.width(winHeight*0.5625);
    }else{
        oMainView.width(winWidth);
    }

}

//canvas 圆角矩形
CanvasRenderingContext2D.prototype.roundRect =
    function(x, y, width, height, radius, fill, stroke) {
        if (typeof stroke == "undefined") {
            stroke = true;
        }
        if (typeof radius === "undefined") {
            radius = 5;
        }
        this.beginPath();
        this.moveTo(x + radius, y);
        this.lineTo(x + width - radius, y);
        this.quadraticCurveTo(x + width, y, x + width, y + radius);
        this.lineTo(x + width, y + height - radius);
        this.quadraticCurveTo(x + width, y + height, x + width - radius, y+ height);
        this.lineTo(x + radius, y + height);
        this.quadraticCurveTo(x, y + height, x, y + height - radius);
        this.lineTo(x, y + radius);
        this.quadraticCurveTo(x, y, x + radius, y);
        this.closePath();
        if (stroke) {
            this.stroke();
        }
        if (fill) {
            this.fill();
        }
    };
//-->

function showDialog(data){
    var closable = alertify.dialog('prompt');
    closable.setting({
        'title': '恭喜你获得'+data.money+'元抵用券',
        'labels':{ok:'确定', cancel:'关闭'},
        'message':
        "<p>1、	活动时间:1月22日——3月5日 ;</p>"+
        "<p>2、	活动地点：上海、北京、天津 ;</p>" +
        "<p>3、	活动奖品：10元、20元、50元、100元蛋糕优惠券 ;</p>" +
        "<p>4、	优惠券仅限门店内购买新年大蛋糕时使用，不找零、不兑现、不可叠加使用 ;</p>" +
        "<p>5、	不可与店内其他优惠共同使用 ;</p>" +
        "<p>6、	本活动最终解释权归面包新语所有。</p>" +
        "<p style='color:#f00'>请自行截图或者复制保存兑换码</p>",
        'value':data.code,
        'onok': function(){
            closable.close();
        },
        'oncancel': function(){
            closable.close();
        }
    });
    closable.show();

}

function getCode(){
    if(breadCode!=null){
        showDialog(breadCode);
        return;
    }

    var jsonSent={};
    jsonSent.idx=calcLevel(App.bestScore);
    $.ajax({
        url: "http://zhixin.me:8080/know-heart/breakTalk/retrieveBreadTalkCode",
        type: "POST",
        dataType: "json",
        data: jsonSent,
        success: function (data, textStatus, jqXHR) {
            var result = data.success;

            console.log(data);
            if (result == 1) {
                breadCode=data;
                showDialog(breadCode);
            }

        }
    });
}

function calcLevel(score){
    if(score<=1000){
        return 0;
    }else if(score>1000 && score<=1300){
        return 1;
    }else if(score>1300 && score<=1600){
        return 2;
    }else if(score>1600){
        return 3;
    }

}