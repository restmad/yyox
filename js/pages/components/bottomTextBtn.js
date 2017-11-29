'use strict';

import React, {Component} from 'react';
import {
    Text,
    StyleSheet,
    View,
    Image,
    TouchableOpacity
} from 'react-native';

export default class BottomTextBtn extends Component {

    render() {
        return (
            <TouchableOpacity onPress={this.props.onPress} style={this.props.style}>
                <Image source={this.props.backgroundImage} style={styles.whole}>
                    <Image source={this.props.icon} style={styles.upImage} resizeMode={'center'} />
                    <Text style={styles.title}>{this.props.title}</Text>
                </Image>
            </TouchableOpacity>
        )
    }
}

const styles = StyleSheet.create({
    whole: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    upImage: {
        flex: 3,
    },
    title: {
        fontSize: 13,
        color: 'black',
        alignSelf: 'center',
        flex: 2,
        textAlign: 'center'
    }
})