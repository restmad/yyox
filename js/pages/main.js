'use strict';

import React, {Component} from 'react';
import {
    Navigator,
    View
} from 'react-native';

import Router from '../configs/router';

import { connect } from 'react-redux';

import Service from './service';

class Main extends Component {
    constructor(props) {
        super(props);
    }

    renderScene(route, navigator){
        this.router = this.router || new Router(navigator);
        let Component = route.component;
        if(Component){
            return <Component   {...route.passProps}
                                title={route.title}
                                route={{
                                    title: route.title,
                                    index: route.index
                                }}
                                router={this.router}
            />;
        }
    }

    render() {
        return (
            <Navigator initialRoute={{index:0, title: '邮客服务', component: Service}}
                       configureScene={()=>{
                            return Router.noneGesPop;
                       }}
                       renderScene={this.renderScene.bind(this)}
            />
        );
    }
}

export default connect()(Main);