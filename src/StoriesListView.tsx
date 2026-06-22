import React from 'react';
import {
  findNodeHandle,
  Platform,
  requireNativeComponent,
  UIManager,
  type NativeSyntheticEvent,
  type StyleProp,
  type ViewStyle,
} from 'react-native';

export type StoryVisibilityNativeEvent = {
  /** `true` when the story slot is hidden (e.g. not found with `hideIfNotFound`). */
  isHidden: boolean;
};

interface RCTStoriesListViewProps {
  storyPropertyId: string | null;
  screenName: string | null;
  customParams: Record<string, string> | null;
  hideIfNotFound?: boolean;
  onStoryVisibilityChanged?: (
    event: NativeSyntheticEvent<StoryVisibilityNativeEvent>
  ) => void;
  style?: StyleProp<ViewStyle>;
}

const RCTStoriesListView =
  requireNativeComponent<RCTStoriesListViewProps>('RCTStoriesListView');

export interface StoriesListViewProps {
  storyPropertyId: string | null;
  screenName: string | null;
  customParams: Record<string, string> | null;
  hideIfNotFound?: boolean;
  onStoryVisibilityChanged?: (
    event: NativeSyntheticEvent<StoryVisibilityNativeEvent>
  ) => void;
  style?: StyleProp<ViewStyle>;
}

function dispatchRefreshCommand(nativeRef: React.ElementRef<typeof RCTStoriesListView> | null) {
  const tag = findNodeHandle(nativeRef);
  if (tag == null) {
    return;
  }
  UIManager.dispatchViewManagerCommand(
    tag,
    UIManager.getViewManagerConfig('RCTStoriesListView').Commands.refresh,
    []
  );
}

export class StoriesListView extends React.Component<StoriesListViewProps> {
  private nativeRef = React.createRef<React.ElementRef<typeof RCTStoriesListView>>();

  refresh() {
    if (Platform.OS === 'android') {
      dispatchRefreshCommand(this.nativeRef.current);
    }
  }

  render() {
    const { hideIfNotFound, onStoryVisibilityChanged, style, ...rest } = this.props;
    return (
      <RCTStoriesListView
        ref={this.nativeRef}
        {...rest}
        hideIfNotFound={hideIfNotFound !== false}
        onStoryVisibilityChanged={onStoryVisibilityChanged}
        style={[
          {
            width: '100%',
            minHeight: Platform.OS === 'ios' ? 1 : undefined,
          },
          style,
        ]}
      />
    );
  }
}
