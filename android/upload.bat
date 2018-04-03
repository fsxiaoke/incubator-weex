copy ..\packages\weex-js-framework\index.js .\sdk\assets\main.js  && ^
call gradlew weex_sdk:upload -PcompileType=weex_debug  &&  ^
copy ..\packages\weex-js-framework\index.min.js .\sdk\assets\main.js && ^
call gradlew weex_sdk:upload -PcompileType=weex_release & ^
pause