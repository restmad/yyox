'use strict';

import React, { Component } from 'react';
import {
    Navigator,
    View
} from 'react-native';
import { connect } from 'react-redux';

import Main from './pages/main';

class Root extends Component {
    constructor(props){
        super(props);
    }

    render() {
        return <Main />;
    }
}

export default connect()(Root);