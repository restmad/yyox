'use strict';

class Response {}
Response.protoTypes = {
    code: Number,
    msg: String,
    data: Array
};

const BaseUrl = "http://app.yyox.com/";
//const BaseUrl = 'http://192.168.4.172:7070/';
//const BaseUrl = "http://10.0.0.19:8080/";
//const BaseUrl = "http://52.69.220.34:80/";

const WebApi = {
    get BANNERDETAILAPI() {return BaseUrl + 'app/banner/index.html'},
    get SAFEGUARDAPI() {return BaseUrl + 'app/server/guarantee/guarantee.html'},
    get SEVERAPI() {return BaseUrl + 'app/server/serve-yyox/serve-yyox.html'},
    get CALCULATERULE() {return BaseUrl + 'app/PriceRule/rule.html#/USA/yy'}
};

const Api = {
    get GETPRICEBASEDATAAPI() {return BaseUrl + 'app/order/warehouse'},
    get CALCULATEPRICEAPI() {return BaseUrl + 'app/utility/priceCalculation/appcalculation'}
};

const Process = {
    get FETCH_BEGIN() {return 'FETCH_BEGIN'},
    get FETCH_SUCCESS() {return 'FETCH_SUCCESS'},
    get FETCH_FAIL() {return 'FETCH_FAIL'}
};

class Net {

    static commonGetModel(api, params, callback) {
        return this.commonModel('GET', api, params, callback);
    }

    static commonPostModel(api, params, callback) {
        return this.commonModel('POST', api, params, callback);
    }

    static commonModel(method, api, params, callback) {
        if (method === 'GET') {
            api = this.generateApi(method, api, params);
        }

        let setting = this.generateSetting(method, api, params);

        return fetch(api, setting)
            .then(response => {
                return response.json();
            })
            .then(json=>{
                this.dealWithJson(json, callback);
            })
            .catch(e=>{
                this.dealWithJson(null, callback);
            })
    }

    static commonGetRefresh(api, params, currentCount, pageSize, callback) {
        return this.commonRefresh('GET', api, params, currentCount, pageSize, callback);
    }

    static commonPostRefresh(api, params, currentCount, pageSize, callback) {
        this.commonRefresh('POST', api, params, currentCount, pageSize, callback);
    }

    static commonRefresh(method, api, params, currentCount, pageSize, callback) {
        if (method === 'GET') {
            api = this.generateApi(method, api, params);
        }

        if (pageSize > 0) {
            params['pageNo'] = currentCount / pageSize + 1;
        }

        let setting = this.generateSetting(method, api, params);

        return fetch(api, setting)
            .then(response => {
                return response.json();
            })
            .then(json=>{
                if (json.other && json.other.totalCount) {
                    if (json.other.totalCount < currentCount + pageSize) {
                        json.status = 999;
                    }
                } else {
                    json.status = 999;
                }
                this.dealWithJson(json, callback);
            })
            .catch(e=>{
                this.dealWithJson(null, callback);
            })
    }

    static appendParams(url, paramsObj){
        let params = '';
        if (!paramsObj) {
            return url;
        } else {
            for (let pro in paramsObj) {
                params += pro + '=' + paramsObj[pro] + '&'
            }
            params = params.substring(0, params.length-1)
        }

        return url += params ? ("?" + params) : '';
    }

    static setNetHeaders(api) {
        if (api.match(Api.CALCULATEPRICEAPI)) {
            return {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }
        return  null;
    }

    static generateSetting(method, api, params) {
        let setting = {};
        setting.credentials = 'same-origin';
        setting.method = method;
        if (method === 'GET') {

        } else if (method === 'POST') {
            setting.body = JSON.stringify(params);
        }
        let headers = this.setNetHeaders(api);
        if (headers) setting.headers = headers;
        return setting;
    }

    static generateApi(method, api, params) {
        if (method === 'GET') {
            api = this.appendParams(api, params);
        }
        return api;
    }

    static dealWithJson(json, callback) {
        let res = this.generateResponse(json);
        this.sendCallback(res, callback);
    }

    static generateResponse(json) {
        let res = new Response();
        if (!json) {
            res.code = 1;
            res.msg = '网络错误';
            return res;
        }
        res.code = json.status;
        res.data = json.data;
        res.msg = this.generateMsg(json);
        return res;
    }

    static generateMsg(json) {
        if (!json) return null;
        return json.msgs;
    }

    static sendCallback(res, callback) {
        if (callback) callback(res);
    }
}

export {Net, WebApi, Process, Api, Response};