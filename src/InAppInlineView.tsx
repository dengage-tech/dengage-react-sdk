'use strict';

import React from 'react';
import {
  requireNativeComponent,
  type StyleProp,
  type ViewStyle,
  StyleSheet,
} from 'react-native';


const RCTInAppInlineView =
  requireNativeComponent<RCTInAppInlineViewProps>('RCTInAppInlineView');

interface RCTInAppInlineViewProps {
  propertyId: string;
  screenName: string;
  customParams: Record<string, string>;
  style?: StyleProp<ViewStyle>;
}

export interface InAppInlineViewProps {
  propertyId: string;
  screenName: string;
  customParams: Record<string, string>;
  style?: StyleProp<ViewStyle>;
}

const styles = StyleSheet.create({
  defaultStyle: {
    minHeight: 110,
  },
});

export class InAppInlineView extends React.Component<InAppInlineViewProps> {

  render() {
    return (
      <RCTInAppInlineView
        propertyId={this.props.propertyId}
        screenName={this.props.screenName}
        customParams={this.props.customParams}
        style={[styles.defaultStyle, this.props.style]}
      />
    );
  }
}
