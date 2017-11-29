'use strict';

import React, {Component} from 'react';
import {
    Text,
    StyleSheet,
    View,
    Image,
    Platform,
    TouchableOpacity,
    Dimensions
} from 'react-native';

var {height, width} = Dimensions.get('window');

export default class NavBar extends Component {

    render() {
        return (
            <View style={[styles.whole, this.props.style]}>
                <View style={styles.container}>
                    {this.props.back && (
                        <TouchableOpacity style={styles.backBtnStyle} onPress={this.props.onPress}>
                            <Image source={require('../../imgs/nav/nav_back.png')} resizeMode={'center'}></Image>
                            <Text style={[styles.navTextStyle, this.props.backStyle]}>{'返回'}</Text>
                        </TouchableOpacity>
                    )}
                    <Text style={[styles.title, this.props.titleStyle]}>{this.props.title}</Text>
                    {this.props.rightItem}
                </View>
            </View>
        )
    }
}

const styles = StyleSheet.create({
    whole: {
        backgroundColor: '#1b82d2',
        height: (Platform.OS === 'ios') ? 64 : 44,
        justifyContent: 'flex-end'
    },
    container: {
        height: 44,
        flexDirection: 'row'
    },
    title: {
        fontSize: 17,
        color: 'white',
        fontWeight: 'bold',
        textAlign: 'center',
        position: 'absolute',
        top: 0,
        zIndex: 1,
        height: 44,
        width: width,
        ...Platform.select({
            android: {
                textAlignVertical: 'center'
            },
            ios: {
                lineHeight: 44
            }
        })
    },
    navTextStyle: {
        color: 'white',
        fontSize: 14,
        marginLeft: 5,
    },
    backBtnStyle: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingLeft: 10,
        position: 'absolute',
        top: 0,
        height: 44,
        zIndex: 2
    }
})