'use strict';
import React from 'react';

import {
    Navigator
} from 'react-native';

// Pages
import Price from '../pages/price';
import Answers from '../pages/answers';
import CommonWeb from '../pages/commonWeb';

// Config
import {noneGesPop} from './sceneConfig';

class Router {
    constructor(navigator) {
        this.navigator = navigator;
    }

    static noneGesPop = noneGesPop;

    push(props, route) {
        let routesList = this.navigator.getCurrentRoutes();
        let nextIndex = routesList[routesList.length - 1].index + 1;
        route.passProps = props;
        route.index = nextIndex;
        this.navigator.push(route)
    }

    pop() {
        this.navigator.pop()
    }

    toPrice(props){
        this.push(props, {
            component: Price,
            title: '价格'
        })
    }

    toAnswers(props) {
        this.push(props, {
            component: Answers,
            title: '常见问题'
        })
    }

    toPriceRule(props) {
        this.push(props, {
            component: CommonWeb,
            title: '计价规则'
        })
    }

    toSubServer(props) {
        this.push(props, {
            component: CommonWeb,
            title: '服务'
        })
    }

    toProtect(props) {
        this.push(props, {
            component: CommonWeb,
            title: '保障'
        })
    }

    toBannerDetail(props) {
        this.push(props, {
            component: CommonWeb,
            title: '邮客服务'
        })
    }

    replaceWithHome() {
        this.navigator.popToTop()
    }


}

module.exports = Router;

