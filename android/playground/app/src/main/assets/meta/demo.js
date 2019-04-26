// { "framework": "Vue" }
/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

	var __vue_exports__, __vue_options__
	var __vue_styles__ = []

	/* styles */
	__vue_styles__.push(__webpack_require__(1)
	)

	/* script */
	__vue_exports__ = __webpack_require__(2)

	/* template */
	var __vue_template__ = __webpack_require__(15)
	__vue_options__ = __vue_exports__ = __vue_exports__ || {}
	if (
	  typeof __vue_exports__.default === "object" ||
	  typeof __vue_exports__.default === "function"
	) {
	if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
	__vue_options__ = __vue_exports__ = __vue_exports__.default
	}
	if (typeof __vue_options__ === "function") {
	  __vue_options__ = __vue_options__.options
	}
	__vue_options__.__file = "D:\\code\\android\\fsgit\\weex-fs-lib-1\\android\\playground\\app\\pages\\meta\\demo.vue"
	__vue_options__.render = __vue_template__.render
	__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
	__vue_options__._scopeId = "data-v-1be63b15"
	__vue_options__.style = __vue_options__.style || {}
	__vue_styles__.forEach(function (module) {
	  for (var name in module) {
	    __vue_options__.style[name] = module[name]
	  }
	})
	if (typeof __register_static_styles__ === "function") {
	  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
	}

	module.exports = __vue_exports__
	module.exports.el = 'true'
	new Vue(module.exports)


/***/ }),
/* 1 */
/***/ (function(module, exports) {

	/* WEBPACK VAR INJECTION */(function(global) {var __transformPX = function(number){if(global){return number * 750 / global.WXEnvironment.deviceWidth};return number;};
	module.exports = {
	  "commit": {
	    "fontSize": "18wx",
	    "paddingTop": "6wx",
	    "paddingRight": "6wx",
	    "paddingBottom": "6wx",
	    "paddingLeft": "6wx",
	    "borderRadius": "5wx",
	    "marginTop": "20wx",
	    "marginRight": "20wx",
	    "marginBottom": "20wx",
	    "marginLeft": "20wx",
	    "textAlign": "center",
	    "backgroundColor": "#ff2222"
	  }
	}
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

	'use strict';

	var _inputComponent = __webpack_require__(3);

	var _inputComponent2 = _interopRequireDefault(_inputComponent);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	module.exports = {
	    components: {
	        inputComponent: _inputComponent2.default,
	        selectComponent: __webpack_require__(7),
	        addressComponent: __webpack_require__(11)
	    },

	    data: {
	        items: [{
	            type: "inputComponent",
	            "api_name": "f_api_name_1473159845964",
	            metadata: {
	                label: "单行文本",
	                help_text: "输入数字"
	            }
	        }, {
	            type: "selectComponent",
	            "api_name": "f_api_name_1473159845965",
	            metadata: {
	                label: "选择框",
	                help_text: "请选择时间"
	            }
	        }, {
	            type: "addressComponent",
	            "api_name": "f_api_name_147315923265",
	            metadata: {
	                label: "选择框",
	                help_text: "请选择时间"
	            }
	        }]
	    },

	    props: {
	        result: {
	            default: "提交"
	        }
	    },

	    created: function created() {
	        // var body = weex.document.createBody('div')
	        // var inputComp = this.fsinput.createInput();
	        // var selectComp = require('./fsselect.vue').createSelect();

	        // var button = weex.document.createElement('text')
	        // button.setAttr('value', '提交')
	        //   button.setStyle('fontSize', '18wx')
	        //     button.setStyle('padding', '6wx')
	        //     button.setStyle('borderRadius', '5wx')
	        //     button.setStyle('margin', '20wx')
	        //     button.setStyle('textAlign', 'center')
	        //     button.setStyle('backgroundColor', '#ff3333')

	        // weex.document.documentElement.appendChild(body)
	        // body.appendChild(inputComp.comp)
	        // body.appendChild(selectComp.comp)
	        // body.appendChild(button)
	        // button.addEvent('click', function(){
	        //         button.setAttr('value', inputComp.getValue()+"   "+selectComp.getValue())
	        //     })

	        var fff = "console.log(\"成功运行\");";
	        var ret = this.evil("{ getA:function (a){return a;}}");
	        console.log(ret.getA(2));

	        for (var i = 0; i < 20; i++) {
	            var d1 = {
	                type: "inputComponent",
	                "api_name": "f_api_name_1473159845964",
	                metadata: {
	                    label: "单行文本" + i,
	                    help_text: "输入数字" + i
	                }
	            };
	            this.items[i] = d1;
	        }
	    },

	    methods: {

	        commit: function commit() {
	            var value = "";
	            for (var i = 0, len = this.items.length; i < len; i++) {
	                var item = this.items[i];
	                value = value + " " + this.$refs[item.api_name][0].getValue();
	            }

	            this.result = value;
	        },
	        evil: function evil(str) {
	            var fn = Function;
	            return new fn('return ' + str)();
	        }

	    }
	}; //
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//

/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

	var __vue_exports__, __vue_options__
	var __vue_styles__ = []

	/* styles */
	__vue_styles__.push(__webpack_require__(4)
	)

	/* script */
	__vue_exports__ = __webpack_require__(5)

	/* template */
	var __vue_template__ = __webpack_require__(6)
	__vue_options__ = __vue_exports__ = __vue_exports__ || {}
	if (
	  typeof __vue_exports__.default === "object" ||
	  typeof __vue_exports__.default === "function"
	) {
	if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
	__vue_options__ = __vue_exports__ = __vue_exports__.default
	}
	if (typeof __vue_options__ === "function") {
	  __vue_options__ = __vue_options__.options
	}
	__vue_options__.__file = "D:\\code\\android\\fsgit\\weex-fs-lib-1\\android\\playground\\app\\pages\\meta\\inputComponent.vue"
	__vue_options__.render = __vue_template__.render
	__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
	__vue_options__._scopeId = "data-v-7220b505"
	__vue_options__.style = __vue_options__.style || {}
	__vue_styles__.forEach(function (module) {
	  for (var name in module) {
	    __vue_options__.style[name] = module[name]
	  }
	})
	if (typeof __register_static_styles__ === "function") {
	  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
	}

	module.exports = __vue_exports__


/***/ }),
/* 4 */
/***/ (function(module, exports) {

	/* WEBPACK VAR INJECTION */(function(global) {var __transformPX = function(number){if(global){return number * 750 / global.WXEnvironment.deviceWidth};return number;};
	module.exports = {
	  "body": {
	    "width": 750,
	    "paddingTop": "10wx",
	    "paddingRight": "10wx",
	    "paddingBottom": "10wx",
	    "paddingLeft": "10wx"
	  },
	  "label": {
	    "width": 750,
	    "fontSize": "14wx"
	  },
	  "input": {
	    "paddingTop": "6wx",
	    "paddingRight": "6wx",
	    "paddingBottom": "6wx",
	    "paddingLeft": "6wx",
	    "borderWidth": "1wx",
	    "borderRadius": "2wx",
	    "borderColor": "#888888",
	    "marginTop": "5wx",
	    "fontSize": "18wx"
	  }
	}
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ }),
/* 5 */
/***/ (function(module, exports) {

	'use strict';

	//
	//
	//
	//
	//
	//
	//
	//
	//

	module.exports = {
	    data: function data() {
	        return {
	            txtInput: ''
	        };
	    },
	    watch: {
	        txtInput: function txtInput(nv, ov) {
	            console.log(nv);
	        }
	    },
	    props: {
	        metadata: {
	            default: ''
	        }
	    },

	    created: function created() {
	        //this.value=this.metadata.value;
	    },

	    methods: {
	        getValue: function getValue() {
	            return this.txtInput;
	        },
	        oninput: function oninput(event) {
	            this.txtInput = event.value;
	            this.$refs['input'].attr.value = this.txtInput;
	        }
	    }
	};

/***/ }),
/* 6 */
/***/ (function(module, exports) {

	module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
	  return _c('div', {
	    staticClass: ["body"]
	  }, [_c('text', {
	    staticClass: ["label"],
	    attrs: {
	      "value": _vm.metadata.label
	    }
	  }), _c('input', {
	    ref: "input",
	    staticClass: ["input"],
	    attrs: {
	      "placeholder": _vm.metadata.help_text
	    },
	    on: {
	      "input": _vm.oninput
	    }
	  })])
	},staticRenderFns: []}
	module.exports.render._withStripped = true

/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

	var __vue_exports__, __vue_options__
	var __vue_styles__ = []

	/* styles */
	__vue_styles__.push(__webpack_require__(8)
	)

	/* script */
	__vue_exports__ = __webpack_require__(9)

	/* template */
	var __vue_template__ = __webpack_require__(10)
	__vue_options__ = __vue_exports__ = __vue_exports__ || {}
	if (
	  typeof __vue_exports__.default === "object" ||
	  typeof __vue_exports__.default === "function"
	) {
	if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
	__vue_options__ = __vue_exports__ = __vue_exports__.default
	}
	if (typeof __vue_options__ === "function") {
	  __vue_options__ = __vue_options__.options
	}
	__vue_options__.__file = "D:\\code\\android\\fsgit\\weex-fs-lib-1\\android\\playground\\app\\pages\\meta\\selectComponent.vue"
	__vue_options__.render = __vue_template__.render
	__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
	__vue_options__._scopeId = "data-v-78a6151f"
	__vue_options__.style = __vue_options__.style || {}
	__vue_styles__.forEach(function (module) {
	  for (var name in module) {
	    __vue_options__.style[name] = module[name]
	  }
	})
	if (typeof __register_static_styles__ === "function") {
	  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
	}

	module.exports = __vue_exports__


/***/ }),
/* 8 */
/***/ (function(module, exports) {

	/* WEBPACK VAR INJECTION */(function(global) {var __transformPX = function(number){if(global){return number * 750 / global.WXEnvironment.deviceWidth};return number;};
	module.exports = {
	  "body": {
	    "width": 750,
	    "paddingTop": "10wx",
	    "paddingRight": "10wx",
	    "paddingBottom": "10wx",
	    "paddingLeft": "10wx"
	  },
	  "label": {
	    "width": 750,
	    "fontSize": "14wx"
	  },
	  "text": {
	    "paddingTop": "6wx",
	    "paddingRight": "6wx",
	    "paddingBottom": "6wx",
	    "paddingLeft": "6wx",
	    "borderWidth": "1wx",
	    "borderRadius": "2wx",
	    "borderColor": "#888888",
	    "marginTop": "5wx",
	    "fontSize": "18wx"
	  }
	}
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ }),
/* 9 */
/***/ (function(module, exports) {

	'use strict';

	//
	//
	//
	//
	//
	//
	//

	module.exports = {
	    data: function data() {
	        return {
	            result: ''
	        };
	    },

	    props: {
	        metadata: {
	            default: ''
	        }
	    },

	    created: function created() {},

	    methods: {
	        getValue: function getValue() {
	            return this.result;
	        },
	        select: function select(event) {
	            var self = this;
	            var picker = weex.requireModule('picker');
	            picker.pickDate({
	                'value': '2016-11-28',
	                'max': '2029-11-28',
	                'min': '2015-11-28'
	            }, function (ret) {
	                var result = ret.result;
	                if (result == 'success') {
	                    self.result = ret.data;
	                    self.$refs.text.setAttr("value", ret.data);
	                }
	            });
	        }

	    }
	};

/***/ }),
/* 10 */
/***/ (function(module, exports) {

	module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
	  return _c('div', {
	    staticClass: ["body"]
	  }, [_c('text', {
	    staticClass: ["label"],
	    attrs: {
	      "value": _vm.metadata.label
	    }
	  }), _c('text', {
	    ref: "text",
	    staticClass: ["text"],
	    attrs: {
	      "value": _vm.metadata.help_text
	    },
	    on: {
	      "click": _vm.select
	    }
	  })])
	},staticRenderFns: []}
	module.exports.render._withStripped = true

/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

	var __vue_exports__, __vue_options__
	var __vue_styles__ = []

	/* styles */
	__vue_styles__.push(__webpack_require__(12)
	)

	/* script */
	__vue_exports__ = __webpack_require__(13)

	/* template */
	var __vue_template__ = __webpack_require__(14)
	__vue_options__ = __vue_exports__ = __vue_exports__ || {}
	if (
	  typeof __vue_exports__.default === "object" ||
	  typeof __vue_exports__.default === "function"
	) {
	if (Object.keys(__vue_exports__).some(function (key) { return key !== "default" && key !== "__esModule" })) {console.error("named exports are not supported in *.vue files.")}
	__vue_options__ = __vue_exports__ = __vue_exports__.default
	}
	if (typeof __vue_options__ === "function") {
	  __vue_options__ = __vue_options__.options
	}
	__vue_options__.__file = "D:\\code\\android\\fsgit\\weex-fs-lib-1\\android\\playground\\app\\pages\\meta\\addressComponent.vue"
	__vue_options__.render = __vue_template__.render
	__vue_options__.staticRenderFns = __vue_template__.staticRenderFns
	__vue_options__._scopeId = "data-v-49e4f2ca"
	__vue_options__.style = __vue_options__.style || {}
	__vue_styles__.forEach(function (module) {
	  for (var name in module) {
	    __vue_options__.style[name] = module[name]
	  }
	})
	if (typeof __register_static_styles__ === "function") {
	  __register_static_styles__(__vue_options__._scopeId, __vue_styles__)
	}

	module.exports = __vue_exports__


/***/ }),
/* 12 */
/***/ (function(module, exports) {

	/* WEBPACK VAR INJECTION */(function(global) {var __transformPX = function(number){if(global){return number * 750 / global.WXEnvironment.deviceWidth};return number;};
	module.exports = {
	  "body": {
	    "width": 750,
	    "paddingTop": "10wx",
	    "paddingRight": "10wx",
	    "paddingBottom": "10wx",
	    "paddingLeft": "10wx"
	  },
	  "label": {
	    "width": 750,
	    "fontSize": "14wx"
	  },
	  "text": {
	    "paddingTop": "6wx",
	    "paddingRight": "6wx",
	    "paddingBottom": "6wx",
	    "paddingLeft": "6wx",
	    "borderWidth": "1wx",
	    "borderRadius": "2wx",
	    "borderColor": "#888888",
	    "marginTop": "5wx",
	    "fontSize": "18wx"
	  }
	}
	/* WEBPACK VAR INJECTION */}.call(exports, (function() { return this; }())))

/***/ }),
/* 13 */
/***/ (function(module, exports) {

	'use strict';

	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//

	var activityApi = weex.requireModule('ActivityApiModule');
	module.exports = {
	    data: function data() {
	        return {
	            result: ''
	        };
	    },

	    props: {
	        metadata: {
	            default: ''
	        }

	    },

	    created: function created() {
	        var self = this;
	        activityApi.addEventListener('onActivityResult_js', function (params) {
	            var data = params.data;
	            var selectAddress = data.datacode.select_address;
	            self.$refs.country.setAttr("value", selectAddress.countryName + "/" + selectAddress.province + "/" + selectAddress.city + "/" + selectAddress.district);
	            self.$refs.location.setAttr("value", selectAddress.address);
	            self.$refs.detail.setAttr("value", selectAddress.address);
	        });
	    },

	    methods: {
	        getValue: function getValue() {
	            return this.result;
	        },
	        selectAddress: function selectAddress(event) {
	            var params = {};
	            activityApi.startActivityForResult(1, 'fs://cfplug/MapSelectAddressActivity', params);
	        }

	    }
	};

/***/ }),
/* 14 */
/***/ (function(module, exports) {

	module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
	  return _c('div', {
	    staticClass: ["body"]
	  }, [_c('text', {
	    staticClass: ["label"],
	    attrs: {
	      "value": "国家/省/市/区"
	    }
	  }), _c('text', {
	    ref: "country",
	    staticClass: ["text"],
	    attrs: {
	      "value": "请选择"
	    },
	    on: {
	      "click": _vm.selectAddress
	    }
	  }), _c('text', {
	    staticClass: ["label"],
	    attrs: {
	      "value": "定位"
	    }
	  }), _c('text', {
	    ref: "location",
	    staticClass: ["text"],
	    attrs: {
	      "value": "请选择"
	    },
	    on: {
	      "click": _vm.selectAddress
	    }
	  }), _c('text', {
	    staticClass: ["label"],
	    attrs: {
	      "value": "详细地址"
	    }
	  }), _c('text', {
	    ref: "detail",
	    staticClass: ["text"],
	    attrs: {
	      "value": "请选择"
	    },
	    on: {
	      "click": _vm.selectAddress
	    }
	  })])
	},staticRenderFns: []}
	module.exports.render._withStripped = true

/***/ }),
/* 15 */
/***/ (function(module, exports) {

	module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
	  return _c('div', [_c('switch'), _c('list', {
	    attrs: {
	      "showScrollbar": "true",
	      "scrollable": "true"
	    }
	  }, _vm._l((_vm.items), function(item, i) {
	    return _c('cell', {
	      ref: item.api_name,
	      refInFor: true,
	      appendAsTree: true,
	      attrs: {
	        "append": "tree"
	      }
	    }, [_c(item.type, {
	      tag: "component",
	      attrs: {
	        "metadata": item.metadata
	      }
	    })], 1)
	  })), _c('text', {
	    staticClass: ["commit"],
	    attrs: {
	      "value": "result"
	    },
	    on: {
	      "click": _vm.functionOne
	    }
	  }), _c('div', {
	    ref: "jsdiv"
	  })])
	},staticRenderFns: []}
	module.exports.render._withStripped = true

/***/ })
/******/ ]);