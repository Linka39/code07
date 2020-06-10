
function ResizeImages()
{
   var myimg,oldwidth,oldheight;
   var maxwidth=800;
   var maxheight=1000
   var imgs = document.getElementById('content').getElementsByTagName('img');   //濡傛灉浣犲畾涔夌殑id涓嶆槸article锛岃淇敼姝ゅ

   for(i=0;i<imgs.length;i++){
     myimg = imgs[i];
     if(myimg.width > myimg.height)
     {
         if(myimg.width > maxwidth)
         {
            oldwidth = myimg.width;
            myimg.height = myimg.height * (maxwidth/oldwidth);
            myimg.width = maxwidth;
         }
     }else{
         if(myimg.height > maxheight)
         {
            oldheight = myimg.height;
            myimg.width = myimg.width * (maxheight/oldheight);
            myimg.height = maxheight;
         }
     }
   }
}

function showRegister() {
    layer.open({
        type: 2,
        title: '用户注册页面',
     /*   shadeClose: true, //点击阴影关闭弹窗
        shade: 0.1,*/
        area: ['480px', '600px'],
        content: '/register.html' //iframe的url
    });
}

function reloadPage() {
    window.location.reload();
}
