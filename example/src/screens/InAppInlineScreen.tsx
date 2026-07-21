import { useState } from 'react';
import { Button, ScrollView, StyleSheet, Text, View } from 'react-native';
import NoCapTextInput from '../components/NoCapTextInput';
import { InAppInlineView } from '@dengage-tech/react-native-dengage';

export default function InAppInlineScreen() {
  const [propertyId, setPropertyId] = useState('1');
  const [screenName, setScreenName] = useState('inline');
  const [showInline, setShowInline] = useState(false);
  const [nativeReportsHidden, setNativeReportsHidden] = useState(false);

  const hideIfNotFound = true;
  const collapseSlot = hideIfNotFound && nativeReportsHidden;

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <NoCapTextInput
        placeholder="Property ID"
        value={propertyId}
        onChangeText={setPropertyId}
      />
      <NoCapTextInput
        placeholder="Screen Name"
        value={screenName}
        onChangeText={setScreenName}
      />
      <Button
        title="Show InApp Inline"
        onPress={() => {
          setShowInline(true);
          setNativeReportsHidden(false);
        }}
      />
      {showInline && !collapseSlot && (
        <View style={styles.inlineSlot}>
          <InAppInlineView
            propertyId={propertyId}
            screenName={screenName}
            customParams={{}}
            hideIfNotFound={hideIfNotFound}
            onInlineVisibilityChanged={(e) => {
              const isHidden = e.nativeEvent.isHidden;
              console.log('[InAppInline] onInlineVisibilityChanged isHidden =', isHidden);
              setNativeReportsHidden(isHidden);
            }}
            style={styles.inline}
          />
        </View>
      )}
      {showInline && (
        <Text style={styles.sizeTestHint}>
          Testing size: this line should sit directly under the button when the
          inline slot collapses (hidden / not found). If you still see a big gap
          above this text, collapse did not reclaim height.
        </Text>
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { padding: 16 },
  inlineSlot: {
    marginTop: 16,
    alignSelf: 'stretch',
  },
  inline: {
    height: 244,
  },
  sizeTestHint: {
    marginTop: 12,
    fontSize: 13,
    lineHeight: 18,
    color: '#444',
  },
});
