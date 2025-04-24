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
  propertyId: string | null;
  screenName: string | null;
  customParams: Record<string, string> | null;
  style?: StyleProp<ViewStyle>;
}

export interface InAppInlineViewProps {
  propertyId: string | null;
  screenName: string | null;
  customParams: Record<string, string> | null;
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
        {...this.props}
        propertyId={this.props.propertyId}
        screenName={this.props.screenName}
        customParams={this.props.customParams}
        style={[styles.defaultStyle, this.props.style]}
      />
    );
  }
}
