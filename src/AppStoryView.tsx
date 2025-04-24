/* Copyright Related Digital and Contributors */

import React from 'react';
import {
  requireNativeComponent,
  type StyleProp,
  type ViewStyle,
  StyleSheet,
} from 'react-native';

const RCTStoriesListView =
  requireNativeComponent<RCTStoriesListViewProps>('RCTStoriesListView');

interface RCTStoriesListViewProps {
  storyPropertyId: string;
  screenName: string;
  style?: StyleProp<ViewStyle>;
}

export interface StoriesListViewProps {
  storyPropertyId: string;
  screenName: string;
  style?: StyleProp<ViewStyle>;
}

const styles = StyleSheet.create({
  defaultStyle: {
    minHeight: 110,
  },
});

export class StoriesListView2 extends React.Component<StoriesListViewProps> {
  render() {
    console.log('StoriesListView', this.props.storyPropertyId);
    return (
      <RCTStoriesListView
        storyPropertyId={this.props.storyPropertyId}
        screenName={this.props.screenName}
        style={[styles.defaultStyle, this.props.style]}
      />
    );
  }
}


export class StoriesListView extends React.Component<StoriesListViewProps> {
  render() {
    console.log('StoriesListView', this.props.storyPropertyId);
    return (
      <div>asd</div>
    );
  }
}
