import { useState } from 'react';
import {
  Button,
  ScrollView,
  StyleSheet,
  Switch,
  Text,
  View,
} from 'react-native';
import NoCapTextInput from '../components/NoCapTextInput';
import { InAppInlineView } from '@dengage-tech/react-native-dengage';

export default function InAppInlineScreen() {
  const [propertyId, setPropertyId] = useState('1');
  const [screenName, setScreenName] = useState('inline');
  const [showInline, setShowInline] = useState(false);
  const [hideIfNotFound, setHideIfNotFound] = useState(true);
  const [nativeHidden, setNativeHidden] = useState<boolean | null>(null);

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
      <View style={styles.row}>
        <Text style={styles.label}>hideIfNotFound (collapse when empty)</Text>
        <Switch value={hideIfNotFound} onValueChange={setHideIfNotFound} />
      </View>
      <Text style={styles.hint}>
        With hideIfNotFound: if no inline message matches the property id, the red
        box below stays empty (no reserved height). Toggle off to always keep a
        slot.
      </Text>
      <Button title="Show InApp Inline" onPress={() => setShowInline(true)} />
      <Button
        title="Reset demo"
        onPress={() => {
          setShowInline(false);
          setNativeHidden(null);
        }}
      />
      {showInline && (
        <View style={styles.slot}>
          <Text style={styles.status}>
            Native visibility:{' '}
            {nativeHidden == null
              ? '…'
              : nativeHidden
                ? 'hidden (no / empty inline)'
                : 'visible slot'}
          </Text>
          <InAppInlineView
            propertyId={propertyId}
            screenName={screenName}
            customParams={{}}
            hideIfNotFound={hideIfNotFound}
            onVisibilityChanged={setNativeHidden}
            style={styles.inline}
          />
        </View>
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { padding: 16 },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginTop: 12,
  },
  label: { flex: 1, marginRight: 8 },
  hint: { marginTop: 8, fontSize: 13, color: '#555' },
  slot: {
    marginTop: 16,
    minHeight: 48,
    borderWidth: 2,
    borderColor: '#c62828',
    borderStyle: 'dashed',
    padding: 8,
  },
  status: { fontSize: 12, marginBottom: 8, color: '#333' },
  inline: { minHeight: 200, width: '100%' },
});
