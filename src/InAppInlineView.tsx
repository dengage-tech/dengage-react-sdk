import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import {
  requireNativeComponent,
  type NativeSyntheticEvent,
  type StyleProp,
  type ViewStyle,
} from 'react-native';

type VisibilityPayload = { isHidden: boolean };

/** Native may briefly report hidden while the SDK loads inline HTML; unmounting immediately prevents display. */
const COLLAPSE_WHEN_HIDDEN_DEBOUNCE_MS = 320;

type RCTNativeInAppInlineProps = {
  propertyId: string;
  screenName: string;
  customParams: Record<string, string>;
  hideIfNotFound?: boolean;
  style?: StyleProp<ViewStyle>;
  onVisibilityChanged?: (event: NativeSyntheticEvent<VisibilityPayload>) => void;
};

const RCTInAppInlineView =
  requireNativeComponent<RCTNativeInAppInlineProps>('RCTInAppInlineView');

export interface InAppInlineViewProps {
  /**
   * Changing this remounts the native view so the SDK performs a fresh inline load (same idea as
   * unmounting the slot and showing again).
   */
  propertyId: string;
  screenName: string;
  customParams: Record<string, string>;
  /** When true (default), the view takes no layout space if no inline message exists for [propertyId]. */
  hideIfNotFound?: boolean;
  /** Fired when native inline visibility is determined (after the same debounce rules as Flutter). */
  onVisibilityChanged?: (isHidden: boolean) => void;
  style?: StyleProp<ViewStyle>;
}

export function InAppInlineView({
  propertyId,
  screenName,
  customParams,
  hideIfNotFound = true,
  onVisibilityChanged,
  style,
}: InAppInlineViewProps) {
  const [slotCollapsed, setSlotCollapsed] = useState(false);
  const customParamsKey = useMemo(() => JSON.stringify(customParams), [customParams]);
  const collapseWhenHiddenTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    if (collapseWhenHiddenTimerRef.current != null) {
      clearTimeout(collapseWhenHiddenTimerRef.current);
      collapseWhenHiddenTimerRef.current = null;
    }
    setSlotCollapsed(false);
  }, [propertyId, screenName, customParamsKey]);

  useEffect(() => {
    return () => {
      if (collapseWhenHiddenTimerRef.current != null) {
        clearTimeout(collapseWhenHiddenTimerRef.current);
        collapseWhenHiddenTimerRef.current = null;
      }
    };
  }, []);

  const handleVisibility = useCallback(
    (e: NativeSyntheticEvent<VisibilityPayload>) => {
      const raw = e.nativeEvent as VisibilityPayload & Record<string, unknown>;
      const hidden = Boolean(raw?.isHidden);
      onVisibilityChanged?.(hidden);
      if (!hideIfNotFound) {
        return;
      }
      if (!hidden) {
        if (collapseWhenHiddenTimerRef.current != null) {
          clearTimeout(collapseWhenHiddenTimerRef.current);
          collapseWhenHiddenTimerRef.current = null;
        }
        setSlotCollapsed(false);
        return;
      }
      if (collapseWhenHiddenTimerRef.current != null) {
        clearTimeout(collapseWhenHiddenTimerRef.current);
      }
      collapseWhenHiddenTimerRef.current = setTimeout(() => {
        collapseWhenHiddenTimerRef.current = null;
        setSlotCollapsed(true);
      }, COLLAPSE_WHEN_HIDDEN_DEBOUNCE_MS);
    },
    [hideIfNotFound, onVisibilityChanged]
  );

  if (hideIfNotFound && slotCollapsed) {
    return null;
  }

  const nativeRemountKey = propertyId.trim();

  return (
    <RCTInAppInlineView
      key={nativeRemountKey}
      propertyId={propertyId}
      screenName={screenName}
      customParams={customParams}
      hideIfNotFound={hideIfNotFound}
      onVisibilityChanged={handleVisibility}
      style={[{ width: '100%' }, style]}
    />
  );
}
