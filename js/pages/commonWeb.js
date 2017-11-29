'use strict';

import React, {Component} from 'react';
import {
    StyleSheet,
    Text,
    View,
    TouchableOpacity,
    Dimensions,
    WebView,
    ActivityIndicator
} from 'react-native';

import { PixelRatio } from 'react-native';

import NavBar from './components/navbar';
import commonStyles from '../styles/common';

import { connect } from 'react-redux';

var {height, width} = Dimensions.get('window');

class CommonWeb extends Component {

    constructor(props){
        super(props);
    }

    _webView() {
        return (
            <WebView source={{uri: this.props.uri, method: this.props.method || 'get'}}
                     renderLoading={()=>{
                        return (
                            <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
                                <ActivityIndicator />
                            </View>
                        )
                     }}
                     startInLoadingState={true}
            />
        )
    }

    _mainView() {
        return (
            <View style={commonStyles.container}>
                <NavBar style={styles.navBarStyle} title={this.props.title} back={true} onPress={()=>this.props.router.pop()}/>
                {this._webView()}
            </View>
        )
    }

    render() {
        return this._mainView();
    }
}

const styles = StyleSheet.create({
    navBarStyle: {
        backgroundColor: '#1b82d2'
    }
});

const mapStateToProps = (state) => {
    return {

    }
};

const mapDispatchToProps = (dispatch) => {
    return {

    }
};

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(CommonWeb);