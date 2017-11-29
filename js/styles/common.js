'use strict';
/**
 * @class StylesCommon
 * @desc 通用样式
 * */


var React   = require('react-native');
import {
  StyleSheet,
  PixelRatio,
  Dimensions,
} from 'react-native';
var cell_w = Dimensions.get('window').width;
const commonStyles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F5F9'
    }
});
module.exports = commonStyles;
