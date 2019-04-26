copy ..\packages\weex-js-framework\weex-js-framework.js .\sdk\assets\weex-main-jsfm.js  && ^
call gradlew weex_sdk:upload -PcompileType=weex_debug  &&  ^
copy ..\packages\weex-js-framework\weex-js-framework.min.js .\sdk\assets\weex-main-jsfm.js && ^
call gradlew weex_sdk:upload -PcompileType=weex_release & ^
pause