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
  storyPropertyId: string | null;
  screenName: string | null;
  customParams: Record<string, string>| null;
  style?: StyleProp<ViewStyle>;
}

export interface StoriesListViewProps {
  storyPropertyId: string | null;
  screenName: string | null;
  customParams: Record<string, string>| null;
  style?: StyleProp<ViewStyle>;
}

const styles = StyleSheet.create({
  defaultStyle: {
    minHeight: 110,
  },
});

export class StoriesListView extends React.Component<StoriesListViewProps> {
  render() {
    return (
      <RCTStoriesListView
        storyPropertyId={this.props.storyPropertyId}
        screenName={this.props.screenName}
        customParams={this.props.customParams}
        style={[styles.defaultStyle, this.props.style]}
      />
    );
  }
}