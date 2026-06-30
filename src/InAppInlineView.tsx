import React from 'react';
import {
  requireNativeComponent,
  type NativeSyntheticEvent,
  type StyleProp,
  type ViewStyle,
} from 'react-native';

export type InlineVisibilityNativeEvent = {
  /** Same semantics as Flutter `onVisibilityChanged`: `true` when the inline slot is hidden (e.g. not found with `hideIfNotFound`). */
  isHidden: boolean;
};

interface RCTInAppInlineViewProps {
  propertyId: string;
  screenName: string;
  customParams: Record<string, string>;
  hideIfNotFound?: boolean;
  onInlineVisibilityChanged?: (
    event: NativeSyntheticEvent<InlineVisibilityNativeEvent>
  ) => void;
  style?: StyleProp<ViewStyle>;
}

const RCTInAppInlineView =
  requireNativeComponent<RCTInAppInlineViewProps>('RCTInAppInlineView');

export interface InAppInlineViewProps {
  propertyId: string;
  screenName: string;
  customParams: Record<string, string>;
  /** When true (default), native SDK hides the inline WebView if no message matches; pair with `onInlineVisibilityChanged` to collapse layout. */
  hideIfNotFound?: boolean;
  onInlineVisibilityChanged?: (
    event: NativeSyntheticEvent<InlineVisibilityNativeEvent>
  ) => void;
  style?: StyleProp<ViewStyle>;
}

export class InAppInlineView extends React.Component<InAppInlineViewProps> {
  render() {
    const { hideIfNotFound, onInlineVisibilityChanged, style, ...rest } =
      this.props;
    return (
      <RCTInAppInlineView
        {...rest}
        hideIfNotFound={hideIfNotFound !== false}
        onInlineVisibilityChanged={onInlineVisibilityChanged}
        style={[{ width: '100%' }, style]}
      />
    );
  }
}
