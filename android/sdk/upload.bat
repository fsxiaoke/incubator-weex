copy ..\..\packages\weex-js-framework\index.js .\assets\main.js  && ^
call gradle upload -PcompileType=weex_debug  &&  ^
copy ..\..\packages\weex-js-framework\index.min.js .\assets\main.js && ^
call gradle upload -PcompileType=weex_release & ^
pause