var bridge={"default":this,call:function(a,b,c){var e,f,d="";return"function"==typeof b&&(c=b,b={}),e={data:void 0===b?null:b},"function"==typeof c&&(f="kzcb"+window.kzcb++,window[f]=c,e["_kzcbstub"]=f),e=JSON.stringify(e),window._kzbridge?d=_kzbridge.call(a,e):(window._kzwk||-1!=navigator.userAgent.indexOf("_kzbridge"))&&(d=prompt("_kzbridge="+a,e)),JSON.parse(d||"{}").data},register:function(a,b,c){var d=c?window._kzaf:window._kzf;window._kzInit||(window._kzInit=!0,setTimeout(function(){bridge.call("_kzb.kzinit")},0)),"object"==typeof b?d._obs[a]=b:d[a]=b},registerAsyn:function(a,b){this.register(a,b,!0)},hasNativeMethod:function(a,b){return this.call("_kzb.hasNativeMethod",{name:a,type:b||"all"})},disableJavascriptDialogBlock:function(a){this.call("_kzb.disableJavascriptDialogBlock",{disable:a!==!1})}};!function(){var a,b,c;if(!window._kzf){a=window.close,b={_kzf:{_obs:{}},_kzaf:{_obs:{}},kzcb:0,kzBridge:bridge,close:function(){bridge.hasNativeMethod("_kzb.closePage")?bridge.call("_kzb.closePage"):a.call(window)},_handleMessageFromNative:function(a){var h,i,j,k,l,m,b=JSON.parse(a.data),c={id:a.callbackId,complete:!0},d=this._kzf[a.method],e=this._kzaf[a.method],f=function(a,d){c.data=a.apply(d,b),bridge.call("_kzb.returnValue",c)},g=function(a,d){b.push(function(a,b){c.data=a,c.complete=b!==!1,bridge.call("_kzb.returnValue",c)}),a.apply(d,b)};if(d)f(d,this._kzf);else if(e)g(e,this._kzaf);else{if(h=a.method.split("."),h.length<2)return;if(i=h.pop(),j=h.join("."),k=this._kzf._obs,l=k[j]||{},m=l[i],m&&"function"==typeof m)return f(m,l),void 0;if(k=this._kzaf._obs,l=k[j]||{},m=l[i],m&&"function"==typeof m)return g(m,l),void 0}}};for(c in b)window[c]=b[c];bridge.register("_hasJavascriptMethod",function(a){var d,e,c=a.split(".");return c.length<2?!(!_kzf[c]&&!_kzaf[c]):(a=c.pop(),d=c.join("."),e=_kzf._obs[d]||_kzaf._obs[d],e&&!!e[a])})}}(),module.exports=bridge;